package com.liskovsoft.youtubeapi.app;

import java.util.regex.Pattern;

public interface AppConstants {
    String USER_AGENT_SAMSUNG_1 = "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+";
    Pattern DECIPHER_ORIGINAL_SIGNATURE = Pattern.compile("function [_$A-Za-z]{2}");
    String SCRIPTS_URL_BASE = "https://www.youtube.com";
}
