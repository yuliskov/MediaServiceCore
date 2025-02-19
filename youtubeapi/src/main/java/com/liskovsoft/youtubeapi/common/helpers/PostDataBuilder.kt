package com.liskovsoft.youtubeapi.common.helpers

private const val JSON_POST_DATA_BASE = "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
        "\"clientScreen\":\"%s\",\"userAgent\":\"%s\",%s\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
        "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},%%s\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}}," +
        "\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"
// Include Shorts: "browserName":"Cobalt"
private const val POST_DATA_BROWSE =
    "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_FULL_ANIMATION\",\"zylonLeftNav\":true},\"browserName\":\"Cobalt\",\"webpSupport\":false,\"animatedWebpSupport\":true,"

internal class PostDataBuilder(val client: AppClient) {
    private var acceptLanguage: String? = null
    private var acceptRegion: String? = null
    private var utcOffsetMinutes: Int? = null
    private var visitorData: String? = null
    private var cpn: String? = null
    private var videoId: String? = null
    private var clickTrackingParams: String? = null
    private var poToken: String? = null
    private var signatureTimestamp: Int? = null

    fun setLanguage(lang: String?) = apply { acceptLanguage = lang }
    fun setCountry(country: String?) = apply { acceptRegion = country }
    fun setUtcOffsetMinutes(offset: Int?) = apply { utcOffsetMinutes = offset }
    fun setVideoId(videoId: String?) = apply { this.videoId = videoId }
    fun setPoToken(poToken: String?) = apply { this.poToken = poToken }
    fun setClientPlaybackNonce(cpn: String?) = apply { this.cpn = cpn }
    fun setSignatureTimestamp(timestamp: Int?) = apply { signatureTimestamp = timestamp }
    fun setClickTrackingParams(params: String?) = apply { clickTrackingParams = params }
    fun setVisitorData(visitorData: String?) = apply { this.visitorData = visitorData }

    fun build(): String {
        return """
             {
                "context": {
                     ${createClientChunk()},
                     ${createClickTrackingChunk()?.let { "$it," } ?: ""}
                     ${createUserChunk()}
                },
                "racyCheckOk": true,
                "contentCheckOk": true,
                ${createCheckParamsChunk()?.let { "$it," } ?: ""}
                ${createPotChunk()?.let { "$it," } ?: ""}
                ${createVideoIdChunk()?.let { "$it," } ?: ""}
             }
        """.trimIndent()
    }

    private fun createClientChunk(): String {
        val clientVars = """
            "clientName": "${client.clientName}",
            "clientVersion": "${client.clientVersion}",
            "clientScreen": "${client.clientScreen}",
            "userAgent": "${client.userAgent}",
        """.trimIndent()
        val postVars = client.postData
        val browseVars = """
            "tvAppInfo": { 
                "appQuality": "TV_APP_QUALITY_FULL_ANIMATION",
                "zylonLeftNav": true
            },
            "browserName": "Cobalt",
            "webpSupport": false,
            "animatedWebpSupport": true,
        """.trimIndent() // Include Shorts: "browserName":"Cobalt"
        val regionVars = """
            "acceptLanguage": "${requireNotNull(acceptLanguage)}",
            "acceptRegion": "${requireNotNull(acceptRegion)}",
            "utcOffsetMinutes": "${requireNotNull(utcOffsetMinutes)}",
        """.trimIndent()
        val visitorVar = visitorData?.let { """ "visitorData": "$visitorData" """ }
        return """
             "client": {
                ${ServiceHelper.combineText(clientVars, postVars, browseVars, regionVars, visitorVar)}
             }
        """.trimIndent()
    }

    private fun createClickTrackingChunk(): String? {
        return clickTrackingParams?.let {
            """
                "clickTracking": {
                    "clickTrackingParams": "$it"
                }
            """.trimIndent()
        }
    }

    private fun createUserChunk(): String {
        return """
           "user":{
                "enableSafetyMode": false,
                "lockedSafetyMode":false
           } 
        """.trimIndent()
    }

    private fun createPotChunk(): String? {
        return poToken?.let {
            """
               "serviceIntegrityDimensions": {
                    "poToken": "$it"
               } 
            """.trimIndent()
        }
    }

    private fun createVideoIdChunk(): String? {
        return videoId?.let {
            """
                "videoId": "$it",
                "cpn": "${requireNotNull(cpn)}"
            """.trimIndent()
        }
    }

    private fun createCheckParamsChunk(): String? {
        return signatureTimestamp?.let {
            """
                "playbackContext": {
                    "contentPlaybackContext": {
                        "html5Preference": "HTML5_PREF_WANTS",
                        "lactMilliseconds": 60000,
                        "signatureTimestamp": $it
                    }
                }
            """.trimIndent()
        }
    }
}