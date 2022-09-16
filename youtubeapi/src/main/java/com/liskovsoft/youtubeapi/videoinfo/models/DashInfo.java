package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class DashInfo {
    /**
     * yt:earliestMediaSequence="1769487"
     */
    @RegExp("earliestMediaSequence=\"(.*?)\"")
    private String mStartSegmentNum;

    /**
     * minimumUpdatePeriod="PT5.000S"
     */
    @RegExp("minimumUpdatePeriod=\"PT(.*?)S\"")
    private String mSegmentDuration;

    /**
     * timeShiftBufferDepth="PT14400.000S"
     */
    @RegExp("timeShiftBufferDepth=\"PT(.*?)S\"")
    private String mTimeShiftBufferDepth;

    /**
     * availabilityStartTime="2022-09-15T07:31:41"
     */
    @RegExp("availabilityStartTime=\"(.*?)\"")
    private String mPublishedTime;

    public int getStartSegmentNum() {
        return 0;
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
