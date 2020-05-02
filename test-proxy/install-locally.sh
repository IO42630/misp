#!/bin/bash
version="0.1"
file="target/test-proxy-${version}.war"
groupId="com.olexyn.test.proxy"
artifactId="test-proxy"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=war -DgeneratePom=true
