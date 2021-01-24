package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.youtubeapi.block.data.Segment;
import com.liskovsoft.youtubeapi.block.data.SegmentList;

import java.util.ArrayList;
import java.util.List;

public class YouTubeSponsorSegment implements SponsorSegment {
    private float mStart;
    private float mEnd;

    public static List<SponsorSegment> from(SegmentList segmentList) {
        if (segmentList == null || segmentList.getSegments() == null) {
            return null;
        }

        List<SponsorSegment> result = new ArrayList<>();

        for (Segment segment : segmentList.getSegments()) {
            YouTubeSponsorSegment sponsorSegment = new YouTubeSponsorSegment();
            sponsorSegment.mStart = segment.getStart();
            sponsorSegment.mEnd = segment.getEnd();
            result.add(sponsorSegment);
        }

        return result;
    }

    @Override
    public float getStart() {
        return mStart;
    }

    @Override
    public float getEnd() {
        return mEnd;
    }
}
