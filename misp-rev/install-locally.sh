#!/bin/bash
version="0.1"
file="target/misp-rev-${version}.war"
groupId="com.olexyn.misp.rev"
artifactId="misp-rev"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=war -DgeneratePom=true
