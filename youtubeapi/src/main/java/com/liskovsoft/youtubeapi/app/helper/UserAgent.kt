package com.liskovsoft.youtubeapi.app.helper

import android.os.Build

object UserAgent {
    private const val USER_AGENT_SAMSUNG =
        "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+"
    private const val USER_AGENT_SAMSUNG_2 = "Mozilla/5.0 (SMART-TV; Linux; Tizen 2.4.0) AppleWebkit/538.1 (KHTML, like Gecko) SamsungBrowser/1.1 TV Safari/538.1"

    // Best (no lost history)
    private const val USER_AGENT_SAMSUNG_3 =
        "Mozilla/5.0(SMART-TV; Linux; Tizen 4.0.0.2) AppleWebkit/605.1.15 (KHTML, like Gecko) SamsungBrowser/9.2 TV Safari/605.1.15"

    // Bad. Doesn't contain 'Not recommend'/'Remove from history' context item
    private const val USER_AGENT_LG_2013 =
        "Mozilla/5.0 (Unknown; Linux armv7l) AppleWebKit/537.1+ (KHTML, like Gecko) Safari/537.1+ LG Browser/6.00.00(+mouse+3D+SCREEN+TUNER; LGE; 42LA660S-ZA; 04.25.05; 0x00000001;); LG NetCast.TV-2013 /04.25.05 (LG, 42LA660S-ZA, wired)"
    private const val USER_AGENT_COBALT = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/4.13031-qa (unlike Gecko) Starboard/1"
    private const val USER_AGENT_COBALT_2 = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/20.lts.2.0-gold (unlike Gecko) Starboard/11"

    // OK
    private const val USER_AGENT_WEBOS =
        "Mozilla/5.0 (Web0S; Linux/SmartTV) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36 WebAppManager"
    private const val USER_AGENT_XBOX =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; Xbox; Xbox Series X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36 Edge/20.02"
    private const val USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36"
    private const val USER_AGENT_FIRE_TV_1 =
        "Mozilla/5.0 (Linux armeabi-v7a; Android 7.1.2; Fire OS 6.0) Cobalt/22.lts.3.306369-gold (unlike Gecko) v8/8.8.278.8-jit gles Starboard/13, Amazon_ATV_mediatek8695_2019/NS6294 (Amazon, AFTMM, Wireless) com.amazon.firetv.youtube/22.3.r2.v66.0"
    private const val USER_AGENT_ATV_1 =
        "Mozilla/5.0 (Linux armeabi-v7a; Android 7.1.2) Cobalt/23.lts.2.309559-gold (unlike Gecko) v8/8.8.278.8-jit gles Starboard/14, Amazon_ATV_mt8695_0/NS6294 (Amazon, AFTMM) com.google.android.youtube.tv/3.02.006"


    private const val COBALT_VER = "23.lts.2.309559-gold"
    private const val V8_VER = "8.8.278.8-jit"
    private const val STARBOARD_VER = "14"
    private const val APK_VER = "3.02.006"
    private val USER_AGENT_ATV_COMBINED =
        "Mozilla/5.0 (Linux ${Build.CPU_ABI}; Android ${Build.VERSION.RELEASE}) Cobalt/$COBALT_VER (unlike Gecko) v8/$V8_VER gles Starboard/$STARBOARD_VER, ${Build.BRAND}_ATV_${Build.HARDWARE}_0/${Build.ID} (${Build.BRAND}, ${Build.MODEL}) com.google.android.youtube.tv/$APK_VER"

    @JvmField
    val APP_USER_AGENT = USER_AGENT_ATV_COMBINED
}