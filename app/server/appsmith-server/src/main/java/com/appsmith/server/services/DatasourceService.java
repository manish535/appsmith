package com.appsmith.server.services;

import com.appsmith.external.models.DatasourceTestResult;
import com.appsmith.server.acl.AclPermission;
import com.appsmith.server.domains.Datasource;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface DatasourceService extends CrudService<Datasource, String> {

    Mono<DatasourceTestResult> testDatasource(Datasource datasource);

    Mono<Datasource> findByName(String name);

    Mono<Datasource> findById(String id, AclPermission aclPermission);

    Set<String> extractKeysFromDatasource(Datasource datasource);

    Mono<Datasource> validateDatasource(Datasource datasource);

}
