DESCRIPTION = "Basic utility library"
HOMEPAGE = "http://wiki.openwrt.org/doc/uci"
LICENSE = "ISC BSD-3c"
LIC_FILES_CHKSUM = "file://uloop.c;beginline=4;endline=17;md5=ae045d075c7d6dde096232b6130a5202"

SRC_URI = "git://git.openwrt.org/project/libubox.git;protocol=git;branch=master"
SRC_URI += "file://luajit.patch"
		   
SRCREV = "b8d9b382e39823850331edc2a92379173daf1be3"
S = "${WORKDIR}/git"

inherit cmake

PR="r1"

DEPENDS = "json-c"
# RDEPENDS_${PN} = ""
# EXTRA_OECMAKE = "-DLUAPATH=/usr/lib/lua/5.1 -DBUILD_LUA=OFF"
EXTRA_OECMAKE = "-DBUILD_LUA=OFF"

FILES_${PN} += "/lib/ /usr/share"
FILES_${PN}-dbg += ""
FILES_${PN}-dev = "/usr/lib/*.so  /usr/include"

do_install_append () {
	mkdir ${D}/lib

	mv ${D}/usr/lib/libubox.so ${D}/usr/lib/libubox.so.1.0.0

	ln -rs ${D}/usr/lib/libubox.so.1.0.0 ${D}/usr/lib/libubox.so.1
	ln -rs ${D}/usr/lib/libubox.so.1.0.0 ${D}/usr/lib/libubox.so

	install ${D}/usr/lib/libubox.so.1.0.0 ${D}/lib
	install ${D}/usr/lib/libubox.so.1 ${D}/lib
	install ${D}/usr/lib/libubox.so ${D}/lib

	mv ${D}/usr/lib/libblobmsg_json.so ${D}/usr/lib/libblobmsg_json.so.1.0.0

	ln -rs ${D}/usr/lib/libblobmsg_json.so.1.0.0 ${D}/usr/lib/libblobmsg_json.so.1
	ln -rs ${D}/usr/lib/libblobmsg_json.so.1.0.0 ${D}/usr/lib/libblobmsg_json.so

	install ${D}/usr/lib/libblobmsg_json.so.1.0.0 ${D}/lib
	install ${D}/usr/lib/libblobmsg_json.so.1 ${D}/lib
	install ${D}/usr/lib/libblobmsg_json.so ${D}/lib

	mv ${D}/usr/lib/libjson_script.so ${D}/usr/lib/libjson_script.so.1.0.0

	ln -rs ${D}/usr/lib/libjson_script.so.1.0.0 ${D}/usr/lib/libjson_script.so.1
	ln -rs ${D}/usr/lib/libjson_script.so.1.0.0 ${D}/usr/lib/libjson_script.so
	
	install ${D}/usr/lib/libjson_script.so.1.0.0 ${D}/lib
	install ${D}/usr/lib/libjson_script.so.1 ${D}/lib
	install ${D}/usr/lib/libjson_script.so ${D}/lib
}