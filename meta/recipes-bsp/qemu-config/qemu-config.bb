DESCRIPTION = "Adds scripts to use distcc on the host system under qemu"

LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

RDEPENDS = "distcc task-poky-nfs-server oprofileui-server rsync bash"
PR = "r18"

SRC_URI = "file://distcc.sh \
           file://anjuta-remote-run \
           file://exports \
           file://shutdown.desktop \
           file://qemu-autostart \
           file://COPYING.GPL"

S = "${WORKDIR}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86)"
PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}${sysconfdir}/profile.d

    install -m 0755 distcc.sh ${D}${sysconfdir}/profile.d/
    install -m 0644 exports ${D}${sysconfdir}/
    
    install -d ${D}${bindir}
    install -m 0755 anjuta-remote-run ${D}${bindir}/
    
    install -d ${D}${datadir}/applications
    install -m 0644 shutdown.desktop ${D}${datadir}/applications/

    install -d ${D}/etc/init.d
    install qemu-autostart ${D}/etc/init.d/
}

inherit update-rc.d

INITSCRIPT_NAME = "qemu-autostart"
INITSCRIPT_PARAMS = "start 999 5 2 . stop 20 0 1 6 ."
