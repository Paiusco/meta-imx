# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017-2022 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Freescale Multimedia VPU wrapper"
LICENSE = "Proprietary"
SECTION = "multimedia"
LIC_FILES_CHKSUM = "file://COPYING;md5=17d2319de7baa686e8a755ba58a9ebf5"

DEPENDS = "virtual/imxvpu"
DEPENDS:append:mx8mp = " imx-vpu-hantro-vc"

IMX_VPUWRAP_SRC ?= "git://github.com/NXP/imx-vpuwrap.git;protocol=https"
SRC_URI = "${IMX_VPUWRAP_SRC};branch=${SRCBRANCH}"

SRCBRANCH = "MM_04.06.04_2112_L5.15.y"
SRCREV = "d4bfad3f8b6395c2fa642856048b363f8d372594" 

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_install:append() {
    # FIXME: Drop examples for now
    rm -r ${D}${datadir}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(imxvpu)"
