package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class DecipherFunctionResult {
    /**
     * Return JS decipher function as string
     */
    @RegExp("var [_$A-Za-z]{2}=\\{.*\\n.*\\n.*\\nfunction [_$A-Za-z]{2}\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}")
    private String mContent;

    public String getContent() {
        return mContent;
    }
}
