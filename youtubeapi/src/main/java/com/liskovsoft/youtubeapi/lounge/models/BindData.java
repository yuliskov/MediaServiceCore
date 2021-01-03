package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class BindData {
    @RegExp(".*")
    private String mBindData;

    public String getBindData() {
        return mBindData;
    }
}
