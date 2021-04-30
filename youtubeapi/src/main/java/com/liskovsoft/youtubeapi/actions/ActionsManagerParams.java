package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class ActionsManagerParams {
    private static final String VIDEO_ID_TEMPLATE = "\"target\":{\"videoId\":\"%s\"}";
    private static final String CHANNEL_ID_TEMPLATE = "\"channelIds\":[\"%s\"]";

    public static String getLikeActionQuery(String videoId) {
        String likeTemplate = String.format(VIDEO_ID_TEMPLATE, videoId);
        return ServiceHelper.createQuery(likeTemplate);
    }

    public static String getSubscribeActionQuery(String channelId) {
        String channelTemplate = String.format(CHANNEL_ID_TEMPLATE, channelId);
        return ServiceHelper.createQuery(channelTemplate);
    }
}
