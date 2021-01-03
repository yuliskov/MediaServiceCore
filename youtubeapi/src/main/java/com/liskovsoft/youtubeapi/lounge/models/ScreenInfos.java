package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class ScreenInfos {
    @JsonPath("$.screens[*]")
    private List<Screen> mScreens;

    public List<Screen> getScreens() {
        return mScreens;
    }
}
