#!/bin/bash
cwd=$(pwd)


# compile and distribute the misphelper.jar
cd "${cwd}/out/production/misphelper" || exit

jar -cvf misphelper.jar ./com/olexyn/misp/helper/*








a='misp'
b='bridge'


n="${a}${b}"
pkg="/com/olexyn/${a}/${b}"
out="/out/production/${n}${pkg}"
wrapper="/${n}/war/wrapper"

# copy misphelper.jar to wrapper/.../lib
cp "${cwd}/out/production/misphelper/misphelper.jar" "${cwd}${wrapper}/WEB-INF/lib"

# copy compiled code into the wrapper.
cp -r "${cwd}${out}" "${cwd}${wrapper}/WEB-INF/classes/com/olexyn/${a}"

# compress .war
cd "${cwd}${wrapper}" || exit
jar -cvf "../${n}.war" *


a='misp'
b='client'


n="${a}${b}"
pkg="/com/olexyn/${a}/${b}"
out="/out/production/${n}${pkg}"
wrapper="/${n}/war/wrapper"

# copy misphelper.jar to wrapper/.../lib
cp "${cwd}/out/production/misphelper/misphelper.jar" "${cwd}${wrapper}/WEB-INF/lib"

# copy compiled code into the wrapper.
cp -r "${cwd}${out}" "${cwd}${wrapper}/WEB-INF/classes/com/olexyn/${a}"

# compress .war
cd "${cwd}${wrapper}" || exit
jar -cvf "../${n}.war" *



a='mirror'

pkg="/com/olexyn/${a}"
out="/out/production/${a}${pkg}"
wrapper="/${a}/war/wrapper"

# copy misphelper.jar to wrapper/.../lib
cp "${cwd}/out/production/misphelper/misphelper.jar" "${cwd}${wrapper}/WEB-INF/lib"

# copy compiled code into the wrapper.
cp -r "${cwd}${out}" "${cwd}${wrapper}/WEB-INF/classes/com/olexyn"

# compress .war
cd "${cwd}${wrapper}" || exit
jar -cvf "../${a}.war" *