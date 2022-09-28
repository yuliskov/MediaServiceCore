package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class DashInfoFormat implements DashInfo {
    private static final int MAX_DURATION_MS = 24 * 60 * 60 * 1_000;

    /**
     * Sequence-Number: 309188
     */
    @RegExp("Sequence-Number: (\\d*)")
    private String mLastSegmentNum;

    /**
     * Ingestion-Walltime-Us: 1664281048614296
     */
    @RegExp("Ingestion-Walltime-Us: (\\d*)")
    private String mLastSegmentTimeUs;

    /**
     * NOTE: may be absent on some streams<br/>
     * First-Frame-Time-Us: 1664281048614296
     */
    //@RegExp("First-Frame-Time-Us: (\\d*)")
    //private String mFirstFrameTimeUs;

    /**
     * Stream-Duration-Us: 16642810
     */
    @RegExp("Stream-Duration-Us: (\\d*)")
    private String mStreamDurationUs;

    private int mSegmentDurationSec;
    private long mStartTimeMs = -1;
    private int mStartSegmentNum = -1;

    private int getLastSegmentNum() {
        if (mLastSegmentNum == null) {
            return 0;
        }

        return Integer.parseInt(mLastSegmentNum);
    }

    private long getLastSegmentTimeMs() {
        if (mLastSegmentTimeUs == null) {
            return 0;
        }

        return Long.parseLong(mLastSegmentTimeUs) / 1_000;
    }

    public void setSegmentDurationSec(int durationSec) {
        mSegmentDurationSec = durationSec;
    }

    @Override
    public long getStartTimeMs() {
        if (mStartTimeMs == -1) {
            int segmentNum = getLastSegmentNum() - getStartSegmentNum();
            mStartTimeMs = getLastSegmentTimeMs() - ((long) segmentNum * mSegmentDurationSec * 1_000);
        }

        return mStartTimeMs;
    }

    @Override
    public int getStartSegmentNum() {
        if (mStartSegmentNum == -1) {
            int maxSegmentCount = MAX_DURATION_MS / (mSegmentDurationSec * 1_000);
            int startSegment = getLastSegmentNum() - maxSegmentCount;
            mStartSegmentNum = Math.max(startSegment, 0);
        }

        return mStartSegmentNum;
    }

    @Override
    public boolean isSeekable() {
        return true;
    }
}
