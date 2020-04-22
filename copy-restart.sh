#!/bin/bash

cwd=$(pwd)

/home/user/app/tomcat/bin/shutdown.sh

echo "================"
echo "END TOMCAT STOP "
echo "================"

cp -v "${cwd}/mirror/war/mirror.war" /home/user/app/tomcat/webapps
cp -v "${cwd}/mispbridge/war/mispbridge.war" /home/user/app/tomcat/webapps
cp -v "${cwd}/mispclient/war/mispclient.war" /home/user/app/tomcat/webapps

echo "================"
echo "END COPY"
echo "================"

/home/user/app/tomcat/bin/startup.sh

echo "================"
echo "END TOMCAT START "
echo "================"