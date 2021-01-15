package com.liskovsoft.youtubeapi.lounge.models.info;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class ScreenInfo {
    @JsonPath("$.screens[*]")
    private List<ScreenItem> mScreens;

    public List<ScreenItem> getScreens() {
        return mScreens;
    }
}
