package com.liskovsoft.youtubeapi.app;

public interface AppConstants {
    // Samsung Smart TV
    String USER_AGENT_LEGACY = "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+";
    String USER_AGENT_LEGACY_2 = "Mozilla/5.0 (SMART-TV; Linux; Tizen 2.4.0) AppleWebkit/538.1 (KHTML, like Gecko) SamsungBrowser/1.1 TV Safari/538.1";
    String USER_AGENT_LEGACY_3 = "Mozilla/5.0(SMART-TV; Linux; Tizen 4.0.0.2) AppleWebkit/605.1.15 (KHTML, like Gecko) SamsungBrowser/9.2 TV Safari/605.1.15";
    // LG Smart TV 2013
    String USER_AGENT_MODERN = "Mozilla/5.0 (Unknown; Linux armv7l) AppleWebKit/537.1+ (KHTML, like Gecko) Safari/537.1+ LG Browser/6.00.00(+mouse+3D+SCREEN+TUNER; LGE; 42LA660S-ZA; 04.25.05; 0x00000001;); LG NetCast.TV-2013 /04.25.05 (LG, 42LA660S-ZA, wired)";
    String USER_AGENT_COBALT = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/4.13031-qa (unlike Gecko) Starboard/1";
    String USER_AGENT_WEBOS = "Mozilla/5.0 (Web0S; Linux/SmartTV) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36 WebAppManager";
    String USER_AGENT_XBOX = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; Xbox; Xbox Series X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36 Edge/20.02";
    String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36";
    String APP_USER_AGENT = USER_AGENT_COBALT;
    String ACCEPT_ENCODING_COMPRESSED = "gzip, deflate, br"; // uses more RAM because of decompression
    String ACCEPT_ENCODING_IDENTITY = "identity";
    String ACCEPT_ENCODING_DEFAULT = ACCEPT_ENCODING_COMPRESSED;
    String SCRIPTS_URL_BASE = "https://www.youtube.com";
    String API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8";
    String API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8";
    String API_KEY = API_KEY_NEW;
    String FUNCTION_RANDOM_BYTES =
     "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}}";

    // 2.20210617.01.00
    // 7.20210622.10.00
    // 7.20210428.10.00
    // 7.20210411.10.00
    // 7.20201122.00.00
    String CLIENT_VERSION_TV = "7.20211013.10.00";
    String CLIENT_VERSION_WEB = "2.20211014.05.00-canary_control";
    String CLIENT_VERSION_ANDROID = "16.20";
    String CLIENT_NAME_TV = "TVHTML5";
    String CLIENT_NAME_WEB = "WEB";
    String CLIENT_NAME_ANDROID = "ANDROID";
    String CLIENT_SCREEN_WATCH = "WATCH"; // won't play 18+ restricted videos
    String CLIENT_SCREEN_EMBED = "EMBED"; // no 18+ restriction but not all video embeddable

    String JSON_POST_DATA_TEMPLATE = "{\"context\":{\"client\":{\"tvAppInfo\":{\"zylonLeftNav\":true},\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"webpSupport\":false,\"animatedWebpSupport\":true,\"acceptRegion\":\"%%s\",\"acceptLanguage\":\"%%s\"," +
            "\"utcOffsetMinutes\":\"%%s\"},\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}},\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}";

    /**
     * Used in browse, next, search<br/>
     * Previous client version: 7.20190214<br/>
     * racyCheckOk - confirm age<br/>
     * contentCheckOk - ?
     */
    String JSON_POST_DATA_TEMPLATE_TV = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_TV, CLIENT_VERSION_TV, CLIENT_SCREEN_EMBED);

    String JSON_POST_DATA_TEMPLATE_WEB = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_WEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_EMBED);

    /**
     * Used when parsing video_info data
     */
    String VIDEO_INFO_JSON_CONTENT_PARAM = "player_response";
}
