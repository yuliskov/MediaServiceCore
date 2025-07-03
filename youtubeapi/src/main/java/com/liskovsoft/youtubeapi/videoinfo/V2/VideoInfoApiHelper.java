package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.QueryBuilder;
import com.liskovsoft.youtubeapi.common.helpers.PostDataType;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

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
        boolean isPotSupported = client.isPotSupported() && PoTokenGate.supportsNpPot();
        LocaleManager localeManager = LocaleManager.instance();
        return new QueryBuilder(client)
                .setType(PostDataType.Player)
                .setLanguage(localeManager.getLanguage())
                .setCountry(localeManager.getCountry())
                .setUtcOffsetMinutes(localeManager.getUtcOffsetMinutes())
                .setVideoId(videoId)
                .setClickTrackingParams(clickTrackingParams)
                .setClientPlaybackNonce(AppService.instance().getClientPlaybackNonce()) // get it somewhere else?
                .setSignatureTimestamp(Helpers.parseInt(AppService.instance().getSignatureTimestamp())) // get it somewhere else?
                .setPoToken(isPotSupported ? PoTokenGate.getContentPoToken(videoId) : null)
                .setVisitorData(isPotSupported ? PoTokenGate.getVisitorData() : AppService.instance().getVisitorData())
                .enableGeoFix(enableGeoFix) // may broke other functionality
                .build();
    }
}
