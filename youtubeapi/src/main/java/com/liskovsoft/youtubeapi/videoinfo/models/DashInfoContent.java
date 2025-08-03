package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;

public class DashInfoContent extends DashInfoBase {
    /**
     * Sequence-Number: 309188 (inaccurate???)
     */
    @RegExp("Sequence-Number: (\\d*)")
    private String mLastSegmentNumStr;

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

    /**
     * Target-Duration-Us: 2000000 (inaccurate???)
     */
    //@RegExp("Target-Duration-Us: (\\d*)")
    //private String mSegmentDurationUs;

    private int mLastSegmentNum = -1;
    private long mLastSegmentTimeMs = -1;
    private long mStreamDurationMs = -1;

    @Override
    protected int getLastSegmentNum() {
        if (mLastSegmentNum == -1) {
            mLastSegmentNum = Integer.parseInt(mLastSegmentNumStr);
        }

        return mLastSegmentNum;
    }

    @Override
    protected long getLastSegmentTimeMs() {
        if (mLastSegmentTimeMs == -1) {
            mLastSegmentTimeMs = Long.parseLong(mLastSegmentTimeUs) / 1_000;
        }

        return mLastSegmentTimeMs;
    }

    @Override
    protected long getStreamDurationMs() {
        if (mStreamDurationMs == -1) {
            mStreamDurationMs = Long.parseLong(mStreamDurationUs) / 1_000;
        }

        return mStreamDurationMs;
    }
}
