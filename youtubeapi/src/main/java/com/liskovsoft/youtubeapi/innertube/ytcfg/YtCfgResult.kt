package com.liskovsoft.youtubeapi.innertube.ytcfg

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp

internal class YtCfgResult {
    @RegExp("ytcfg\\.set\\(\\s*(\\{[\\s\\S]*?\\})\\s*\\);")
    var ytCfg: String? = null
}