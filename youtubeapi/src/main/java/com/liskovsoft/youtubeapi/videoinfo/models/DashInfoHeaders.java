package com.liskovsoft.youtubeapi.videoinfo.models;

import okhttp3.Headers;

public class DashInfoHeaders extends DashInfoBase {
    private static final String SEQ_NUM = "X-Sequence-Num";
    private static final String SEQ_NUM_ALT = "X-Head-Seqnum";
    private static final String STREAM_DUR_MS = "X-Head-Time-Millis";
    private static final String LAST_SEG_TIME_MS = "X-Walltime-Ms";
    private int mLastSegmentNum;
    private long mStreamDurationMs;
    private long mLastSegmentTimeMs;

    public DashInfoHeaders(Headers headers) {
        if (headers == null) {
            throw new IllegalStateException("Headers are null!");
        }

        String seqNum = headers.get(SEQ_NUM);
        mLastSegmentNum = Integer.parseInt(seqNum != null ? seqNum : headers.get(SEQ_NUM_ALT));
        mStreamDurationMs = Long.parseLong(headers.get(STREAM_DUR_MS));
        mLastSegmentTimeMs = Long.parseLong(headers.get(LAST_SEG_TIME_MS));
    }

    @Override
    protected int getLastSegmentNum() {
        return mLastSegmentNum;
    }

    @Override
    protected long getLastSegmentTimeMs() {
        return mLastSegmentTimeMs;
    }

    @Override
    protected long getStreamDurationMs() {
        return mStreamDurationMs;
    }
}
