#!/bin/bash
cwd=$(pwd)


cp -r ./production/mispbridge/core/ mispbridge-war-wrapper/WEB-INF/classes





cd warbridge


jar -cvf ../mispbridge.war *

cd $cwd

cp -r ./production/mispclient/core/ mispclient-war-wrapper/WEB-INF/classes

cd warclient
jar -cvf ../mispclient.war *