package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class VideoInfoApiHelper {
    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\"," +
            "\"lactMilliseconds\":\"60000\"," +
            "\"signatureTimestamp\":%s}}";

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

    /**
     * Doesn't work
     */
    private static String getVideoInfoQueryAndroid(String videoId) {
        return getVideoInfoQueryAndroid(videoId, null);
    }

    /**
     * Support live streams seeking!<br/>
     * NOTE: Don't support startTimestamp<br/>
     * NOTE: CLIENT_NAME_ANDROID doesn't play 18+ videos
     */
    public static String getVideoInfoQueryAndroid(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_ANDROID, videoId, clickTrackingParams);
    }

    /**
     * Doesn't work!<br/>
     * Support live streams seeking!<br/>
     * NOTE: Don't support startTimestamp<br/>
     * NOTE: CLIENT_NAME_ANDROID doesn't play 18+ videos
     */
    public static String getVideoInfoQueryGeoAndroid(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_ANDROID, videoId, clickTrackingParams, THROTTLE_QUERY);
    }

    public static String getVideoInfoQueryTV(String videoId) {
        return getVideoInfoQueryTV(videoId, null);
    }

    /**
     * Support viewing private (user) videos
     */
    public static String getVideoInfoQueryTV(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_TV, videoId, clickTrackingParams);
    }

    /**
     * Support restricted (18+) videos viewing. Alt method from github
     */
    public static String getVideoInfoQueryEmbed(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_EMBED, videoId, clickTrackingParams);
    }

    /**
     * Support live streams seeking!<br/>
     */
    public static String getVideoInfoQueryWeb(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_WEB, videoId, clickTrackingParams);
    }

    /**
     * NOTE: Should use protobuf to bypass geo blocking.
     */
    public static String getVideoInfoQueryGeoWeb(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_WEB, videoId, clickTrackingParams, THROTTLE_QUERY);
    }

    public static String getVideoInfoQueryIOS(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.JSON_POST_DATA_PLAYER_IOS, videoId, clickTrackingParams);
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

    private static String createCheckedQuery(String template, String videoId, String clickTrackingParams) {
        return createCheckedQuery(template, videoId, clickTrackingParams, null);
    }

    private static String createCheckedQuery(String template, String videoId, String clickTrackingParams, String query) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        clickTrackingParams = clickTrackingParams != null ? String.format(CLICK_TRACKING, clickTrackingParams) : "";
        return ServiceHelper.createQuery(template, clickTrackingParams, Helpers.join(",", checkParamsTemplate, videoIdTemplate, query));
    }
}
