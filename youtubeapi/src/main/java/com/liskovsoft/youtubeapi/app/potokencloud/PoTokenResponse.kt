package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.sharedutils.helpers.Helpers

private const val DELIM: String = "%pot%"

internal data class PoTokenResponse(
    var visitorData: String?,
    val placeholderPoToken: String?,
    val poToken: String?,
    val mintRefreshDate: String?,
    val timestamp: Long = System.currentTimeMillis()
) {
    override fun toString(): String {
        return Helpers.merge(DELIM, visitorData, placeholderPoToken, poToken, mintRefreshDate, timestamp)
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): PoTokenResponse? {
            if (spec == null)
                return null

            val split = Helpers.split(spec, DELIM)

            val visitorData = Helpers.parseStr(split, 0)
            val placeholderPoToken = Helpers.parseStr(split, 1)
            val poToken = Helpers.parseStr(split, 2)
            val mintRefreshDate = Helpers.parseStr(split, 3)
            val timestamp = Helpers.parseLong(split, 4)

            return PoTokenResponse(visitorData, placeholderPoToken, poToken, mintRefreshDate, timestamp)
        }
    }
}

internal data class PoTokenPart1Response(
    val requestKey: String?,
    val botguardResponse: String?
)
