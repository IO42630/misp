#!/bin/bash

echo "================"
echo "START RUN"
echo "================"
cwd=$(pwd)


# compile and distribute the misphelper.jar
cd "${cwd}/out/production/misphelper" || exit

jar -cvf misphelper.jar ./com/olexyn/misp/helper/*



echo "================"
echo "END MispHelper JAR"
echo "================"




a='misp'
b='bridge'


n="${a}${b}"
pkg="/com/olexyn/${a}/${b}"
out="/out/production/${n}${pkg}"
wrapper="/${n}/war/wrapper"

# copy misphelper.jar to wrapper/.../lib
cp -v"${cwd}/out/production/misphelper/misphelper.jar" "${cwd}${wrapper}/WEB-INF/lib"

# copy compiled code into the wrapper.
cp -vr "${cwd}${out}" "${cwd}${wrapper}/WEB-INF/classes/com/olexyn/${a}"

# compress .war
cd "${cwd}${wrapper}" || exit
jar -cvf "../${n}.war" *


echo "================"
echo "END MispBridge WAR"
echo "================"


a='misp'
b='client'


n="${a}${b}"
pkg="/com/olexyn/${a}/${b}"
out="/out/production/${n}${pkg}"
wrapper="/${n}/war/wrapper"

# copy misphelper.jar to wrapper/.../lib
cp -v "${cwd}/out/production/misphelper/misphelper.jar" "${cwd}${wrapper}/WEB-INF/lib"

# copy compiled code into the wrapper.
cp -vr "${cwd}${out}" "${cwd}${wrapper}/WEB-INF/classes/com/olexyn/${a}"

# compress .war
cd "${cwd}${wrapper}" || exit
jar -cvf "../${n}.war" *


echo "================"
echo "END MispClient WAR"
echo "================"



a='mirror'

pkg="/com/olexyn/${a}"
out="/out/production/${a}${pkg}"
wrapper="/${a}/war/wrapper"

# copy misphelper.jar to wrapper/.../lib
cp -v "${cwd}/out/production/misphelper/misphelper.jar" "${cwd}${wrapper}/WEB-INF/lib"

# copy compiled code into the wrapper.
cp -vr "${cwd}${out}" "${cwd}${wrapper}/WEB-INF/classes/com/olexyn"

# compress .war
cd "${cwd}${wrapper}" || exit
jar -cvf "../${a}.war" *

echo "================"
echo "END Mirror WAR"
echo "================"