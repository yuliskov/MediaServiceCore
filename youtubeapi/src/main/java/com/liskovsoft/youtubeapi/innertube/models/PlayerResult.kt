package com.liskovsoft.youtubeapi.innertube.models

internal data class PlayerResult(
    val playabilityStatus: PlayabilityStatus?

) {
    data class PlayabilityStatus(
        val status: String?,
        val playableInEmbed: Boolean?
    )
}
