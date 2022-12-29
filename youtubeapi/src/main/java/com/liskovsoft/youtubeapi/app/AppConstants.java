package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.helper.UserAgent;

public interface AppConstants {
    String APP_USER_AGENT = UserAgent.APP_USER_AGENT;
    /**
     * NOTE: Possible OOM exception!!! Uses more RAM because of decompression.
     */
    String ACCEPT_ENCODING_COMPRESSED = "gzip, deflate, br"; // NOTE: Old format live streams won't work with compression.
    /**
     * No compression. Optimal RAM usage!
     */
    String ACCEPT_ENCODING_IDENTITY = "identity";
    String ACCEPT_ENCODING_DEFAULT = ACCEPT_ENCODING_IDENTITY;
    String SCRIPTS_URL_BASE = "https://www.youtube.com";
    String API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8";
    String API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8";
    String API_KEY = API_KEY_NEW;
    String FUNCTION_RANDOM_BYTES =
     "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}}";
    
    // 7.20211013.10.00
    String CLIENT_VERSION_TV = "7.20220118.09.00";
    // 2.20211014.05.00-canary_control
    String CLIENT_VERSION_WEB = "2.20220119.01.00";
    // Special embed version (thanks for github thread)
    String CLIENT_VERSION_EMBED = "2.0";
    // 16.20
    String CLIENT_VERSION_ANDROID = "16.49";
    String CLIENT_NAME_TV = "TVHTML5";
    String CLIENT_NAME_WEB = "WEB";
    String CLIENT_NAME_EMBED = "TVHTML5_SIMPLY_EMBEDDED_PLAYER";
    String CLIENT_NAME_ANDROID = "ANDROID";
    String CLIENT_SCREEN_WATCH = "WATCH"; // won't play 18+ restricted videos
    String CLIENT_SCREEN_EMBED = "EMBED"; // no 18+ restriction but not all video embeddable, and no descriptions

    String JSON_POST_DATA_TEMPLATE = "{\"context\":{\"client\":{\"tvAppInfo\":{\"zylonLeftNav\":true},\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"webpSupport\":false,\"animatedWebpSupport\":true,\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
            "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}},\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}";

    /**
     * Used in browse, next, search<br/>
     * Previous client version: 7.20190214<br/>
     * racyCheckOk - confirm age<br/>
     * contentCheckOk - ?
     */
    String JSON_POST_DATA_TEMPLATE_TV = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_TV, CLIENT_VERSION_TV, CLIENT_SCREEN_WATCH);

    String JSON_POST_DATA_TEMPLATE_WEB = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_WEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_WATCH);

    String JSON_POST_DATA_TEMPLATE_ANDROID = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_ANDROID, CLIENT_VERSION_ANDROID, CLIENT_SCREEN_WATCH);

    /**
     * Used when parsing video_info data
     */
    String VIDEO_INFO_JSON_CONTENT_PARAM = "player_response";

    String VISITOR_COOKIE_NAME = "VISITOR_INFO1_LIVE";
}
