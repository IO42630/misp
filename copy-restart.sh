#!/bin/bash

tomcat_webapps="${HOME}/app/tomcat/webapps"
jetty_webapps="${HOME}/app/jetty9.4/webapps"
webapps=$jetty_webapps
cwd=$(pwd)

/home/user/app/tomcat/bin/shutdown.sh

echo "================"
echo "END TOMCAT STOP "
echo "================"

cp -v "${cwd}/misp-mirror/target/misp-mirror-0.1.war" "${webapps}"
cp -v "${cwd}/misp-rev/target/misp-rev-0.1.war" "${webapps}"
cp -v "${cwd}/test-proxy/target/test-proxy-0.1.war" "${webapps}"

echo "================"
echo "END COPY"
echo "================"

# /home/user/app/tomcat/bin/startup.sh

echo "================"
echo "END TOMCAT START "
echo "================"