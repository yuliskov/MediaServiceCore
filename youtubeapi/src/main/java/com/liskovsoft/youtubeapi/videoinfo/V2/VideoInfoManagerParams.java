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
            "\"clientScreen\":\"WATCH\",\"visitorData\":\"%s\",%s" +
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
        return getVideoInfoQuery(videoId, null);
    }

    public static String getVideoInfoQuery(String videoId, String clickTrackingParams) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        return createQuery(AppConstants.CLIENT_NAME_WEB, AppConstants.CLIENT_VERSION_WEB, checkParamsTemplate + "," + videoIdTemplate, clickTrackingParams);
    }

    public static String getVideoInfoQueryPrivate(String videoId) {
        return getVideoInfoQueryPrivate(videoId, null);
    }

    public static String getVideoInfoQueryPrivate(String videoId, String clickTrackingParams) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        return createQuery(AppConstants.CLIENT_NAME_TV, AppConstants.CLIENT_VERSION_TV, checkParamsTemplate + "," + videoIdTemplate, clickTrackingParams);
    }

    private static String createQuery(String clientName, String clientVersion, String template, String clickTrackingParams) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(JSON_POST_DATA_TEMPLATE,
                clientName,
                clientVersion,
                AppService.instance().getVisitorData(),
                clickTrackingParams != null ? String.format(CLICK_TRACKING, clickTrackingParams) : "",
                localeManager.getCountry(),
                localeManager.getLanguage(),
                localeManager.getUtcOffsetMinutes(),
                template);
    }
}
