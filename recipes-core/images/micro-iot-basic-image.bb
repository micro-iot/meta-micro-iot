require recipes-core/images/core-image-minimal.bb

DEPENDS = "linux-yocto-linkit7688-4.14.95 padjffs2-native init-ifupdown"
RDEPENDS_${PN} += "init-ifupdown"

IMAGE_FSTYPES = "squashfs-xz tar.xz multiubi ubi ubifs"

# EXTRA_IMAGECMD_jffs2_remove = "--pad"
EXTRA_IMAGECMD_jffs2 += "-x lzo -x zlib"

IMAGE_INSTALL += "mtd-utils"
IMAGE_INSTALL += "mtd-utils-ubifs"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "swconfig"
IMAGE_INSTALL += "init-ifupdown"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "packagegroup-core-ssh-dropbear"

UBINIZE_ARGS = " -m 1 -p 0x10000 -O 4096 "
MKUBIFS_ARGS = " -m 1 -e 61376 -c 513"

do_prepare_sysupgrade_ubi_squashfs() {
    cd ${DEPLOY_DIR_IMAGE}
    ubinize -o base-image-ubi.img -m 1 -p 0x10000 -O 4096 lks7688_ubinize.cfg
    dd if=${DEPLOY_DIR_IMAGE}/uImage >  ${DEPLOY_DIR_IMAGE}/lks7688.img
    dd if=${DEPLOY_DIR_IMAGE}/base-image-ubi.img >>  ${DEPLOY_DIR_IMAGE}/lks7688.img
}

do_prepare_sysupgrade_ubi_ubifs() {
    cd ${DEPLOY_DIR_IMAGE}
    dd if=${DEPLOY_DIR_IMAGE}/uImage >  ${DEPLOY_DIR_IMAGE}/lks7688_ubifs_.img
    padjffs2 ${DEPLOY_DIR_IMAGE}/lks7688_ubifs_.img 64
    dd if=${DEPLOY_DIR_IMAGE}/lks7688_ubifs_.img of=${DEPLOY_DIR_IMAGE}/lks7688_ubifs.img bs=1 count=$(expr $(stat -c %s ${DEPLOY_DIR_IMAGE}/lks7688_ubifs_.img) - 4 )
    rm ${DEPLOY_DIR_IMAGE}/lks7688_ubifs_.img
    dd if=${DEPLOY_DIR_IMAGE}/micro-iot-basic-image-linkit7688.ubi >>  ${DEPLOY_DIR_IMAGE}/lks7688_ubifs.img
}


# Call function prepare_sysupgrade to generate images
addtask prepare_sysupgrade_ubi_squashfs after do_image_complete before do_populate_lic_deploy
addtask prepare_sysupgrade_ubi_ubifs after do_image_complete before do_populate_lic_deploy