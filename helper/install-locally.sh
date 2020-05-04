#!/bin/bash
version="0.1"
file="target/misp-helper-${version}.jar"
groupId="com.olexyn.misp.helper"
artifactId="misp-helper"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=jar -DgeneratePom=true
