package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

public class SearchManagerParams {
    private static final String JSON_DATA_TEMPLATE = "{'context':{'client':{'clientName':'TVHTML5','clientVersion':'6.20180807','screenWidthPoints':1280," +
            "'screenHeightPoints':720,'screenPixelDensity':1,'theme':'CLASSIC','utcOffsetMinutes':120,'webpSupport':false," +
            "'animatedWebpSupport':false,'tvAppInfo':{'livingRoomAppMode':'LIVING_ROOM_APP_MODE_MAIN'," +
            "'appQuality':'TV_APP_QUALITY_LIMITED_ANIMATION','isFirstLaunch':true},'acceptRegion':'UA','deviceMake':'LG'," +
            "'deviceModel':'42LA660S-ZA','platform':'TV'},'request':{},'user':{'enableSafetyMode':false}},'query':'%s'," +
            "'supportsVoiceSearch':false}";
    private static final String JSON_CONTINUATION_DATA_TEMPLATE = "{'context':{'client':{'clientName':'TVHTML5','clientVersion':'6.20180807'," +
            "'screenWidthPoints':1280,'screenHeightPoints':720,'screenPixelDensity':1,'theme':'CLASSIC','utcOffsetMinutes':120,'webpSupport':false," +
            "'animatedWebpSupport':false,'tvAppInfo':{'livingRoomAppMode':'LIVING_ROOM_APP_MODE_MAIN'," +
            "'appQuality':'TV_APP_QUALITY_LIMITED_ANIMATION'},'acceptRegion':'UA','deviceMake':'LG','deviceModel':'42LA660S-ZA','platform':'TV'}," +
            "'request':{},'user':{'enableSafetyMode':false},'clickTracking':{'clickTrackingParams':'CAQQybcCIhMIyI2g4f3t4AIV13SyCh1LPghk'}}," +
            "'continuation':'%s'}";

    private static final String FIRST_SEARCH = "\"query\":\"%s\"";
    private static final String CONTINUATION_SEARCH = "\"continuation\":\"%s\"";

    public static String getSearchQuery(String searchText) {
        String search = String.format(FIRST_SEARCH, escape(searchText));
        return createQuery(search);
    }

    /**
     * Get data param for the next search
     * @param nextPageKey {@link SearchResult#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION_SEARCH, nextPageKey);
        return createQuery(continuation);
    }

    private static String escape(String text) {
        return text
                .replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\\"");
    }

    private static String createQuery(String template) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(AppConstants.JSON_POST_DATA_TEMPLATE, localeManager.getCountry(), localeManager.getLanguage(), template);
    }
}
