package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.helpers.DateHelper;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class DashInfo {
    private static final int MAX_DURATION_MS = 12 * 60 * 60 * 1_000;

    /**
     * yt:earliestMediaSequence="1769487"
     */
    @RegExp("yt\\:earliestMediaSequence=\"(.*?)\"")
    private String mEarliestMediaSequence;

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

    private long mStartTimeMs = -1;
    private int mStartSegmentNum = -1;
    private long mMpdRequestTimeMs = -1;
    private long mAvailabilityStartTimeMs = -1;
    private int mMinimumUpdatePeriodMs = -1;
    private int mEarliestMediaSequenceNum = -1;
    private int mTimeShiftBufferDepthMs = -1;

    public int getStartSegmentNum() {
        calculateTimings();

        return mStartSegmentNum;
    }

    public long getStartTimeMs() {
        calculateTimings();

        return mStartTimeMs;
    }

    private long getMpdRequestTimeMs() {
        if (mMpdRequestTimeMs == -1) {
            mMpdRequestTimeMs = DateHelper.toUnixTimeMs(mMpdRequestTime);
        }

        return mMpdRequestTimeMs;
    }

    private long getAvailabilityStartTimeMs() {
        if (mAvailabilityStartTimeMs == -1) {
            if (getEarliestMediaSequenceNum() == 0) {
                mAvailabilityStartTimeMs = DateHelper.toUnixTimeMs(mAvailabilityStartTime);
            } else {
                mAvailabilityStartTimeMs = getMpdRequestTimeMs() - getTimeShiftBufferDepthMs();
            }
        }

        return mAvailabilityStartTimeMs;
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

    private void calculateTimings() {
        if (mStartTimeMs != -1 && mStartSegmentNum != -1) {
            return;
        }

        mStartTimeMs = getAvailabilityStartTimeMs();
        mStartSegmentNum = getEarliestMediaSequenceNum();

        int shortDurationMs = (int)(getMpdRequestTimeMs() - getAvailabilityStartTimeMs());

        int additionalDurationMs = MAX_DURATION_MS - shortDurationMs;

        if (additionalDurationMs > 0) {
            int additionalSegmentsNum = additionalDurationMs / getMinimumUpdatePeriodMs();

            mStartSegmentNum = getEarliestMediaSequenceNum() - additionalSegmentsNum;

            if (mStartSegmentNum > 0) {
                mStartTimeMs = getAvailabilityStartTimeMs() - additionalDurationMs;
            } else {
                mStartSegmentNum = 0;
                mStartTimeMs = getAvailabilityStartTimeMs() - (getEarliestMediaSequenceNum() * getMinimumUpdatePeriodMs());
            }
        }
    }
}
