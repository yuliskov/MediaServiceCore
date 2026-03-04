package com.liskovsoft.youtubeapi.lounge;

import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommandParams {
    private static final String COMMAND_NOW_PLAYING = "nowPlaying";
    private static final String COMMAND_ON_STATE_CHANGE = "onStateChange";
    private static final String COMMAND_ON_VOLUME_CHANGED = "onVolumeChanged";
    private static final String COMMAND_ON_PREVIOUS_NEXT_CHANGE = "onHasPreviousNextChanged";
    private static final String COMMAND_ON_VIDEO_QUALITY_CHANGED = "onVideoQualityChanged";
    private static final String COMMAND_ON_SUBTITLES_TRACK_CHANGED = "onSubtitlesTrackChanged";
    private static final String COMMAND_ON_DISCOVERY_DEVICE_ID = "setDiscoveryDeviceId";

    public static final int STATE_UNDETECTED = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_IDLE = 3;
    public static final int STATE_READY = 4;
    public static final int STATE_UNDEFINED = -1;
    
    private static final String FIELD_COUNT = "count";
    private static final String FIELD_OFS = "ofs";
    private static final String FIELD_PREFIX = "req0_";
    private static final String FIELD_PREFIX_FORMAT = "req%s_";
    private static final String FIELD_COMMAND_NAME = "req0__sc";
    private static final String FIELD_COMMAND_NAME_FORMAT = "%s_sc";
    private static final String FIELD_STATE = "req0_state";
    private static final String FIELD_VIDEO_ID = "req0_videoId";
    private static final String FIELD_PLAYLIST_ID = "req0_listId";
    private static final String FIELD_POSITION = "req0_currentTime";
    private static final String FIELD_DURATION = "req0_duration";
    private static final String FIELD_VOLUME = "req0_volume"; // 0-100
    private static final String FIELD_MUTED = "req0_muted";
    private static final String FIELD_CPN = "req0_cpn";
    private static final String FIELD_CTT = "req0_ctt";
    private static final String FIELD_INDEX = "req0_currentIndex";
    private static final String FIELD_LOADED_TIME = "req0_loadedTime";
    private static final String FIELD_SEEK_START = "req0_seekableStartTime";
    private static final String FIELD_SEEK_END = "req0_seekableEndTime";
    private static final String FIELD_HAS_PREVIOUS = "req0_hasPrevious";
    private static final String FIELD_HAS_NEXT = "req0_hasNext";
    private static final String FIELD_VSS_ID = "req0_vss_id";
    private static final String FIELD_LANGUAGE_CODE = "req0_languageCode";
    private static final String FIELD_SOURCE_LANGUAGE_CODE = "req0_sourceLanguageCode";
    private static final String FIELD_QUALITY_LEVEL = "req0_qualityLevel";
    private static final String FIELD_AVAILABLE_QUALITY_LEVELS = "req0_availableQualityLevels";
    private static final String FIELD_DISCOVERY_DEVICE_ID = "req0_discoveryDeviceId";
    private static final String FIELD_LOUNGE_DEVICE_ID = "req0_loungeDeviceId";

    private static final String CPN_NONE = "foo";
    private static int sOfsCounter;

    /**
     * <pre>
     *     "req4__sc": "nowPlaying",
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
     * </pre>
     */
    public static Map<String, String> getNowPlaying(String videoId,
                                                    long positionMs,
                                                    long durationMs,
                                                    String ctt,
                                                    String playlistId,
                                                    String playlistIndex) {
        Map<String, String> result = getBaseCommand(positionMs, durationMs);

        result.put(FIELD_COMMAND_NAME, COMMAND_NOW_PLAYING);
        result.put(FIELD_STATE, String.valueOf(STATE_IDLE));
        result.put(FIELD_VIDEO_ID, videoId);
        result.put(FIELD_CTT, ctt); // from previous request
        result.put(FIELD_PLAYLIST_ID, playlistId); // from previous request
        result.put(FIELD_INDEX, playlistIndex); // from previous request

        return result;
    }

    public static Map<String, String> getOnStateChange(long positionMs, long durationMs, int state) {
        Map<String, String> result = getBaseCommand(positionMs, durationMs);

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_STATE_CHANGE);
        result.put(FIELD_STATE, String.valueOf(state));
        result.put(FIELD_CPN, CPN_NONE);

        return result;
    }

    /**
     * <pre>
     *     "req2__sc": "onHasPreviousNextChanged",
     *     "req2_hasPrevious": "false",
     *     "req2_hasNext": "true",
     * </pre>
     */
    public static Map<String, String> getOnPrevNextChange() {
        Map<String, String> result = getBaseCommand(-1, -1);

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_PREVIOUS_NEXT_CHANGE);
        result.put(FIELD_HAS_PREVIOUS, "false");
        result.put(FIELD_HAS_NEXT, "false");

        return result;
    }

    /**
     * <pre>
     *     "req7__sc": "onVolumeChanged",
     *     "req7_volume": "50",
     *     "req7_muted": "false",
     * </pre>
     */
    public static Map<String, String> getOnVolumeChanged(int volume) {
        Map<String, String> result = getBaseCommand();

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_VOLUME_CHANGED);
        result.put(FIELD_VOLUME, String.valueOf(volume));
        result.put(FIELD_MUTED, "false");

        return result;
    }

    /**
     * <pre>
     *     "req5_videoId": "hWKr20RnVM0",
     *     "req5_qualityLevel": "360",
     *     "req5_availableQualityLevels": "[0,720,480,360,240,144]",
     * </pre>
     */
    public static Map<String, String> getOnVideoQualityChanged(String videoId, String qualityLevel, List<String> availableQualityLevels) {
        Map<String, String> result = getBaseCommand();

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_VIDEO_QUALITY_CHANGED);
        result.put(FIELD_VIDEO_ID, videoId);
        result.put(FIELD_QUALITY_LEVEL, qualityLevel);
        result.put(FIELD_AVAILABLE_QUALITY_LEVELS,
                Helpers.join(",", availableQualityLevels, item -> item
                        .toLowerCase()
                        .replace("p", "")
                        .trim()
                ));

        return result;
    }

    /**
     * required parameters: vssId, languageCode or sourceLanguageCode (if languageCode is null, sourceLanguageCode will be used as fallback)
     * <pre>
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
     *  </pre>
     */
    public static Map<String, String> getOnSubtitlesTrackChanged(String vssId, String languageCode) {
        Map<String, String> result = getBaseCommand();

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_SUBTITLES_TRACK_CHANGED);

        if (vssId != null) {
            result.put(FIELD_VSS_ID, vssId);
        }

        if (languageCode != null) {
            result.put(FIELD_LANGUAGE_CODE, languageCode);
            result.put(FIELD_SOURCE_LANGUAGE_CODE, languageCode);
        }

        return result;
    }

    /**
     * <pre>
     *  "req0__sc": "setDiscoveryDeviceId",
     *  "req0_discoveryDeviceId": "d6e51266-0538-44a8-8cc7-0687e27cb89c",
     *  "req0_loungeDeviceId": "d6e51266-0538-44a8-8cc7-0687e27cb89c",
     * </pre>
     */
    public static Map<String, String> getOnDiscoveryDeviceId(String discoveryDeviceId) {
        Map<String, String> result = getBaseCommand();

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_DISCOVERY_DEVICE_ID);
        result.put(FIELD_DISCOVERY_DEVICE_ID, discoveryDeviceId);
        result.put(FIELD_LOUNGE_DEVICE_ID, discoveryDeviceId);

        return result;
    }

    @SafeVarargs
    public static Map<String, String> packageCommands(Map<String, String>... commands) {
        if (Helpers.allNulls(commands)) {
            return null;
        }

        if (commands.length == 1) {
            return commands[0];
        }

        Map<String, String> result = new HashMap<>();

        int index = -1;

        for (Map<String, String> command : commands) {
            if (command == null) {
                continue;
            }

            sOfsCounter--;
            index++;
            String prefix = String.format(FIELD_PREFIX_FORMAT, index);
            result.put(String.format(FIELD_COMMAND_NAME_FORMAT, prefix), command.get(FIELD_COMMAND_NAME));
            for (Entry<String, String> entry : command.entrySet()) {
                if (Helpers.equalsAny(entry.getKey(), FIELD_COMMAND_NAME, FIELD_COUNT, FIELD_OFS)) {
                    continue;
                }

                result.put(String.format("%s%s", prefix, entry.getKey().replace(FIELD_PREFIX, "")), entry.getValue());
            }
        }

        result.put(FIELD_COUNT, String.valueOf(index + 1));
        result.put(FIELD_OFS, String.valueOf(sOfsCounter++));

        return result;
    }

    private static Map<String, String> getBaseCommand(long positionMs, long durationMs) {
        Map<String, String> result = getBaseCommand();

        if (positionMs >= 0) {
            result.put(FIELD_POSITION, String.valueOf(positionMs / 1_000f));
        }
        if (durationMs > 0) {
            result.put(FIELD_DURATION, String.valueOf(durationMs / 1_000f));
            result.put(FIELD_SEEK_END, String.valueOf(durationMs / 1_000f));
        }
        result.put(FIELD_LOADED_TIME, "0");
        result.put(FIELD_SEEK_START, "0");

        return result;
    }

    private static Map<String, String> getBaseCommand() {
        Map<String, String> result = new HashMap<String, String>() {
            @Nullable
            @Override
            public String put(String key, String value) {
                if (value == null) {
                    return null;
                }

                return super.put(key, value);
            }
        };

        result.put(FIELD_COUNT, "1");
        result.put(FIELD_OFS, String.valueOf(sOfsCounter++));

        return result;
    }
}
