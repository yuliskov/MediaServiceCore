package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.sharedutils.helpers.Helpers

private const val DELIM: String = "%pot%"

internal data class PoTokenResponse(
    val visitorData: String?,
    val placeholderPoToken: String?,
    val poToken: String?,
    val mintRefreshDate: String?
) {
    override fun toString(): String {
        return Helpers.merge(DELIM, visitorData, placeholderPoToken, poToken, mintRefreshDate)
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): PoTokenResponse? {
            if (spec == null)
                return null

            val split = Helpers.split(DELIM, spec)

            return PoTokenResponse(split.getOrNull(0), split.getOrNull(1), split.getOrNull(2), split.getOrNull(3))
        }
    }
}

internal data class PoTokenPart1Response(
    val requestKey: String?,
    val botguardResponse: String?
)
