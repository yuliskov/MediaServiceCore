package com.liskovsoft.youtubeapi.videoinfo.models;

public abstract class DashInfoBase implements DashInfo {
    private static final int MAX_DURATION_MS = 24 * 60 * 60 * 1_000;

    private int mSegmentDurationUs = -1;
    private int mStartSegmentNum = -1;
    private long mStartTimeMs = -1;

    protected abstract int getLastSegmentNum();

    protected abstract long getLastSegmentTimeMs();

    protected abstract long getStreamDurationMs();

    @Override
    public int getSegmentDurationUs() {
        if (mSegmentDurationUs == -1) {
            //mSegmentDurationMs = (int) (getStreamDurationMs() / getLastSegmentNum());
            mSegmentDurationUs = (int)(getStreamDurationMs() / ((float) getLastSegmentNum()) * 1_000);
        }

        return mSegmentDurationUs;
    }

    //@Override
    //public long getStartTimeMs() {
    //    if (mStartTimeMs == -1) {
    //        int segmentCount = getLastSegmentNum() - getStartSegmentNum();
    //        mStartTimeMs = getLastSegmentTimeMs() - ((long) segmentCount * getSegmentDurationUs() / 1_000);
    //    }
    //
    //    return mStartTimeMs;
    //}

    @Override
    public long getStartTimeMs() {
        // stream ahead of time fix (in case system time incorrect)
        return System.currentTimeMillis() - Math.min(getStreamDurationMs(), MAX_DURATION_MS);
    }

    @Override
    public int getStartSegmentNum() {
        if (mStartSegmentNum == -1) {
            int maxSegmentCount = (int)(MAX_DURATION_MS * 1_000L / getSegmentDurationUs());
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
