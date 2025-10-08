package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.sharedutils.helpers.Helpers

internal data class PlayerExtractorCache(
    val playerUrl: String?,
    val cpnCode: String?,
    val signatureTimestamp: String?
) {
    override fun toString(): String {
        return Helpers.merge(FIELD_DELIM, playerUrl, cpnCode, signatureTimestamp)
    }

    companion object {
        private const val FIELD_DELIM = "%FIELD%"

        @JvmStatic
        fun fromString(data: String): PlayerExtractorCache {
            val split = Helpers.split(data, FIELD_DELIM)

            val playerUrl = Helpers.parseStr(split, 0)
            val cpnCode = Helpers.parseStr(split, 1)
            val signatureTimestamp = Helpers.parseStr(split, 2)

            return PlayerExtractorCache(playerUrl, cpnCode, signatureTimestamp)
        }
    }
}
