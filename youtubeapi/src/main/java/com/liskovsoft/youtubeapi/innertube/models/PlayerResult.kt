package com.liskovsoft.youtubeapi.innertube.models

import com.liskovsoft.youtubeapi.common.models.gen.TextItem
import com.liskovsoft.youtubeapi.common.models.gen.ThumbnailItem

internal data class PlayerResult(
    val playabilityStatus: PlayabilityStatus?,
    val streamingData: StreamingData?,
    val playerConfig: PlayerConfig?,
    val captions: Captions?,
    val videoDetails: VideoDetails?,
    val storyboards: Storyboards?,
    val microformat: Microformat?,
    val paidContentOverlay: PaidContentOverlay?,
    val playbackTracking: PlaybackTracking?
) {
    data class PlayabilityStatus(
        val status: String?,
        val reason: String?,
        val playableInEmbed: Boolean?,
        val errorScreen: ErrorScreen?
    ) {
        data class ErrorScreen(
            val playerLegacyDesktopYpcTrailerRenderer: PlayerLegacyDesktopYpcTrailerRenderer?,
            val playerErrorMessageRenderer: PlayerErrorMessageRenderer?
        ) {
            data class PlayerLegacyDesktopYpcTrailerRenderer(
                val trailerVideoId: String?
            )
            data class PlayerErrorMessageRenderer(
                val subreason: TextItem?
            )
        }
    }
    data class PlayerConfig(
        val mediaCommonConfig: MediaCommonConfig?,
        val audioConfig: AudioConfig?
    ) {
        data class MediaCommonConfig(
            val mediaUstreamerRequestConfig: MediaUstreamerRequestConfig?
        ) {
            data class MediaUstreamerRequestConfig(
                val videoPlaybackUstreamerConfig: String?
            )
        }
        data class AudioConfig(
            val loudnessDb: Float?
        )
    }
    data class Captions(
        val playerCaptionsTracklistRenderer: PlayerCaptionsTracklistRenderer?
    ) {
        data class PlayerCaptionsTracklistRenderer(
            val captionTracks: List<CaptionTrack>?,
            val translationLanguages: List<TranslationLanguage>?
        )
    }
    data class Storyboards(
        val playerStoryboardSpecRenderer: StoryboardSpecRenderer?,
        val playerLiveStoryboardSpecRenderer: StoryboardSpecRenderer?
    ) {
        data class StoryboardSpecRenderer(
            val spec: String?
        )
    }
    data class Microformat(
        val playerMicroformatRenderer: PlayerMicroformatRenderer?
    ) {
        data class PlayerMicroformatRenderer(
            val liveBroadcastDetails: LiveBroadcastDetails?,
            val uploadDate: String?
        ) {
            data class LiveBroadcastDetails(
                val startTimestamp: String?
            )
        }
    }
    data class PaidContentOverlay(
        val paidContentOverlayRenderer: PaidContentOverlayRenderer?
    ) {
        data class PaidContentOverlayRenderer(
            val text: TextItem?
        )
    }
    data class PlaybackTracking(
        val videostatsPlaybackUrl: VideostatsUrl?,
        val videostatsWatchtimeUrl: VideostatsUrl?
    ) {
        data class VideostatsUrl(
            val baseUrl: String?
        )
    }
}

internal data class StreamingData(
    val expiresInSeconds: String?,
    val formats: List<StreamingFormat>?,
    val adaptiveFormats: List<StreamingFormat>?,
    val serverAbrStreamingUrl: String?,
    val hlsManifestUrl: String?,
    val dashManifestUrl: String?
)

internal data class StreamingFormat(
    // BaseFormat
    val itag: Int?,
    val mimeType: String?,
    val bitrate: Int?,
    val width: Int?,
    val height: Int?,
    val lastModified: String?,
    val contentLength: String?,
    val quality: String?,
    val fps: Int?,
    val qualityLabel: String?,
    val projectionType: String?,
    val averageBitrate: Int?,
    val approxDurationMs: String?,
    val targetDurationSec: Int?,
    val maxDvrDurationSec: Int?,
    val qualityOrdinal: String?,
    // audio
    val highReplication: Boolean?,
    val audioSampleRate: String?,
    val audioChannels: Int?,
    val loudnessDb: Float?,
    val trackAbsoluteLoudnessLkfs: Float?,
    val isDrc: Boolean?,
    /**
     * New format type FORMAT_STREAM_TYPE_OTF or null
     */
    val type: String?, // OTF
    // Old, DASH values
    val url: String?,
    val cipher: String?,
    val signatureCipher: String?,

    // LegacyFormat
    val audioQuality: String?,

    // AdaptiveFormat
    val initRange: Range?,
    val indexRange: Range?
) {
    data class Range(
        val start: String?,
        val end: String?
    )
}

internal data class CaptionTrack(
    val baseUrl: String?,
    val isTranslatable: Boolean?,
    val languageCode: String?,
    val vssId: String?,
    val name: TextItem?,
    val kind: String?
)

internal data class TranslationLanguage(
    val languageCode: String?,
    val languageName: TextItem?
)

internal data class VideoDetails(
    val lengthSeconds: String?,
    val title: String?,
    val channelId: String?,
    val videoId: String?,
    val shortDescription: String?,
    val viewCount: String?,
    val author: String?,
    val isLive: Boolean?,
    val isLiveContent: Boolean?,
    val isOwnerViewing: Boolean?,
    val thumbnail: ThumbnailItem?,
    val isLowLatencyLiveStream: Boolean?
)
