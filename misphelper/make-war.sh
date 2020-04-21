#!/bin/bash
cd ../mispbridge/mispbridge
jar -cvf mispbridge.war *
cd ../
cd ../mispclient/mispclient
jar -cvf mispclient.war *