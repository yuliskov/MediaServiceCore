package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class TrackingApiParams {
    public static String getHistoryQuery() {
        return ServiceHelper.createQuery("\"nop\":\"false\"");
    }
}
