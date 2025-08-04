package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.QueryBuilder;

public class VideoInfoApiHelper {
    public static String getVideoInfoQuery(AppClient client, String videoId, String clickTrackingParams) {
        return createCheckedQuery(client, videoId, clickTrackingParams, false);
    }

    /**
     * NOTE: Should use protobuf to bypass geo blocking.
     */
    public static String getVideoInfoQueryGeo(AppClient client, String videoId, String clickTrackingParams) {
        return createCheckedQuery(client, videoId, clickTrackingParams, true);
    }

    private static String createCheckedQuery(AppClient client, String videoId, String clickTrackingParams, boolean enableGeoFix) {
        // Important: use only for the clients that don't support auth.
        // Otherwise, google suggestions and history won't work (visitor data bug)
        boolean isPotSupported = client.isPotSupported() && PoTokenGate.isNpPotSupported();
        return new QueryBuilder(client)
                .setVideoId(videoId)
                .setClickTrackingParams(clickTrackingParams)
                .setPoToken(isPotSupported ? PoTokenGate.getContentPoToken(videoId) : null)
                .setVisitorData(isPotSupported ? PoTokenGate.getVisitorData() : null)
                .enableGeoFix(enableGeoFix) // may broke other functionality
                .build();
    }
}
