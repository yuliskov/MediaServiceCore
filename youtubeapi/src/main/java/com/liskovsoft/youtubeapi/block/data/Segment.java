package com.liskovsoft.youtubeapi.block.data;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class Segment {
    public static final String CATEGORY_SPONSOR = "sponsor";
    public static final String CATEGORY_INTRO = "intro";
    public static final String CATEGORY_OUTRO = "outro";
    public static final String CATEGORY_INTERACTION = "interaction";
    public static final String CATEGORY_SELF_PROMO = "selfpromo";
    public static final String CATEGORY_MUSIC_OFF_TOPIC = "music_offtopic";

    @JsonPath("$.category")
    private String mCategory;

    @JsonPath("$.segment[0]")
    private float mStart;

    @JsonPath("$.segment[1]")
    private float mEnd;

    @JsonPath("$.UUID")
    private String mUuid;

    public String getCategory() {
        return mCategory;
    }

    public float getStart() {
        return mStart;
    }

    public float getEnd() {
        return mEnd;
    }

    public String getUuid() {
        return mUuid;
    }
}
