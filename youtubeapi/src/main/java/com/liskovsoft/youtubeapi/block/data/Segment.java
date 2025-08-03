package com.liskovsoft.youtubeapi.block.data;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class Segment {
    @JsonPath("$.category")
    private String mCategory;

    @JsonPath("$.actionType")
    private String mActionType;

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

    public String getActionType() {
        return mActionType;
    }
}
