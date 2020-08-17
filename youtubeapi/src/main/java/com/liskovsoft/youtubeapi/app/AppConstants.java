package com.liskovsoft.youtubeapi.app;

import java.util.regex.Pattern;

public interface AppConstants {
    String USER_AGENT_SAMSUNG_1 = "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+";
    Pattern SIGNATURE_DECIPHER = Pattern.compile("function [_$A-Za-z]{2}");
    Pattern SIGNATURE_CLIENT_PLAYBACK_NONCE = Pattern.compile("function [_$A-Za-z]\\(\\)");
    String SCRIPTS_URL_BASE = "https://www.youtube.com";
    String API_KEY = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8";
    String FUNCTION_RANDOM_BYTES =
     "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}}";
}
