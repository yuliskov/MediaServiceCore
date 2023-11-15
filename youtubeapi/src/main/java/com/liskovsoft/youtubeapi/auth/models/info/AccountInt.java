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

    @JsonPath("$.hasChannel")
    private boolean mHasChannel;

    @JsonPath("$.serviceEndpoint.selectActiveIdentityEndpoint.supportedTokens[0].pageIdToken.pageId")
    private String mPageIdToken;

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

    /**
     * Every YouTube account has a channel<br/>
     * This method could be served as a simple testing<br/>
     * whether the account is belongs to YouTube
     */
    public boolean hasChannel() {
        return mHasChannel;
    }

    /**
     * This token token used along with the access token to support restricted videos
     */
    public String getPageIdToken() {
        return mPageIdToken;
    }
}
