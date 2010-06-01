require libtool.inc

PR = "r21"

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}a.tar.gz \
           file://trailingslash.patch;patch=1 \
           file://dolt.m4"

PACKAGES =+ "libltdl libltdl-dev libltdl-dbg"
FILES_${PN} += "${datadir}/aclocal*"
FILES_libltdl = "${libdir}/libltdl.so.*"
FILES_libltdl-dev = "${libdir}/libltdl.* ${includedir}/ltdl.h"
FILES_libltdl-dbg = "${libdir}/.debug/"

inherit autotools

EXTRA_AUTORECONF = "--exclude=libtoolize"

do_compile_prepend () {
	# Sometimes this file doesn't get rebuilt, force the issue
	rm -f ${S}/libltdl/config/ltmain.sh
	make libltdl/config/ltmain.sh
}

#
# We want the results of libtool-cross preserved - don't stage anything ourselves.
#
SYSROOT_PREPROCESS_FUNCS += "libtool_sysroot_preprocess"

libtool_sysroot_preprocess () {
	if [ "${PN}" == "libtool" ]; then
		rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${bindir}/*
		rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${datadir}/aclocal/*
		rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${datadir}/libtool/config/*
	fi
}
