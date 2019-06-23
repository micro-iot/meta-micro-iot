INSANE_SKIP_${PN} += "dev-so installed-vs-shipped file-rdeps dep-cmp build-deps"

DESCRIPTION = "Generalized Wireless Information Library (iwinfo)"
HOMEPAGE = "http://wiki.openwrt.org/doc/howto/wireless.utilities"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/piotrbetlej/iwinfo.git;branch=master;"
		   
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

PR="r1"

DEPENDS = "libnl libubox"
RDEPENDS_${PN} = "libubox libnl-genl"

FILES_${PN} = "/usr/bin/*"

EXTRA_OEMAKE = "IWINFO_BACKENDS=nl80211"

do_compile () {
	oe_runmake 'FPIC=-fPIC -I${B}/../recipe-sysroot/usr/include/libnl3 -I${B}/../recipe-sysroot/usr/include' compile
}

do_install () {
	mkdir -p ${D}/${libdir} ${D}/usr/bin
	install ${B}/iwinfo ${D}/usr/bin
}