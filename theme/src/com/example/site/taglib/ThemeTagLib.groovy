package com.example.site.taglib

import com.sysgears.grain.taglib.GrainTagLib

class ThemeTagLib {

    /**
     * Grain taglib reference.
     */
    private GrainTagLib taglib

    public ThemeTagLib(GrainTagLib taglib) {
        this.taglib = taglib
    }

    /**
     * Converts a date to XML date time format.
     *
     * @attr date the date to convert
     *
     * @return XML date time representation of the date, for instance 2013-12-31T12:49:00+07:00
     */
    static def xmlDateTime = { Date date ->
        def tz = String.format('%tz', date)
        String.format("%tFT%<tT${tz.substring(0, 3)}:${tz.substring(3)}", date)
    }

    /**
     * Generates html tag for an image.
     *
     * @attr location image location
     * @attr width (optional) image width
     * @attr height (optional) image height
     */
    //def img = { String location, Integer width = null, Integer height = null ->
    //    def widthStr = width ? " width=\"${width}\"" : ""
    //    def heightStr = height ? " height=\"${height}\"" : ""
    //
    //    "<img${widthStr}${heightStr} src=\"${taglib.r(location)}\" alt=\"image\">"
    //}
}
