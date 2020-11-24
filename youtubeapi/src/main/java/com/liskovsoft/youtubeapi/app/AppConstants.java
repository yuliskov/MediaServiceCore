package com.liskovsoft.youtubeapi.app;

import java.util.regex.Pattern;

public interface AppConstants {
    // Samsung Smart TV
    String USER_AGENT_LEGACY = "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+";
    String USER_AGENT_LEGACY_2 = "Mozilla/5.0 (SMART-TV; Linux; Tizen 2.4.0) AppleWebkit/538.1 (KHTML, like Gecko) SamsungBrowser/1.1 TV Safari/538.1";
    // LG Smart TV 2013
    String USER_AGENT_MODERN = "Mozilla/5.0 (Unknown; Linux armv7l) AppleWebKit/537.1+ (KHTML, like Gecko) Safari/537.1+ LG Browser/6.00.00(+mouse+3D+SCREEN+TUNER; LGE; 42LA660S-ZA; 04.25.05; 0x00000001;); LG NetCast.TV-2013 /04.25.05 (LG, 42LA660S-ZA, wired)";
    String USER_AGENT_COBALT = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/40.13031-qa (unlike Gecko) Starboard/1";
    String USER_AGENT_COBALT_2 = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/120 (unlike Gecko) Starboard/1";
    String APP_USER_AGENT = USER_AGENT_COBALT;
    Pattern SIGNATURE_DECIPHER = Pattern.compile("function [_$A-Za-z]{2}");
    Pattern SIGNATURE_CLIENT_PLAYBACK_NONCE = Pattern.compile("\\nfunction [_$A-Za-z]{2}\\(\\)");
    String SCRIPTS_URL_BASE = "https://www.youtube.com";
    String API_KEY = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8";
    String FUNCTION_RANDOM_BYTES =
     "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}}";

    String CLIENT_VERSION = "7.20201122.00.00";

    /**
     * Used in browse, next, search<br/>
     * Previous client version: 7.20190214
     */
    String JSON_POST_DATA_TEMPLATE = String.format("{\"context\":{\"client\":{\"tvAppInfo\":{\"zylonLeftNav\":true},\"clientName\":\"TVHTML5\",\"clientVersion\":\"%s\"," +
            "\"webpSupport\":false,\"animatedWebpSupport\":true,\"acceptRegion\":\"%%s\",\"acceptLanguage\":\"%%s\",\"utcOffsetMinutes\":\"%%s\"}," +
            "\"user\":{\"enableSafetyMode\":false}},%%s}", CLIENT_VERSION);

    /**
     * Used when parsing video_info data
     */
    String VIDEO_INFO_JSON_CONTENT_PARAM = "player_response";
}
