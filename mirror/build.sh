#!/bin/bash

mvn clean install &&
docker build -t io42630/mirror:0.1 .





