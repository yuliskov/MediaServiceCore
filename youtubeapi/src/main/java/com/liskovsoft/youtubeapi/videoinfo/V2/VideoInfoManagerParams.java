package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

public class VideoInfoManagerParams {
    /**
     * Used in player only<br/>
     * Previous client version: 7.20190214<br/>
     * racyCheckOk, lockedSafetyMode - confirm age<br/>
     * contentCheckOk - ?
     */
    private static final String JSON_POST_DATA_TEMPLATE =
            "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"visitorData\":\"%s\",%s" +
            "\"thirdParty\":{\"embedUrl\":\"https://www.youtube.com/tv#/\"}," +
            "\"acceptRegion\":\"%s\",\"acceptLanguage\":\"%s\",\"utcOffsetMinutes\":\"%s\"}," +
            "\"user\":{\"lockedSafetyMode\":false}}," +
            "\"racyCheckOk\":true,\"contentCheckOk\":true,%s}";

    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\"," +
            "\"lactMilliseconds\":\"60000\"," +
            "\"signatureTimestamp\":%s}}";

    private static final String CLICK_TRACKING =
            "\"clickTracking\":{\"clickTrackingParams\":\"%s\"},";

    private static final String VIDEO_ID = "\"videoId\":\"%s\",\"cpn\":\"%s\"";

    public static String getVideoInfoQuery(String videoId) {
        return getVideoInfoQueryLive(videoId, null);
    }

    /**
     * Support live streams seeking!<br/>
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

    public static String getVideoInfoQueryRegular(String videoId, String clickTrackingParams) {
        return createCheckedQuery(AppConstants.CLIENT_NAME_WEB, AppConstants.CLIENT_VERSION_WEB, AppConstants.CLIENT_SCREEN_WATCH, videoId, clickTrackingParams);
    }

    public static String getVideoInfoQueryPrivate(String videoId) {
        return getVideoInfoQueryPrivate(videoId, null);
    }

    private static String createCheckedQuery(String clientName, String clientVersion, String screenType, String videoId, String clickTrackingParams) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        return createQuery(clientName, clientVersion, screenType, checkParamsTemplate + "," + videoIdTemplate, clickTrackingParams);
    }

    private static String createQuery(String clientName, String clientVersion, String screenType, String template, String clickTrackingParams) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(JSON_POST_DATA_TEMPLATE,
                clientName,
                clientVersion,
                screenType,
                AppService.instance().getVisitorData(),
                clickTrackingParams != null ? String.format(CLICK_TRACKING, clickTrackingParams) : "",
                localeManager.getCountry(),
                localeManager.getLanguage(),
                localeManager.getUtcOffsetMinutes(),
                template);
    }
}
