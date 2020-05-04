#!/bin/bash
version="0.1"
file="target/misp-mirror-${version}.war"
groupId="com.olexyn.misp.mirror"
artifactId="misp-mirror"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=war -DgeneratePom=true
