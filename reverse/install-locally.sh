#!/bin/bash
version="0.1"
artifactId="reverse"
file="target/${artifactId}-${version}.jar"
groupId="com.olexyn.misp.reverse"




mvn package
mvn install:install-file -Dfile=${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=jar -DgeneratePom=true
