#!/bin/bash

mvn clean install &&
docker build -t io42630/reverse:0.1 .




