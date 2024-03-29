package com.liskovsoft.driveapi.common.models.items;

import com.liskovsoft.driveapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.driveapi.common.helpers.YouTubeHelper;

public class Thumbnail {
    @JsonPath("$.url")
    private String url;
    @JsonPath("$.width")
    private int width;
    @JsonPath("$.height")
    private int height;

    public String getUrl() {
        // Glide: fix urls without prefix
        if (url != null && url.startsWith("//")) {
            url = "https:" + url;
        }

        url = YouTubeHelper.avatarBlockFix(url);

        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
