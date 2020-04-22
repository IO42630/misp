#!/bin/bash
cp ./mispbridge.war /home/user/app/tomcat/webapps
cp ./mispclient.war /home/user/app/tomcat/webapps
/home/user/app/tomcat/bin/shutdown.sh
/home/user/app/tomcat/bin/startup.sh