package com.liskovsoft.youtubeapi.lounge;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CommandParams {
    private static final String COMMAND_NOW_PLAYING = "nowPlaying";
    private static final String COMMAND_ON_STATE_CHANGE = "onStateChange";
    private static final String COMMAND_ON_VOLUME_CHANGED = "onVolumeChanged";
    private static final String COMMAND_ON_PREVIOUS_NEXT_CHANGE = "onHasPreviousNextChanged";

    public static final int STATE_UNDETECTED = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_IDLE = 3;
    public static final int STATE_READY = 4;
    public static final int STATE_UNDEFINED = -1;
    
    private static final String FIELD_COUNT = "count";
    private static final String FIELD_OFS = "ofs";
    private static final String FIELD_COMMAND_NAME = "req0__sc";
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

    private static final String CPN_NONE = "foo";
    private static int sOfsCounter;

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

    public static Map<String, String> getOnPrevNextChange() {
        Map<String, String> result = getBaseCommand(-1, -1);

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_PREVIOUS_NEXT_CHANGE);
        result.put(FIELD_HAS_PREVIOUS, "false");
        result.put(FIELD_HAS_NEXT, "false");

        return result;
    }

    public static Map<String, String> getOnVolumeChanged(int volume) {
        Map<String, String> result = getBaseCommand();

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_VOLUME_CHANGED);
        result.put(FIELD_VOLUME, String.valueOf(volume));
        result.put(FIELD_MUTED, "false");

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
