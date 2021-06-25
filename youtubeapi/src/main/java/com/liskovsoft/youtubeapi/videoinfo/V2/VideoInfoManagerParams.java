package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

public class VideoInfoManagerParams {
    private static final String CLIENT_VERSION_WEB = "2.20210617.01.00";
    /**
     * Used in player only<br/>
     * Previous client version: 7.20190214<br/>
     * racyCheckOk, lockedSafetyMode - confirm age<br/>
     * contentCheckOk - ?
     */
    private static final String JSON_POST_DATA_TEMPLATE_WEB = String.format(
            "{\"context\":{\"client\":{\"clientName\":\"WEB\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"WATCH\",\"visitorData\":\"%%s\",%%s" +
            "\"thirdParty\":{\"embedUrl\":\"https://www.youtube.com/tv#/\"}," +
            "\"acceptRegion\":\"%%s\",\"acceptLanguage\":\"%%s\",\"utcOffsetMinutes\":\"%%s\"}," +
            "\"user\":{\"lockedSafetyMode\":false}}," +
            "\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}", CLIENT_VERSION_WEB);

    private static final String CHECK_PARAMS =
            "\"playbackContext\":{\"contentPlaybackContext\":{\"html5Preference\":\"HTML5_PREF_WANTS\"," +
            "\"lactMilliseconds\":\"60000\"," +
            "\"signatureTimestamp\":%s}}";
    private static final String CLICK_TRACKING =
            "\"clickTracking\":{\"clickTrackingParams\":\"%s\"},";
    private static final String VIDEO_ID = "\"videoId\":\"%s\",\"cpn\":\"%s\"";
    //private static final String VIDEO_ID = "\"videoId\":\"%s\"";

    public static String getVideoInfoQuery(String videoId) {
        return getVideoInfoQuery(videoId, null);
    }

    public static String getVideoInfoQuery(String videoId, String clickTrackingParams) {
        String videoIdTemplate = String.format(VIDEO_ID, videoId, AppService.instance().getClientPlaybackNonce());
        //String videoIdTemplate = String.format(VIDEO_ID, videoId);
        String checkParamsTemplate = String.format(CHECK_PARAMS, AppService.instance().getSignatureTimestamp());
        return createQueryWeb(checkParamsTemplate + "," + videoIdTemplate, clickTrackingParams);
    }

    private static String createQueryWeb(String template, String clickTrackingParams) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(JSON_POST_DATA_TEMPLATE_WEB,
                AppService.instance().getVisitorData(),
                clickTrackingParams != null ? String.format(CLICK_TRACKING, clickTrackingParams) : "",
                localeManager.getCountry(),
                localeManager.getLanguage(),
                localeManager.getUtcOffsetMinutes(),
                template);
    }
}
