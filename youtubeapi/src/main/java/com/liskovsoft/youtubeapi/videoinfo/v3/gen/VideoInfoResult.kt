package com.liskovsoft.youtubeapi.videoinfo.v3.gen

internal data class VideoInfoResult(
    val streamingData: StreamingData?
) {
    data class StreamingData(
        val hlsManifestUrl: String?
    )
}
