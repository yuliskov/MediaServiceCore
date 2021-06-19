package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class VideoInfoManagerParams {
    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\",\"signatureTimestamp\":%s}}";
    private static final String VIDEO_ID = "\"videoId\":\"%s\",\"cpn\":\"%s\"";

    public static String getVideoInfoQuery(String videoId) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        return ServiceHelper.createQuery(checkParamsTemplate + "," + videoIdTemplate);
    }
}
