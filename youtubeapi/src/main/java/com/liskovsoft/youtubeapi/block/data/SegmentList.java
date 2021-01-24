package com.liskovsoft.youtubeapi.block.data;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SegmentList {
    @JsonPath("$[*]")
    private List<Segment> mSegments;

    public List<Segment> getSegments() {
        return mSegments;
    }
}
