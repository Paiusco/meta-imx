FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " file://0001-test_dvr_play-changed-to-output-TS.patch \
"
# Enable static lib as test build needs it
export enable_static="yes"

do_compile () {
    # Build test to generate test_dvr_play
    cd ${S}
    oe_runmake -C lib
    oe_runmake -C test
}

do_install() {
    install -d ${D}${bindir}
    if [ -e ${S}/test/test_dvr_play ]; then
        cp ${S}/test/test_dvr_play ${D}${bindir}
    fi
}

