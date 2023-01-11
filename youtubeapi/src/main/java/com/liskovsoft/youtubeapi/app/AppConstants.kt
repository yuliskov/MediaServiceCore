package com.liskovsoft.youtubeapi.app

import android.os.Build

object AppConstants {
    private const val COBALT_VER = "23.lts.2.309559-gold"
    private const val V8_VER = "8.8.278.8-jit"
    private const val STARBOARD_VER = "14"
    private const val APK_VER = "3.02.006"

    /**
     * NOTE: Possible OOM exception!!! Uses more RAM because of decompression.
     */
    private const val ACCEPT_ENCODING_COMPRESSED = "gzip, deflate, br" // NOTE: Old format live streams won't work with compression.

    /**
     * No compression. Optimal RAM usage!
     */
    private const val ACCEPT_ENCODING_IDENTITY = "identity"

    private const val API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8"
    private const val API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"

    private const val JSON_POST_DATA_TEMPLATE = "{\"context\":{\"client\":{\"tvAppInfo\":{\"zylonLeftNav\":true},\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"webpSupport\":false,\"animatedWebpSupport\":true,\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
            "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}},\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"

    const val USER_AGENT_SAMSUNG =
        "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+"
    const val USER_AGENT_SAMSUNG_2 = "Mozilla/5.0 (SMART-TV; Linux; Tizen 2.4.0) AppleWebkit/538.1 (KHTML, like Gecko) SamsungBrowser/1.1 TV Safari/538.1"

    // Best (no lost history)
    const val USER_AGENT_SAMSUNG_3 =
        "Mozilla/5.0(SMART-TV; Linux; Tizen 4.0.0.2) AppleWebkit/605.1.15 (KHTML, like Gecko) SamsungBrowser/9.2 TV Safari/605.1.15"

    // Bad. Doesn't contain 'Not recommend'/'Remove from history' context item
    const val USER_AGENT_LG_2013 =
        "Mozilla/5.0 (Unknown; Linux armv7l) AppleWebKit/537.1+ (KHTML, like Gecko) Safari/537.1+ LG Browser/6.00.00(+mouse+3D+SCREEN+TUNER; LGE; 42LA660S-ZA; 04.25.05; 0x00000001;); LG NetCast.TV-2013 /04.25.05 (LG, 42LA660S-ZA, wired)"
    const val USER_AGENT_COBALT = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/4.13031-qa (unlike Gecko) Starboard/1"
    const val USER_AGENT_COBALT_2 = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/20.lts.2.0-gold (unlike Gecko) Starboard/11"

    // OK
    const val USER_AGENT_WEBOS =
        "Mozilla/5.0 (Web0S; Linux/SmartTV) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36 WebAppManager"
    const val USER_AGENT_XBOX =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; Xbox; Xbox Series X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36 Edge/20.02"
    const val USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36"
    const val USER_AGENT_FIRE_TV_1 =
        "Mozilla/5.0 (Linux armeabi-v7a; Android 7.1.2; Fire OS 6.0) Cobalt/22.lts.3.306369-gold (unlike Gecko) v8/8.8.278.8-jit gles Starboard/13, Amazon_ATV_mediatek8695_2019/NS6294 (Amazon, AFTMM, Wireless) com.amazon.firetv.youtube/22.3.r2.v66.0"
    const val USER_AGENT_ATV_1 =
        "Mozilla/5.0 (Linux armeabi-v7a; Android 7.1.2) Cobalt/23.lts.2.309559-gold (unlike Gecko) v8/8.8.278.8-jit gles Starboard/14, Amazon_ATV_mt8695_0/NS6294 (Amazon, AFTMM) com.google.android.youtube.tv/3.02.006"

    @JvmField
    val USER_AGENT_ATV_COMBINED =
        "Mozilla/5.0 (Linux ${Build.CPU_ABI}; Android ${Build.VERSION.RELEASE}) Cobalt/$COBALT_VER (unlike Gecko) v8/$V8_VER gles Starboard/$STARBOARD_VER, ${Build.BRAND}_ATV_${Build.HARDWARE}_0/${Build.ID} (${Build.BRAND}, ${Build.MODEL}) com.google.android.youtube.tv/$APK_VER"

    @JvmField
    val APP_USER_AGENT = USER_AGENT_ATV_COMBINED

    const val ACCEPT_ENCODING_DEFAULT = ACCEPT_ENCODING_IDENTITY
    const val SCRIPTS_URL_BASE = "https://www.youtube.com"
    const val API_KEY = API_KEY_NEW
    const val FUNCTION_RANDOM_BYTES =
        "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}};"

    // 7.20211013.10.00
    const val CLIENT_VERSION_TV = "7.20220118.09.00"

    // 2.20211014.05.00-canary_control
    const val CLIENT_VERSION_WEB = "2.20220119.01.00"

    // Special embed version (thanks for github thread)
    const val CLIENT_VERSION_EMBED = "2.0"

    // 16.20
    const val CLIENT_VERSION_ANDROID = "16.49"
    const val CLIENT_NAME_TV = "TVHTML5"
    const val CLIENT_NAME_WEB = "WEB"
    const val CLIENT_NAME_EMBED = "TVHTML5_SIMPLY_EMBEDDED_PLAYER"
    const val CLIENT_NAME_ANDROID = "ANDROID"
    const val CLIENT_SCREEN_WATCH = "WATCH" // won't play 18+ restricted videos
    const val CLIENT_SCREEN_EMBED = "EMBED" // no 18+ restriction but not all video embeddable, and no descriptions

    /**
     * Used in browse, next, search<br></br>
     * Previous client version: 7.20190214<br></br>
     * racyCheckOk - confirm age<br></br>
     * contentCheckOk - ?
     */
    @JvmField
    val JSON_POST_DATA_TEMPLATE_TV = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_TV, CLIENT_VERSION_TV, CLIENT_SCREEN_WATCH)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_WEB = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_WEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_WATCH)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_ANDROID = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_ANDROID, CLIENT_VERSION_ANDROID, CLIENT_SCREEN_WATCH)

    /**
     * Used when parsing video_info data
     */
    const val VIDEO_INFO_JSON_CONTENT_PARAM = "player_response"

    const val VISITOR_COOKIE_NAME = "VISITOR_INFO1_LIVE"
}