package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class DashInfo {
    /**
     * yt:earliestMediaSequence="1769487"
     */
    @RegExp("yt\\:earliestMediaSequence=\"(.*?)\"")
    private String mEarliestMediaSequence;

    /**
     * Period start="PT14185.270S"
     */
    @RegExp("Period start=\"PT(.*?)S\"")
    private String mPeriodStartSec; // start segment in seconds

    /**
     * minimumUpdatePeriod="PT5.000S"
     */
    @RegExp("minimumUpdatePeriod=\"PT(.*?)S\"")
    private String mMinimumUpdatePeriod;

    /**
     * yt:mpdRequestTime="2022-09-16T23:04:59.728"
     */
    @RegExp("yt\\:mpdRequestTime=\"(.*?)\"")
    private String mMpdRequestTime; // use System.currentTime instead?

    /**
     * timeShiftBufferDepth="PT14400.000S"
     */
    //@RegExp("timeShiftBufferDepth=\"PT(.*?)S\"")
    private String mTimeShiftBufferDepthSec; // not useful. always 4hrs

    /**
     * availabilityStartTime="2022-09-15T07:31:41"
     */
    //@RegExp("availabilityStartTime=\"(.*?)\"")
    private String mAvailabilityStartTime; // not useful. truncated to 4hrs.

    private int mStartSegmentNum = -1;

    public int getStartSegmentNum() {
        if (mStartSegmentNum == -1) {
            mStartSegmentNum = 0;

            if (mEarliestMediaSequence != null) {
                int earliestSequence = Helpers.parseInt(mEarliestMediaSequence);
                if (earliestSequence > 0) {
                    mStartSegmentNum = earliestSequence;
                }
            }
        }

        return mStartSegmentNum;
    }

    public int getSegmentCount() {
        return 0;
    }

    public long getPublishedTimeMs() {
        return 0;
    }

    public int getSegmentDurationMs() {
        return 0;
    }
}
