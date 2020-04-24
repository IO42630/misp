#!/bin/bash


tomcat_webapps="${HOME}/app/tomcat/webapps"
cwd=$(pwd)

/home/user/app/tomcat/bin/shutdown.sh

echo "================"
echo "END TOMCAT STOP "
echo "================"

cp -v "${cwd}/misp-mirror/target/misp-mirror-0.1.war" "${tomcat_webapps}"
cp -v "${cwd}/misp-rev/target/misp-rev-0.1.war" "${tomcat_webapps}"

echo "================"
echo "END COPY"
echo "================"

/home/user/app/tomcat/bin/startup.sh

echo "================"
echo "END TOMCAT START "
echo "================"