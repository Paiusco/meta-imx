SUMMARY = "GStreamer 1.0 multimedia framework"
DESCRIPTION = "GStreamer is a multimedia framework for encoding and decoding video and sound. \
It supports a wide range of formats including mp3, ogg, avi, mpeg and quicktime."
HOMEPAGE = "http://gstreamer.freedesktop.org/"
BUGTRACKER = "https://bugzilla.gnome.org/enter_bug.cgi?product=Gstreamer"
SECTION = "multimedia"
LICENSE = "LGPLv2+"

DEPENDS = "glib-2.0 glib-2.0-native libcap libxml2 bison-native flex-native"

inherit meson pkgconfig gettext upstream-version-is-even gobject-introspection gtk-doc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

# Use i.MX fork of GST for customizations
GST1.0_SRC ?= "gitsm://source.codeaurora.org/external/imx/gstreamer.git;protocol=https"
SRCBRANCH = "imx-1.18.x"

SRC_URI = " \
    ${GST1.0_SRC};branch=${SRCBRANCH} \
    file://0001-gst-gstpluginloader.c-when-env-var-is-set-do-not-fal.patch \
    file://0003-meson-Add-valgrind-feature.patch \
"
SRCREV = "2f20fd10eaf8629b3e8c134424c38412c4d3bd86"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"

PACKAGECONFIG ??= "${@bb.utils.contains('PTEST_ENABLED', '1', 'tests', '', d)} \
                   check \
                   debug \
                   tools"

PACKAGECONFIG[debug] = "-Dgst_debug=true,-Dgst_debug=false"
PACKAGECONFIG[tracer-hooks] = "-Dtracer_hooks=true,-Dtracer_hooks=false"
PACKAGECONFIG[check] = "-Dcheck=enabled,-Dcheck=disabled"
#PACKAGECONFIG[tests] = "-Dtests=enabled -Dinstalled-tests=true,-Dtests=disabled -Dinstalled-tests=false"
PACKAGECONFIG[valgrind] = "-Dvalgrind=enabled,-Dvalgrind=disabled,valgrind,"
PACKAGECONFIG[unwind] = "-Dlibunwind=enabled,-Dlibunwind=disabled,libunwind"
PACKAGECONFIG[dw] = "-Dlibdw=enabled,-Dlibdw=disabled,elfutils"
PACKAGECONFIG[bash-completion] = "-Dbash-completion=enabled,-Dbash-completion=disabled,bash-completion"
PACKAGECONFIG[tools] = "-Dtools=enabled,-Dtools=disabled"
#PACKAGECONFIG[setcap] = "-Dsetcap=enabled,-Dsetcap=disabled,libcap libcap-native"

# TODO: put this in a gettext.bbclass patch
def gettext_oemeson(d):
    if d.getVar('USE_NLS') == 'no':
        return '-Dnls=disabled'
    # Remove the NLS bits if USE_NLS is no or INHIBIT_DEFAULT_DEPS is set
    if d.getVar('INHIBIT_DEFAULT_DEPS') and not oe.utils.inherits(d, 'cross-canadian'):
        return '-Dnls=disabled'
    return '-Dnls=enabled'

EXTRA_OEMESON += " \
    -Dexamples=disabled \
    -Ddbghelp=disabled \
	-Dgtk_doc=disabled \
	-Dtests=disabled \
    ${@gettext_oemeson(d)} \
"

GTKDOC_MESON_OPTION = "gtk_doc"
GTKDOC_MESON_ENABLE_FLAG = "enabled"
GTKDOC_MESON_DISABLE_FLAG = "disabled"

GIR_MESON_ENABLE_FLAG = "enabled"
GIR_MESON_DISABLE_FLAG = "disabled"

PACKAGES += "${PN}-bash-completion"

# Add the core element plugins to the main package
FILES_${PN} += "${libdir}/gstreamer-1.0/*.so"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.a ${libdir}/gstreamer-1.0/include"
FILES_${PN}-bash-completion += "${datadir}/bash-completion/completions/ ${datadir}/bash-completion/helpers/gst*"
FILES_${PN}-dbg += "${datadir}/gdb ${datadir}/gstreamer-1.0/gdb ${datadir}/glib-2.0/gdb"

CVE_PRODUCT = "gstreamer"

#require recipes-multimedia/gstreamer/gstreamer1.0-ptest.inc

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"
