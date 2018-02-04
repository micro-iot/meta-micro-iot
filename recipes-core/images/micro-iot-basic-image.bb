require recipes-core/images/core-image-minimal.bb

IMAGE_FSTYPES = "squashfs-xz jffs2"
# IMAGE_FEATURES += "read-only-rootfs"

EXTRA_IMAGECMD_jffs2 += "-x lzo -x zlib"
