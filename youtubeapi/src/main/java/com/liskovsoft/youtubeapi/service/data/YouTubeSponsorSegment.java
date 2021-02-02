package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.youtubeapi.block.data.Segment;
import com.liskovsoft.youtubeapi.block.data.SegmentList;

import java.util.ArrayList;
import java.util.List;

public class YouTubeSponsorSegment implements SponsorSegment {
    private long mStart;
    private long mEnd;
    private String mCategory;

    public static List<SponsorSegment> from(SegmentList segmentList) {
        if (segmentList == null || segmentList.getSegments() == null) {
            return null;
        }

        List<SponsorSegment> result = new ArrayList<>();

        for (Segment segment : segmentList.getSegments()) {
            YouTubeSponsorSegment sponsorSegment = new YouTubeSponsorSegment();
            sponsorSegment.mStart = (long) (segment.getStart() * 1_000);
            sponsorSegment.mEnd = (long) (segment.getEnd() * 1_000);
            sponsorSegment.mCategory = segment.getCategory();
            result.add(sponsorSegment);
        }

        return result;
    }

    @Override
    public long getStartMs() {
        return mStart;
    }

    @Override
    public long getEndMs() {
        return mEnd;
    }

    @Override
    public String getCategory() {
        return mCategory;
    }
}
