package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class VideoInfoManagerParams {
    private static final String VIDEO_ID = "\"videoId\":\"%s\",\"ps\":\"%s\"";

    public static String getVideoInfoQuery(String videoId) {
        String channelTemplate = String.format(VIDEO_ID, videoId, "leanback");
        return ServiceHelper.createQuery(channelTemplate);
    }

    public static String getRestrictedVideoInfoQuery(String videoId) {
        String channelTemplate = String.format(VIDEO_ID, videoId, "default");
        return ServiceHelper.createQuery(channelTemplate);
    }
}
