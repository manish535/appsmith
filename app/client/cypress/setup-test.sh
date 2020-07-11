#! /bin/sh

# This script is responsible for setting up the local Nginx server for running E2E Cypress tests 
# on our CI/CD system. Currently the script is geared towards Github Actions

# Serve the react bundle on a specific port. Nginx will proxy to this port
echo "Starting the setup the test framework"
echo "127.0.0.1	dev.appsmith.com" >> /etc/hosts
serve -s react-build -p 3000 &
mkdir -p /var/www/appsmith /etc/certificate

# Substitute all the env variables in nginx
vars_to_substitute=$(printf '\$%s,' $(env | grep -o "^APPSMITH_[A-Z0-9_]\+" | xargs))
cat ./docker/templates/nginx-linux.conf.template | envsubst ${vars_to_substitute} | sed -e 's|\${\(APPSMITH_[A-Z0-9_]*\)}||g' > ./docker/nginx.conf
cp ./docker/nginx.conf /etc/nginx/conf.d/app.conf

# Create the SSL files for Nginx. Required for service workers to work properly.
touch /etc/certificate/dev.appsmith.com.pem /etc/certificate/dev.appsmith.com-key.pem
echo "$APPSMITH_SSL_CERTIFICATE" > /etc/certificate/dev.appsmith.com.pem
echo "$APPSMITH_SSL_KEY" > /etc/certificate/dev.appsmith.com-key.pem
echo "Going to run the nginx server"
nginx
sleep 5
echo "Checking if server is up"
curl -v -k "https://dev.appsmith.com"