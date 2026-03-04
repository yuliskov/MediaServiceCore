package com.liskovsoft.youtubeapi.lounge.models.receiverCommands

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal data class ReceiverEvent(
    val eventType: String,
    val valueMap: Map<String, String>
) {
    companion object {
        @Volatile
        private var ofsCounter = 0

        /**
         * packageReceiverEvents packages multiple ReceiverEvents into a Map<String, String>,
         * which contains the number of events, offsets, and parameters of each event.
         */
        @Synchronized
        @JvmStatic
        fun packageReceiverEvents(vararg event: ReceiverEvent): Map<String, String> {
            val eventMap = mutableMapOf<String, String>()
            eventMap["count"] = event.size.toString()
            eventMap["ofs"] = (ofsCounter++).toString()
            event.forEachIndexed { index, receiverEvent ->
                val prefix = "req${index}_"
                eventMap["${prefix}_sc"] = receiverEvent.eventType
                receiverEvent.valueMap.forEach { (key, value) ->
                    eventMap["${prefix}${key}"] = value
                }
            }
            return eventMap
        }
    }
}

/**
 * URL encode Lounge API parameters (only encode characters that need escaping)
 */
private fun encodeLoungeParam(value: String): String {
    // Must specify StandardCharsets.UTF_8 to avoid Chinese garbled characters
    return URLEncoder.encode(value, StandardCharsets.UTF_8.name())
        // Optional: replace + with %20 (some services require space to be encoded as %20 instead of +)
        .replace("+", "%20")
}

internal sealed class ReceiverEventBuilder(
    private val eventType: String,
    protected val valueMap: MutableMap<String, String> = mutableMapOf()
) {
    /**
     * "req1__sc": "onPlaybackSpeedChanged",
     *     "req1_playbackSpeed": "1",
     */
    class OnPlaybackSpeedChanged: ReceiverEventBuilder("onPlaybackSpeedChanged") {
        fun playbackSpeed(speed: Float) = apply {
            valueMap["playbackSpeed"] = speed.toString()
        }
    }

    /**
     * "req2__sc": "onHasPreviousNextChanged",
     *     "req2_hasPrevious": "false",
     *     "req2_hasNext": "true",
     */
    class OnHasPreviousNextChanged: ReceiverEventBuilder("onHasPreviousNextChanged") {
        fun hasPrevious(hasPrevious: Boolean) = apply {
            valueMap["hasPrevious"] = hasPrevious.toString()
        }

        fun hasNext(hasNext: Boolean) = apply {
            valueMap["hasNext"] = hasNext.toString()
        }
    }

    /**
     * "req4__sc": "nowPlaying",
     *     "req4_videoId": "hWKr20RnVM0",
     *     "req4_state": "3",
     *     "req4_currentTime": "0",
     *     "req4_duration": "330",
     *     "req4_loadedTime": "0",
     *     "req4_seekableStartTime": "0",
     *     "req4_seekableEndTime": "0",
     *     "req4_cpn": "2p5Wy2-Pn001CQCT",
     *     "req4_playabilityStatus": "OK",
     *     "req4_listId": "RDhWKr20RnVM0",
     *     "req4_currentIndex": "0",
     *     "req4_mdxExpandedReceiverVideoIdList": "videoId1,videoId2,videoId3"
     */

    class UpdateNowPlaying: ReceiverEventBuilder("nowPlaying") {
        init {
            playabilityStatus("OK")
            seekableStartTime(0f)
            loadedTime(0f)
        }

        fun videoId(videoId: String?) = apply {
            if (videoId.isNullOrEmpty()) {
                return@apply
            }
            valueMap["videoId"] = videoId
        }

        fun state(state: String) = apply {
            valueMap["state"] = state
        }

        fun currentTime(currentTimeSec: Float) = apply {
            valueMap["currentTime"] = currentTimeSec
                .coerceAtLeast(0f)
                .toInt()
                .toString()
        }

        fun duration(durationSec: Float) = apply {
            valueMap["duration"] = durationSec
                .coerceAtLeast(0f)
                .toInt()
                .toString()
        }

        fun loadedTime(loadedTimeSec: Float) = apply {
            valueMap["loadedTime"] = loadedTimeSec
                .coerceAtLeast(0f)
                .toInt()
                .toString()
        }

        fun seekableStartTime(seekableStartTimeSec: Float) = apply {
            valueMap["seekableStartTime"] = seekableStartTimeSec
                .coerceAtLeast(0f)
                .toInt()
                .toString()
        }

        fun seekableEndTime(seekableEndTimeSec: Float) = apply {
            valueMap["seekableEndTime"] = seekableEndTimeSec
                .coerceAtLeast(0f)
                .toInt()
                .toString()
        }

        fun cpn(cpn: String) = apply {
            valueMap["cpn"] = cpn
        }

        fun playabilityStatus(playabilityStatus: String) = apply {
            valueMap["playabilityStatus"] = playabilityStatus
        }

        fun ctt(ctt: String) = apply {
            if (ctt.isEmpty()) {
                return@apply
            }
            valueMap["ctt"] = ctt
        }

        fun listId(listId: String) = apply {
            valueMap["listId"] = listId
        }

        fun currentIndex(currentIndex: String) = apply {
            valueMap["currentIndex"] = currentIndex
        }

        fun mdxExpandedReceiverVideoIdList(videoIdList: List<String>) = apply {
            if (videoIdList.isEmpty()) {
                return@apply
            }
            valueMap["mdxExpandedReceiverVideoIdList"] = encodeLoungeParam(
                videoIdList.joinToString(",")
            )
        }
    }

    /**
     * "req5_videoId": "hWKr20RnVM0",
     *     "req5_qualityLevel": "360",
     *     "req5_availableQualityLevels": "[0,720,480,360,240,144]",
     */
    class OnVideoQualityChanged: ReceiverEventBuilder("onVideoQualityChanged") {
        fun videoId(videoId: String) = apply {
            valueMap["videoId"] = videoId
        }

        fun qualityLevel(qualityLevel: String) = apply {
            valueMap["qualityLevel"] = qualityLevel.lowercase()
                .replace("p", "")
                .trim()
        }

        fun availableQualityLevels(availableQualityLevels: List<String>) = apply {
            valueMap["availableQualityLevels"] = encodeLoungeParam(
                availableQualityLevels.joinToString(",") {
                    it.lowercase()
                        .replace("p", "")
                        .trim()
                }
            )
        }
    }

    /**
     * required parameters: vssId, languageCode or sourceLanguageCode (if languageCode is null, sourceLanguageCode will be used as fallback)
     *
     * @sample
     *      "req0__sc": "onSubtitlesTrackChanged",
     *      "req0_videoId": "-rF55vaURO0",
     *      "req0_trackName": "",
     *      "req0_languageCode": "en",
     *      "req0_sourceLanguageCode": "en",
     *      "req0_languageName": "English (auto-generated)",
     *      "req0_kind": "asr",
     *      "req0_vss_id": "a.en",
     *      "req0_style": "{\"charEdgeStyle\":\"none\",\"color\":\"#FFFFFF\",
     *          \"background\":\"#000000\",\"windowColor\":\"#0000FF\",\"fontFamilyOption\":\"\",
     *          \"backgroundOpacity\":\"1.00\",\"textOpacity\":\"1.00\",\"windowOpacity\":\"0.00\",
     *          \"fontSizeRelative\":\"1.00\"
     *      }"
     */
    class OnSubtitlesTrackChanged: ReceiverEventBuilder("onSubtitlesTrackChanged") {
        init {
            // Default style, can be overridden by setStyle event
            // actually, YouTube's default style is determined by the receiver's system settings,
            // but since we don't apply with it, so just use a fixed default style to avoid some weird styles (e.g. white text with white background)
            valueMap["style"] = "{\"charEdgeStyle\":\"none\",\"color\":\"#FFFFFF\"," +
                    "\"background\":\"#000000\",\"windowColor\":\"#0000FF\"," +
                    "\"fontFamilyOption\":\"\",\"backgroundOpacity\":\"1.00\"," +
                    "\"textOpacity\":\"1.00\",\"windowOpacity\":\"0.00\"," +
                    "\"fontSizeRelative\":\"1.00\"}"
        }

        fun trackName(trackName: String) = apply {
            valueMap["trackName"] = trackName
        }

        fun languageCode(languageCode: String?) = apply {
            valueMap["languageCode"] = languageCode ?: return@apply
            sourceLanguageCode(languageCode)
        }

        fun sourceLanguageCode(sourceLanguageCode: String) = apply {
            valueMap["sourceLanguageCode"] = sourceLanguageCode
        }

        fun languageName(languageName: String?) = apply {
            valueMap["languageName"] = languageName ?: return@apply
        }

        fun kind(kind: String) = apply {
            valueMap["kind"] = kind
        }

        fun vssId(vssId: String) = apply {
            valueMap["vss_id"] = vssId
        }
    }

    /**
     * "req7__sc": "onVolumeChanged",
     *     "req7_volume": "50",
     *     "req7_muted": "false",
     */
    class OnVolumeChanged: ReceiverEventBuilder("onVolumeChanged") {
        fun volume(volume: Int) = apply {
            valueMap["volume"] = volume.toString()
        }

        fun muted(muted: Boolean) = apply {
            valueMap["muted"] = muted.toString()
        }
    }

    /**
     * "req0__sc": "setDiscoveryDeviceId",
     * "req0_discoveryDeviceId": "d6e51266-0538-44a8-8cc7-0687e27cb89c",
     * "req0_loungeDeviceId": "d6e51266-0538-44a8-8cc7-0687e27cb89c",
     */
    class SetDiscoveryDeviceId: ReceiverEventBuilder("setDiscoveryDeviceId") {
        fun discoveryDeviceId(discoveryDeviceId: String) = apply {
            valueMap["discoveryDeviceId"] = discoveryDeviceId
            valueMap["loungeDeviceId"] = discoveryDeviceId
        }
    }

    fun build(): ReceiverEvent {
        return ReceiverEvent(
            eventType = eventType,
            valueMap = valueMap
        )
    }
}