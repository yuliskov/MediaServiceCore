package com.liskovsoft.youtubeapi.lounge.models.info;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

public class PlaylistItem {
    @JsonPath("$.title")
    private String mTitle;

    @JsonPath("$.duration")
    private String mDurationText;

    @JsonPath("$.length_seconds")
    private String mLengthSeconds;

    @JsonPath("$.thumbnail")
    private String mThumbnailUrl;

    @JsonPath("$.encrypted_id")
    private String mEncryptedId;

    public String getTitle() {
        return mTitle;
    }

    public String getDurationText() {
        return mDurationText;
    }

    public String getLengthSeconds() {
        return mLengthSeconds;
    }

    public String getThumbnailUrl() {
        return ServiceHelper.tidyUrl(mThumbnailUrl);
    }

    public String getEncryptedId() {
        return mEncryptedId;
    }
}
