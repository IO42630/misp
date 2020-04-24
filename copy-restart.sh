#!/bin/bash

cwd=$(pwd)

/home/user/app/tomcat/bin/shutdown.sh

echo "================"
echo "END TOMCAT STOP "
echo "================"

#cp -v "${cwd}/mirror/war/mirror.war" /home/user/app/tomcat/webapps
cp -v "${cwd}/misp-rev/target/misp-rev-0.1.war" /home/user/app/tomcat/webapps

echo "================"
echo "END COPY"
echo "================"

/home/user/app/tomcat/bin/startup.sh

echo "================"
echo "END TOMCAT START "
echo "================"