require recipes-core/images/core-image-minimal.bb

DEPENDS = "linux-yocto-linkit7688-4.14.95 padjffs2-native init-ifupdown"
RDEPENDS_${PN} += "init-ifupdown"

IMAGE_FSTYPES = "squashfs-xz jffs2"
# IMAGE_FEATURES += "read-only-rootfs"

# EXTRA_IMAGECMD_jffs2_remove = "--pad"
EXTRA_IMAGECMD_jffs2 += "-x lzo -x zlib"

IMAGE_INSTALL += "mtd-utils"
IMAGE_INSTALL += "mtd-utils-ubifs"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "swconfig"
IMAGE_INSTALL += "init-ifupdown"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "packagegroup-core-ssh-dropbear"

do_prepare_sysupgrade_ubi_squashfs() {
    cd ${DEPLOY_DIR_IMAGE}
    ubinize -o base-image-ubi.img -m 1 -p 0x10000 -O 4096 lks7688_ubinize.cfg
    dd if=${DEPLOY_DIR_IMAGE}/uImage >  ${DEPLOY_DIR_IMAGE}/lks7688.img
    dd if=${DEPLOY_DIR_IMAGE}/base-image-ubi.img >>  ${DEPLOY_DIR_IMAGE}/lks7688.img
}

do_prepare_sysupgrade_jffs2() {
    cat ${DEPLOY_DIR_IMAGE}/uImage > ${DEPLOY_DIR_IMAGE}/sys
    padjffs2 ${DEPLOY_DIR_IMAGE}/sys 64
    dd if=${DEPLOY_DIR_IMAGE}/sys of=${DEPLOY_DIR_IMAGE}/_sys bs=1 count=$(expr $(stat -c %s ${DEPLOY_DIR_IMAGE}/sys) - 4 )
    cat ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}-${MACHINE}.jffs2 >> ${DEPLOY_DIR_IMAGE}/_sys
    padjffs2 ${DEPLOY_DIR_IMAGE}/_sys 64
    dd if=${DEPLOY_DIR_IMAGE}/_sys of=${DEPLOY_DIR_IMAGE}/__sys bs=1 count=$(expr $(stat -c %s ${DEPLOY_DIR_IMAGE}/_sys) - 4 )
    mv ${DEPLOY_DIR_IMAGE}/__sys ${DEPLOY_DIR_IMAGE}/sys
    rm ${DEPLOY_DIR_IMAGE}/_sys
}


# Call function prepare_sysupgrade to generate images
addtask prepare_sysupgrade_ubi_squashfs after do_image_complete before do_populate_lic_deploy
addtask do_prepare_sysupgrade_jffs2 after do_image_complete before do_populate_lic_deploy