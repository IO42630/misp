#!/bin/bash
version="0.1"
file="target/helper-${version}.jar"
groupId="com.olexyn.misp.helper"
artifactId="helper"



mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=jar -DgeneratePom=true
