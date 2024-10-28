package com.liskovsoft.youtubeapi.potokencloud

internal data class PoTokenResponse(
    val visitorData: String?,
    val placeholderPoToken: String?,
    val poToken: String?,
    val mintRefreshDate: String?
) {
    override fun toString(): String {
        return super.toString()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String): PoTokenResponse {
            return PoTokenResponse(null, null, null, null)
        }
    }
}
