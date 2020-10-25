package com.liskovsoft.youtubeapi.auth.models.info;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class AccountInt {
    @JsonPath("$.accountName.simpleText")
    private String mName;

    @JsonPath("$.accountByline.simpleText")
    private String mEmail;

    @JsonPath("$.accountPhoto.thumbnails[*]")
    private List<Thumbnail> mAccountThumbnails;

    @JsonPath("$.isSelected")
    private boolean mIsSelected;

    @JsonPath("$.isDisabled")
    private boolean mIsDisabled;

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public List<Thumbnail> getThumbnails() {
        return mAccountThumbnails;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public boolean isDisabled() {
        return mIsDisabled;
    }
}
