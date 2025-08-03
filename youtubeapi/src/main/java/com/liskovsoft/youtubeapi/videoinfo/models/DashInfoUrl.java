package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.helpers.DateHelper;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;

public class DashInfoUrl implements DashInfo {
    private static final int MAX_DURATION_MS = 24 * 60 * 60 * 1_000;

    /**
     * yt:earliestMediaSequence="1769487"
     */
    @RegExp("yt\\:earliestMediaSequence=\"(.*?)\"")
    private String mEarliestMediaSequence;

    /**
     * startNumber="122185"
     */
    @RegExp("startNumber=\"(.*?)\"")
    private String mStartNumber; // usually the same as above but may be different on non-seekable streams

    /**
     * Period start="PT14185.270S"
     */
    //@RegExp("Period start=\"PT(.*?)S\"")
    private String mPeriodStartSec; // start segment in seconds

    /**
     * minimumUpdatePeriod="PT5.000S"
     */
    @RegExp("minimumUpdatePeriod=\"PT(.*?)S\"")
    private String mMinimumUpdatePeriodSec;

    /**
     * yt:mpdRequestTime="2022-09-16T23:04:59.728"
     */
    @RegExp("yt\\:mpdRequestTime=\"(.*?)\"")
    private String mMpdRequestTime; // use System.currentTime instead?

    /**
     * timeShiftBufferDepth="PT14400.000S"
     */
    @RegExp("timeShiftBufferDepth=\"PT(.*?)S\"")
    private String mTimeShiftBufferDepthSec; // not useful. always 4hrs

    /**
     * availabilityStartTime="2022-09-15T07:31:41"
     */
    @RegExp("availabilityStartTime=\"(.*?)\"")
    private String mAvailabilityStartTime; // not useful. truncated to 4hrs.

    /**
     * yt:segmentIngestTime="2022-09-25T11:40:58.028"
     */
    //@RegExp("yt:segmentIngestTime=\"(.*?)\"")
    private String mSegmentIngestTime; // start of period???

    private long mStartTimeMs = -1;
    private int mStartSegmentNum = -1;
    private int mEarliestMediaSequenceNum = -1;
    private int mSequenceStartNumber = -1;
    private long mMpdRequestTimeMs = -1;
    private long mPeriodStartTimeMs = -1;
    private long mAvailabilityStartTimeMs = -1;
    private long mSegmentIngestTimeMs = -1;
    private int mMinimumUpdatePeriodMs = -1;
    private int mTimeShiftBufferDepthMs = -1;

    @Override
    public int getSegmentDurationUs() {
        return getMinimumUpdatePeriodMs() * 1_000;
    }

    @Override
    public int getStartSegmentNum() {
        calculateTimings();

        return mStartSegmentNum;
    }

    @Override
    public long getStartTimeMs() {
        calculateTimings();

        return mStartTimeMs;
    }

    @Override
    public boolean isSeekable() {
        return getEarliestMediaSequenceNum() == getSequenceStartNumber();
    }

    private long getMpdRequestTimeMs() {
        if (mMpdRequestTimeMs == -1) {
            mMpdRequestTimeMs = DateHelper.toUnixTimeMs(mMpdRequestTime);
        }

        return mMpdRequestTimeMs;
    }

    private long getAvailabilityStartTimeMs() {
        if (mAvailabilityStartTimeMs == -1) {
            mAvailabilityStartTimeMs = DateHelper.toUnixTimeMs(mAvailabilityStartTime);
        }

        return mAvailabilityStartTimeMs;
    }

    private long getSegmentIngestTimeMs() {
        if (mSegmentIngestTimeMs == -1) {
            mSegmentIngestTimeMs = DateHelper.toUnixTimeMs(mSegmentIngestTime);
        }

        return mSegmentIngestTimeMs;
    }

    private long getPeriodStartTimeMs() {
        if (mPeriodStartTimeMs == -1) {
            if (getSequenceStartNumber() == 0) { // stream length < 4hrs
                mPeriodStartTimeMs = getAvailabilityStartTimeMs();
            } else if (!isSeekable()) { // non-seekable stream
                mPeriodStartTimeMs = getMpdRequestTimeMs();
            } else { // stream length > 4hrs
                mPeriodStartTimeMs = getMpdRequestTimeMs() - getTimeShiftBufferDepthMs();
            }
        }

        return mPeriodStartTimeMs;
    }

    private int getMinimumUpdatePeriodMs() {
        if (mMinimumUpdatePeriodMs == -1) {
            mMinimumUpdatePeriodMs = (int)(Helpers.parseFloat(mMinimumUpdatePeriodSec) * 1_000);
        }

        return mMinimumUpdatePeriodMs;
    }

    private int getTimeShiftBufferDepthMs() {
        if (mTimeShiftBufferDepthMs == -1) {
            mTimeShiftBufferDepthMs = (int)(Helpers.parseFloat(mTimeShiftBufferDepthSec) * 1_000);
        }

        return mTimeShiftBufferDepthMs;
    }

    private int getEarliestMediaSequenceNum() {
        if (mEarliestMediaSequenceNum == -1) {
            mEarliestMediaSequenceNum = Helpers.parseInt(mEarliestMediaSequence);
        }

        return mEarliestMediaSequenceNum;
    }

    private int getSequenceStartNumber() {
        if (mSequenceStartNumber == -1) {
            mSequenceStartNumber = Helpers.parseInt(mStartNumber);
        }

        return mSequenceStartNumber;
    }

    private void calculateTimings() {
        if (mStartTimeMs != -1 && mStartSegmentNum != -1) {
            return;
        }

        mStartTimeMs = getPeriodStartTimeMs();
        mStartSegmentNum = getSequenceStartNumber();

        int shortDurationMs = (int)(getMpdRequestTimeMs() - getPeriodStartTimeMs());

        int additionalDurationMs = MAX_DURATION_MS - shortDurationMs;

        if (additionalDurationMs > 0) {
            int additionalSegmentsNum = additionalDurationMs / getMinimumUpdatePeriodMs();

            int startSegmentNum = getSequenceStartNumber() - additionalSegmentsNum;

            if (startSegmentNum > 0) {
                mStartSegmentNum = startSegmentNum;
                //mStartTimeMs = getPeriodStartTimeMs() - (getSequenceStartNumber() * getMinimumUpdatePeriodMs());
                mStartTimeMs = getPeriodStartTimeMs() - additionalDurationMs;
            } else {
                mStartSegmentNum = 0;
                mStartTimeMs = getPeriodStartTimeMs() - ((long) getSequenceStartNumber() * getMinimumUpdatePeriodMs());
            }
        }
    }
}
