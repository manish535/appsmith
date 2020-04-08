package com.external.plugins;

import com.appsmith.external.models.*;
import com.appsmith.external.pluginExceptions.AppsmithPluginError;
import com.appsmith.external.pluginExceptions.AppsmithPluginException;
import com.appsmith.external.plugins.BasePlugin;
import com.appsmith.external.plugins.PluginExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.pf4j.Extension;
import org.pf4j.PluginException;
import org.pf4j.PluginWrapper;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.sql.*;
import java.util.*;

import static com.appsmith.external.models.Connection.Mode.ReadOnly;

@Slf4j
public class PostgresPlugin extends BasePlugin {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static final String JDBC_DRIVER = "org.postgresql.Driver";

    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String SSL = "ssl";

    public PostgresPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Postgres plugin receives the query as json of the following format :
     * {
     * "cmd" : "select * from users;"
     * }
     */

    @Slf4j
    @Extension
    public static class PostgresPluginExecutor implements PluginExecutor {

        @Override
        public Mono<Object> execute(@NonNull Object connection,
                                    DatasourceConfiguration datasourceConfiguration,
                                    ActionConfiguration actionConfiguration) {

            Connection conn = (Connection) connection;

            Map<String, Object> queryJson = actionConfiguration.getQuery();
            String query = (String) queryJson.get("cmd");

            if (query == null) {
                return pluginErrorMono("Missing required parameter: Query.");
            }

            List<Map<String, Object>> rowsList = new ArrayList<>(50);

            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int colCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>(colCount);
                    for (int i = 1; i <= colCount; i++) {
                        row.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    rowsList.add(row);
                }

            } catch (SQLException e) {
                return pluginErrorMono(e);

            }

            ActionExecutionResult result = new ActionExecutionResult();
            result.setBody(objectMapper.valueToTree(rowsList));
            result.setShouldCacheResponse(true);
            System.out.println("In the PostgresPlugin, got action execution result: " + result.toString());
            return Mono.just(result);
        }

        private Mono<Object> pluginErrorMono(Object... args) {
            return Mono.error(new AppsmithPluginException(AppsmithPluginError.PLUGIN_ERROR, args));
        }

        @Override
        public Object datasourceCreate(DatasourceConfiguration datasourceConfiguration) {
            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                return pluginErrorMono("Error loading Postgres JDBC Driver class.");
            }

            String url;
            AuthenticationDTO authentication = datasourceConfiguration.getAuthentication();

            Properties properties = new Properties();
            properties.putAll(Map.of(
                    USER, authentication.getUsername(),
                    PASSWORD, authentication.getPassword(),
                    // TODO: Set SSL connection parameters.
                    SSL, datasourceConfiguration.getConnection().getSsl() != null
            ));

            if (CollectionUtils.isEmpty(datasourceConfiguration.getEndpoints())) {
                url = datasourceConfiguration.getUrl();

            } else {
                StringBuilder urlBuilder = new StringBuilder("jdbc:postgresql://");
                for (Endpoint endpoint : datasourceConfiguration.getEndpoints()) {
                    urlBuilder
                            .append(endpoint.getHost())
                            .append(':')
                            .append(ObjectUtils.defaultIfNull(endpoint.getPort(), 5432L))
                            .append('/')
                            .append(authentication.getDatabaseName());
                }
                url = urlBuilder.toString();

            }

            try {
                Connection connection = DriverManager.getConnection(url, properties);
                connection.setReadOnly(ReadOnly.equals(datasourceConfiguration.getConnection().getMode()));
                return connection;

            } catch (SQLException e) {
                return pluginErrorMono("Error connecting to Postgres.");

            }
        }

        @Override
        public void datasourceDestroy(Object connection) {
            Connection conn = (Connection) connection;
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("Error closing Postgres Connection.", e);
            }
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public Boolean isDatasourceValid(@NonNull DatasourceConfiguration datasourceConfiguration) {

            if (CollectionUtils.isEmpty(datasourceConfiguration.getEndpoints())) {
                return false;
            }

            if (datasourceConfiguration.getAuthentication() == null
                    || datasourceConfiguration.getAuthentication().getUsername() == null
                    || datasourceConfiguration.getAuthentication().getPassword() == null) {
                return false;
            }

            return true;
        }

    }

}
