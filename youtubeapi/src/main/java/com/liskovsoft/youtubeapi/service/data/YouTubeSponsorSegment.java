package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.youtubeapi.block.data.Segment;
import com.liskovsoft.youtubeapi.block.data.SegmentList;

import java.util.ArrayList;
import java.util.List;

public class YouTubeSponsorSegment implements SponsorSegment {
    private long mStartMs;
    private long mEndMs;
    private String mCategory;
    private String mAction;

    public static List<SponsorSegment> from(SegmentList segmentList) {
        if (segmentList == null || segmentList.getSegments() == null) {
            return null;
        }

        List<SponsorSegment> result = new ArrayList<>();

        for (Segment segment : segmentList.getSegments()) {
            if (segment == null || !isValidTime(segment.getStart()) || !isValidTime(segment.getEnd()) ||
                    segment.getEnd() < segment.getStart()) {
                continue;
            }
            // Older responses omit the action type. Other actions need their own playback handling.
            if (segment.getActionType() != null && !ACTION_SKIP.equals(segment.getActionType())) {
                continue;
            }

            YouTubeSponsorSegment sponsorSegment = new YouTubeSponsorSegment();
            sponsorSegment.mStartMs = (long) (segment.getStart() * 1_000);
            sponsorSegment.mEndMs = (long) (segment.getEnd() * 1_000);
            sponsorSegment.mCategory = segment.getCategory();
            sponsorSegment.mAction = segment.getActionType();
            result.add(sponsorSegment);
        }

        return result;
    }

    private static boolean isValidTime(float seconds) {
        return !Float.isNaN(seconds) && !Float.isInfinite(seconds) && seconds >= 0 &&
                (double) (seconds * 1_000) < Long.MAX_VALUE;
    }

    @Override
    public long getStartMs() {
        return mStartMs;
    }

    @Override
    public long getEndMs() {
        return mEndMs;
    }

    @Override
    public String getCategory() {
        return mCategory;
    }

    @Override
    public String getAction() {
        return mAction;
    }
}
