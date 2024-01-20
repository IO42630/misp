#!/bin/bash

cd ./helper &&
mvn clean install &&

cd ../mirror &&
./build.sh &&

cd ../reverse &&
./build.sh &&

cd ../forward &&
./build.sh &






