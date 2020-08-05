package com.liskovsoft.youtubeapi.app;

public interface AppConstants {
    String USER_AGENT_SAMSUNG_1 = "Mozilla/5.0 (Linux; Tizen 2.3; SmartHub; SMART-TV; SmartTV; U; Maple2012) AppleWebKit/538.1+ (KHTML, like Gecko) TV Safari/538.1+";
    String DECIPHER_CHUNK_PATTERN = "var [_$A-Za-z]{2}=\\{.*\\n.*\\n.*\\nfunction [_$A-Za-z]{2}\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}";
    String DECIPHER_FUNCTION_PATTERN = "function [_$A-Za-z]{2}";
}
