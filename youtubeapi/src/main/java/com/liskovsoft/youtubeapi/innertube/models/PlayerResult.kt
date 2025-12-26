package com.liskovsoft.youtubeapi.innertube.models

internal data class PlayerResult(
    val playabilityStatus: PlayabilityStatus?,
    val streamingData: StreamingData?
) {
    data class PlayabilityStatus(
        val status: String?,
        val playableInEmbed: Boolean?
    )
}

internal data class StreamingData(
    val expiresInSeconds: String?,
    val formats: List<LegacyFormat>?,
    val adaptiveFormats: List<AdaptiveFormat>?,
    val serverAbrStreamingUrl: String?
)

internal data class LegacyFormat(
    val itag: Int?,
    val mimeType: String?,
    val bitrate: Int?,
    val width: Int?,
    val height: Int?,
    val lastModified: String?,
    val quality: String?,
    val fps: Int?,
    val qualityLabel: String?,
    val projectionType: String?,
    val audioQuality: String?,
    val approxDurationMs: String?,
    val audioSampleRate: String?,
    val audioChannels: Int?,
    val signatureCipher: String?,
    val qualityOrdinal: String?
)

internal data class AdaptiveFormat(
    val itag: Int?,
    val mimeType: String?,
    val bitrate: Int?,
    val width: Int?,
    val height: Int?,
    val initRange: InitRange?,
    val indexRange: IndexRange?,
    val lastModified: String?,
    val contentLength: String?,
    val quality: String?,
    val fps: Int?,
    val qualityLabel: String?,
    val projectionType: String?,
    val averageBitrate: Int?,
    val approxDurationMs: String?,
    val qualityOrdinal: String?,
    // audio
    val highReplication: Boolean?,
    val audioQuality: String?,
    val audioSampleRate: String?,
    val audioChannels: Int?,
    val loudnessDb: Float?,
    val trackAbsoluteLoudnessLkfs: Float?,
) {
    data class InitRange(
        val start: String?,
        val end: String?
    )
    data class IndexRange(
        val start: String?,
        val end: String?
    )
}
