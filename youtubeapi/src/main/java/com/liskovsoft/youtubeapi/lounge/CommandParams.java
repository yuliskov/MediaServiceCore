package com.liskovsoft.youtubeapi.lounge;

import java.util.HashMap;
import java.util.Map;

public class CommandParams {
    private static final String COMMAND_NOW_PLAYING = "nowPlaying";
    private static final String COMMAND_ON_STATE_CHANGE = "onStateChange";

    private static final int STATE_UNDETECTED = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSED = 2;
    private static final int STATE_IDLE = 3;
    private static final int STATE_READY = 4;
    private static final int STATE_UNDEFINED = -1;
    
    private static final String FIELD_COUNT = "count";
    private static final String FIELD_OFS = "ofs";
    private static final String FIELD_COMMAND_NAME = "req0__sc";
    private static final String FIELD_STATE = "req0_state";
    private static final String FIELD_VIDEO_ID = "req0_videoId";
    private static final String FIELD_PLAYLIST_ID = "req0_listId";
    private static final String FIELD_POSITION = "req0_currentTime";
    private static final String FIELD_DURATION = "req0_duration";
    private static final String FIELD_CPN = "req0_cpn";
    private static final String FIELD_CTT = "req0_ctt";
    private static final String FIELD_INDEX = "req0_currentIndex";
    private static final String FIELD_LOADED_TIME = "req0_loadedTime";
    private static final String FIELD_SEEK_START = "req0_seekableStartTime";
    private static final String FIELD_SEEK_END = "req0_seekableEndTime";

    private static final String CPN_NONE = "foo";
    private static int sOfsCounter;

    public static Map<String, String> getNowPlaying(String videoId,
                                                    long positionMs,
                                                    long lengthMs,
                                                    String ctt,
                                                    String playlistId,
                                                    String playlistIndex) {
        Map<String, String> result = initCommand(positionMs, lengthMs);

        result.put(FIELD_COMMAND_NAME, COMMAND_NOW_PLAYING);
        result.put(FIELD_STATE, String.valueOf(STATE_IDLE));
        result.put(FIELD_VIDEO_ID, videoId);
        result.put(FIELD_CTT, ctt); // from previous request
        result.put(FIELD_PLAYLIST_ID, playlistId); // from previous request
        result.put(FIELD_INDEX, playlistIndex); // from previous request

        return result;
    }

    public static Map<String, String> getOnStateChange(long positionMs, long lengthMs) {
        Map<String, String> result = initCommand(positionMs, lengthMs);

        result.put(FIELD_COMMAND_NAME, COMMAND_ON_STATE_CHANGE);
        result.put(FIELD_STATE, String.valueOf(STATE_PLAYING));
        result.put(FIELD_CPN, CPN_NONE);

        return result;
    }

    private static Map<String, String> initCommand(long positionMs, long lengthMs) {
        Map<String, String> result = new HashMap<>();

        result.put(FIELD_COUNT, "1");
        result.put(FIELD_OFS, String.valueOf(sOfsCounter++));
        result.put(FIELD_POSITION, String.valueOf(positionMs / 1_000f));
        result.put(FIELD_DURATION, String.valueOf(lengthMs / 1_000f));
        result.put(FIELD_LOADED_TIME, "0");
        result.put(FIELD_SEEK_START, "0");
        result.put(FIELD_SEEK_END, String.valueOf(lengthMs / 1_000f));

        return result;
    }
}
