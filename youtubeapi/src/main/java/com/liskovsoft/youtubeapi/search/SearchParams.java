package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;

public class SearchParams {
    private static final String SEARCH_KEY = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8";
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

    public static String getSearchKey() {
        return SEARCH_KEY;
    }

    public static String getSearchQuery(String searchText) {
        return String.format(JSON_DATA_TEMPLATE, escape(searchText));
    }

    /**
     * Get data param for the next search
     * @param nextPageKey {@link SearchResult#getNextPageKey()}
     * @return data param
     */
    public static String getNextSearchQuery(String nextPageKey) {
        return String.format(JSON_CONTINUATION_DATA_TEMPLATE, nextPageKey);
    }

    private static String escape(String text) {
        return text
                .replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\\"");
    }
}
