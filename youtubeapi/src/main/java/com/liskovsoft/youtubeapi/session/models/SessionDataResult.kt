package com.liskovsoft.youtubeapi.session.models

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPathObj

internal class SessionDataResult {
    @JsonPath("$[0][2]")
    var ytcfg: YtCfg? = null
        private set
}

internal class YtCfg: JsonPathObj() {
    @JsonPath("$[1]")
    var apiKey: String? = null
        private set

    @JsonPath("$[0][0]")
    var deviceInfo: DeviceInfo? = null
        private set
}

internal class DeviceInfo : JsonPathObj() {
    @JsonPath("$[0]")
    var hl: String? = null
        private set

    @JsonPath("$[1]")
    var gl: String? = null
        private set

    @JsonPath("$[13]")
    var visitorData: String? = null
        private set

    @JsonPath("$[16]")
    var clientVersion: String? = null
        private set

    @JsonPath("$[17]")
    var osName: String? = null
        private set

    @JsonPath("$[18]")
    var osVersion: String? = null
        private set

    @JsonPath("$[79]")
    var timeZone: String? = null
        private set

    @JsonPath("$[86]")
    var browserName: String? = null
        private set

    @JsonPath("$[87]")
    var browserVersion: String? = null
        private set

    @JsonPath("$[11]")
    var deviceMake: String? = null
        private set

    @JsonPath("$[12]")
    var deviceModel: String? = null
        private set

    @JsonPath("$[103]")
    var deviceExperimentId: String? = null
        private set

    @JsonPath("$[107]")
    var rolloutToken: String? = null
        private set
}
