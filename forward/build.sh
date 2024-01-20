#!/bin/bash

mvn clean install &&
docker build -t io42630/forward:0.1 .





