include mesa-common.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/7.7/MesaLib-${PV}.tar.bz2 \
           ftp://ftp.freedesktop.org/pub/mesa/7.7/MesaDemos-${PV}.tar.bz2  \
           file://crossfix.patch;patch=1"

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PR = "r11"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

# Netbooks have DRI support so use mesa-dri by default
DEFAULT_PREFERENCE_netbook = "1"

PACKAGES =+ "${PN}-xprogs"
PACKAGES_DYNAMIC = "mesa-dri-driver-*"

FILES_${PN}-dbg += "${libdir}/dri/.debug/*"
FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"

LEAD_SONAME = "libGL.so.1"

EXTRA_OECONF += "--with-driver=dri --disable-egl --disable-gallium"

do_install_append () {
    install -d ${D}/usr/bin
    install -m 0755 ${S}/progs/xdemos/{glxdemo,glxgears,glxheads,glxinfo} ${D}/usr/bin/
}

python populate_packages_prepend() {
	import os.path

	dri_drivers_root = os.path.join(bb.data.getVar('libdir', d, 1), "dri")

	do_split_packages(d, dri_drivers_root, '^(.*)_dri\.so$', 'mesa-dri-driver-%s', 'Mesa %s DRI driver', extra_depends='')
}

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'


#
# Header generated by i586-poky-linux-gcc gen_matypes.c -o gen_matypes -I ../../../include/GL -I ../../../include -I .. -I ../main/ -I ../math -I ../glapi/ -I ../tnl
# then run gen_matypes > matypes.h on device
#