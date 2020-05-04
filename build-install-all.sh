#!/bin/bash
cd ./forward
./install-locally.sh
cd ..
cd ./helper
./install-locally.sh
cd ..
cd ./mirror
./install-locally.sh
cd ..
cd ./reverse
./install-locally.sh
cd ..