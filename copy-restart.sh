#!/bin/bash

cwd=$(pwd)

cp "${cwd}/mirror/war/mirror.war" /home/user/app/tomcat/webapps
cp "${cwd}/mispbridge/war/mispbridge.war" /home/user/app/tomcat/webapps
cp "${cwd}/mispclient/war/mispclient.war" /home/user/app/tomcat/webapps

/home/user/app/tomcat/bin/shutdown.sh
/home/user/app/tomcat/bin/startup.sh