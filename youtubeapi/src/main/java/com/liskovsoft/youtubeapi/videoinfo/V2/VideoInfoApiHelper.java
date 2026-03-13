package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.QueryBuilder;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

public class VideoInfoApiHelper {
    public static String getVideoInfoQuery(AppClient client, String videoId, String clickTrackingParams) {
        return createCheckedQuery(client, videoId, clickTrackingParams, client == AppClient.GEO);
    }

    /**
     * NOTE: enableGeoFix - Should use protobuf to bypass geo blocking.
     */
    private static String createCheckedQuery(AppClient client, String videoId, String clickTrackingParams, boolean enableGeoFix) {
        // Important: use only for the clients that don't support auth.
        // Otherwise, google suggestions and history won't work (visitor data bug)
        return new QueryBuilder(client)
                .setVideoId(videoId)
                .setClickTrackingParams(clickTrackingParams)
                .setPoToken(PoTokenGate.getPoToken(client, videoId))
                .setVisitorData(PoTokenGate.getVisitorData(client))
                .enableGeoFix(enableGeoFix) // may broke other functionality
                // Temp history fix. Remove when nsigsolver will be fully fixed
                .setSignatureTimestamp(client == AppClient.TV_AUTH && MediaServiceData.instance().getFailedAppInfo() != null ? 205220 : -1)
                .build();
    }
}
