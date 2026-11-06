package com.liskovsoft.youtubeapi.block.data;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.Collections;
import java.util.List;

public class SegmentList {
    @JsonPath("$[*]")
    private List<Segment> mSegments;

    public static SegmentList empty() {
        SegmentList result = new SegmentList();
        result.mSegments = Collections.emptyList();
        return result;
    }

    public List<Segment> getSegments() {
        return mSegments;
    }
}
