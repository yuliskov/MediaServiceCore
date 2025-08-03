package com.liskovsoft.youtubeapi.videoinfo.models.formats;

import androidx.annotation.NonNull;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class AdaptiveVideoFormat extends VideoFormat {
    public static class Range {
        @JsonPath("$.start")
        private String start;
        @JsonPath("$.end")
        private String end;

        @NonNull
        public String toString() {
            if (start == null || end == null) {
                return "";
            }

            return String.format("%s-%s", start, end);
        }
    }

    @JsonPath("$.indexRange")
    private Range mIndexRange;
    @JsonPath("$.initRange")
    private Range mInitRange;
    private String mIndex;
    private String mInit;

    public String getIndex() {
        if (mIndex == null && mIndexRange != null) {
            mIndex = mIndexRange.toString();
        }

        return mIndex;
    }

    public void setIndex(String index) {
        mIndex = index;
    }

    public String getInit() {
        if (mInit == null && mInitRange != null) {
            mInit = mInitRange.toString();
        }

        return mInit;
    }

    public void setInit(String init) {
        mInit = init;
    }

    public Range getIndexRange() {
        return mIndexRange;
    }

    public Range getInitRange() {
        return mInitRange;
    }
}
