package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class BindData {
    /**
     * Match all, including the new line
     */
    @RegExp("[\\s\\S]*")
    private String mBindData;

    public String getBindData() {
        return mBindData;
    }
}
