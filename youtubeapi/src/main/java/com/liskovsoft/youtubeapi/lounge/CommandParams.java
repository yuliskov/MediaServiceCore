package com.liskovsoft.youtubeapi.lounge;

import java.util.HashMap;
import java.util.Map;

public class CommandParams {
    private static final String COMMAND_NOW_PLAYING = "nowPlaying";
    private static final String COMMAND_ON_STATE_CHANGE = "onStateChange";

    private static final String FIELD_COUNT = "count";
    private static final String FIELD_OFS = "ofs";
    private static final String FIELD_COMMAND_NAME = "req0__sc";
    private static final String FIELD_STATE = "req0_state";
    private static final String FIELD_VIDEO_ID = "req0_videoId";
    private static final String FIELD_PLAYLIST_ID = "req0_listId";
    private static final String FIELD_POSITION = "req0_currentTime";
    private static final String FIELD_DURATION = "req0_duration";
    private static final String FIELD_LOADED_TIME = "req0_loadedTime";
    private static final String FIELD_SEEK_START = "req0_seekableStartTime";
    private static final String FIELD_SEEK_END = "req0_seekableEndTime";
    private static final String FIELD_CPN = "req0_cpn";

    public static Map<String, String> getNowPlaying(String videoId, long positionMs, long lengthMs) {
        Map<String, String> result = initCommand(positionMs, lengthMs);

        result.put(FIELD_COMMAND_NAME, COMMAND_NOW_PLAYING);
        result.put(FIELD_VIDEO_ID, videoId);

        return result;
    }

    public static Map<String, String> getOnStateChange(long positionMs, long lengthMs) {
        Map<String, String> result = initCommand(positionMs, lengthMs);

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_STATE_CHANGE);

        return result;
    }

    private static Map<String, String> initCommand(long positionMs, long lengthMs) {
        Map<String, String> result = new HashMap<>();

        result.put(FIELD_COUNT, "1");
        result.put(FIELD_OFS, "0");
        result.put(FIELD_POSITION, String.valueOf(positionMs / 1_000));
        result.put(FIELD_DURATION, String.valueOf(lengthMs / 1_000));
        result.put(FIELD_LOADED_TIME, "0");
        result.put(FIELD_SEEK_START, "0");
        result.put(FIELD_SEEK_END, String.valueOf(lengthMs / 1_000));

        return result;
    }
}
