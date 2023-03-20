package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

public class VideoInfoApiHelper {
    /**
     * Used in player only<br/>
     * Previous client version: 7.20190214<br/>
     * racyCheckOk, lockedSafetyMode - confirm age<br/>
     * contentCheckOk - ?
     */
    private static final String JSON_POST_DATA_TEMPLATE =
            "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"visitorData\":\"%s\"," +
            "\"thirdParty\":{\"embedUrl\":\"https://www.youtube.com/tv#/\"}," +
            "\"acceptRegion\":\"%s\",\"acceptLanguage\":\"%s\",\"utcOffsetMinutes\":\"%s\"},%s" +
            "\"user\":{\"lockedSafetyMode\":false}}," +
            "\"racyCheckOk\":true,\"contentCheckOk\":true,%s}";

    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\"," +
            "\"lactMilliseconds\":\"60000\"," +
            "\"signatureTimestamp\":%s}}";

    private static final String CLICK_TRACKING =
            "\"clickTracking\":{\"clickTrackingParams\":\"%s\"},";

    private static final String VIDEO_ID = "\"videoId\":\"%s\",\"cpn\":\"%s\"";

    // NOTE: completely removes storyboard (seek previews) from the result!
    // Magic val to fix throttling???
    // https://github.com/TeamNewPipe/NewPipe/issues/9038#issuecomment-1289756816
    private static final String PROTOBUF_VAL = "\"params\":\"CgIQBg%3D%3D\"";

    public static String getVideoInfoQuery(String videoId) {
        return getVideoInfoQueryLive(videoId, null);
    }

    /**
     * Support live streams seeking!<br/>
     * NOTE: Don't support startTimestamp<br/>
     * NOTE: CLIENT_NAME_ANDROID doesn't play 18+ videos
     */
    public static String getVideoInfoQueryLive(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_ANDROID, AppConstants.CLIENT_VERSION_ANDROID, AppConstants.CLIENT_SCREEN_WATCH, videoId, clickTrackingParams);
    }

    /**
     * Support viewing private (user) videos
     */
    public static String getVideoInfoQueryPrivate(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_TV, AppConstants.CLIENT_VERSION_TV, AppConstants.CLIENT_SCREEN_WATCH, videoId, clickTrackingParams);
    }

    /**
     * Support restricted (18+) videos viewing (not working right now).
     */
    public static String getVideoInfoQueryEmbed(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_WEB, AppConstants.CLIENT_VERSION_WEB, AppConstants.CLIENT_SCREEN_EMBED, videoId, clickTrackingParams);
    }

    /**
     * Support restricted (18+) videos viewing. Alt method from github
     */
    public static String getVideoInfoQueryEmbed2(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_EMBED, AppConstants.CLIENT_VERSION_EMBED, AppConstants.CLIENT_SCREEN_WATCH, videoId, clickTrackingParams);
    }

    /**
     * Support live streams seeking!<br/>
     */
    public static String getVideoInfoQueryRegular(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_WEB, AppConstants.CLIENT_VERSION_WEB, AppConstants.CLIENT_SCREEN_WATCH, videoId, clickTrackingParams);
    }

    public static String getVideoInfoQueryGeoBlocked(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_WEB, AppConstants.CLIENT_VERSION_WEB, AppConstants.CLIENT_SCREEN_WATCH, videoId, clickTrackingParams, PROTOBUF_VAL);
    }

    public static String getVideoInfoQueryPrivate(String videoId) {
        return getVideoInfoQueryPrivate(videoId, null);
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

    private static String createCheckedQuery(String clientName, String clientVersion, String screenType, String videoId, String clickTrackingParams) {
        return createCheckedQuery(clientName, clientVersion, screenType, videoId, clickTrackingParams, null);
    }

    private static String createCheckedQuery(String clientName, String clientVersion, String screenType, String videoId, String clickTrackingParams, String params) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        return createQuery(clientName, clientVersion, screenType, clickTrackingParams, Helpers.join(",", checkParamsTemplate, videoIdTemplate, params));
    }

    private static String createQuery(String clientName, String clientVersion, String screenType, String clickTrackingParams, String template) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(JSON_POST_DATA_TEMPLATE,
                clientName,
                clientVersion,
                screenType,
                AppService.instance().getVisitorId(),
                localeManager.getCountry(),
                localeManager.getLanguage(),
                localeManager.getUtcOffsetMinutes(),
                clickTrackingParams != null ? String.format(CLICK_TRACKING, clickTrackingParams) : "",
                template);
    }
}
