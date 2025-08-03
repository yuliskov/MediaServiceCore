package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper;

/**
 * V8-windows not exists fix
 */
public class VideoInfoApiTestHelper {
    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\",\"signatureTimestamp\":%s}}";
    private static final String VIDEO_ID = "\"videoId\":\"%s\"";

    public static String getVideoInfoQuery(String videoId) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId);
        String checkParamsTemplate = String.format(CHECK_PARAMS, "18795");
        return PostDataHelper.createQueryTV(checkParamsTemplate + "," + videoIdTemplate);
    }
}
