#!/bin/bash
version="0.1"
file="target/misp-fwd-${version}.war"
groupId="com.olexyn.misp.fwd"
artifactId="misp-fwd"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=war -DgeneratePom=true
