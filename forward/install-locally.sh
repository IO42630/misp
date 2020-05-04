#!/bin/bash
version="0.1"
file="target/forward-${version}.war"
groupId="com.olexyn.misp.forward"
artifactId="forward"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=war -DgeneratePom=true
