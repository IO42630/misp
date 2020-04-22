#!/bin/bash
cwd=$(pwd)


mispbridge_out='/out/production/mispbridge/com/olexyn/misp/bridge'
mispbridge_wrapper='/mispbridge/war/wrapper';

# copy compiled code into the wrapper.
cp -r "${cwd}${mispbridge_out}" "${cwd}${mispbridge_wrapper}/WEB-INF/classes"

# compress .war
cd "${cwd}${mispbridge_wrapper}" || exit
jar -cvf ../mispbridge.war *


mispclient_out='/out/production/mispclient/com/olexyn/misp/client'
mispclient_wrapper='/mispclient/war/wrapper';

# copy compiled code into the wrapper.
cp -r "${cwd}${mispclient_out}" "${cwd}${mispclient_wrapper}/WEB-INF/classes"

# compress .war
cd "${cwd}${mispclient_wrapper}" || exit
jar -cvf ../mispclient.war *


mirror_out='/out/production/mirror/com/olexyn/mirror'
mirror_wrapper='/mirror/war/wrapper';

# copy compiled code into the wrapper.
cp -r "${cwd}${mirror_out}" "${cwd}${mirror_wrapper}/WEB-INF/classes"

# compress .war
cd "${cwd}${mirror_wrapper}" || exit
jar -cvf ../mirror.war *