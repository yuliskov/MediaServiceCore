package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.QueryBuilder;
import com.liskovsoft.youtubeapi.common.helpers.PostDataType;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

public class VideoInfoApiHelper {
    // For isInlinePlaybackNoAd see https://iter.ca/post/yt-adblock/
    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\"," +
            "\"lactMilliseconds\":\"60000\",\"isInlinePlaybackNoAd\":true," +
            "\"signatureTimestamp\":%s}}";

    private static final String CONTENT_POT_WEB =
            "\"serviceIntegrityDimensions\":{\"poToken\":\"%s\"}";

    private static final String CONTENT_POT_TV = "\"attestationRequest\":{\"omitBotguardData\":true}";

    private static final String CLICK_TRACKING =
            "\"clickTracking\":{\"clickTrackingParams\":\"%s\"},";

    private static final String VIDEO_ID = "\"videoId\":\"%s\",\"cpn\":\"%s\"";

    // Magic val to fix throttling???
    // Workaround streaming URLs returning 403 when using Android clients
    // NOTE: Completely removes storyboard (seek previews) from the result!
    // NOTE: Helps to bypass geo blocking.
    // https://github.com/TeamNewPipe/NewPipe/issues/9038#issuecomment-1289756816
    // https://github.com/revanced/revanced-patches/issues/2432#issuecomment-1601819762
    // https://github.com/yt-dlp/yt-dlp/issues/7811
    // https://github.com/yt-dlp/yt-dlp/commit/81ca451480051d7ce1a31c017e005358345a9149
    //private static final String THROTTLE_QUERY = "\"params\":\"CgIQBg==\"";
    private static final String THROTTLE_QUERY = "\"params\":\"CgIQBg%3D%3D\"";
    private static final String REGULAR_QUERY = "\"params\":\"YAHIAQE%3D\""; // taken from the web browser (used sometimes)

    // Workaround streaming URLs returning 403 when using Android clients
    // https://github.com/LuanRT/YouTube.js/pull/390/commits/6511c23fe6133f4b066c558ebfa531e1ce7c0062
    //private static final String PROTOBUF_VAL_ANDROID = "\"params\":\"8AEB\"";

    public static String getVideoInfoQuery(AppClient client, String videoId, String clickTrackingParams) {
        return createCheckedQuery(client, videoId, clickTrackingParams);
    }

    /**
     * NOTE: Should use protobuf to bypass geo blocking.
     */
    public static String getVideoInfoQueryGeo(AppClient client, String videoId, String clickTrackingParams) {
        return createCheckedQuery(client, videoId, clickTrackingParams, THROTTLE_QUERY);
    }

    /**
     * Is bugged. 'alr=yes' makes some streams unplayable<br/>
     * New part of the request: alr=yes&cpn=zdi3fp0GozNItWoY&cver=2.20221026.05.00&headm=3&rn=1&rbuf=0
     */
    public static String getDashInfoFormatUrl(String originUrl) {
        if (originUrl == null) {
            return null;
        }

        // Maybe this can fix empty DashInfo response?
        return originUrl + "&alr=no&headm=3&rn=1&rbuf=0"; // alr=yes is bugged
    }

    private static String createCheckedQuery(AppClient client, String videoId, String clickTrackingParams) {
        return createCheckedQuery(client, videoId, clickTrackingParams, null);
    }

    private static String createCheckedQuery(AppClient client, String videoId, String clickTrackingParams, String query) {
        // Important: use only for the clients that don't support auth.
        // Otherwise, google suggestions and history won't work (visitor data bug)
        if (isPotSupported(client) && PoTokenGate.supportsNpPot()) {
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
                    .setPoToken(PoTokenGate.getContentPoToken(videoId))
                    .setVisitorData(PoTokenGate.getVisitorData())
                    .setAsWebEmbedded(client == AppClient.WEB_EMBED)
                    .build();
        }

        String template = client.getPlayerTemplate();
        String videoIdParams = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParams = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());

        clickTrackingParams = clickTrackingParams != null ? String.format(CLICK_TRACKING, clickTrackingParams) : "";
        return ServiceHelper.createQuery(template, clickTrackingParams, Helpers.join(",", checkParams, videoIdParams, query));
    }

    private static boolean isPotSupported(AppClient client) {
        return client == AppClient.WEB || client == AppClient.MWEB || client == AppClient.WEB_EMBED || client == AppClient.ANDROID_VR;
    }
}
