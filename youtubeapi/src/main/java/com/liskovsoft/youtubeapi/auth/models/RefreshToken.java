package com.liskovsoft.youtubeapi.auth.models;

import com.google.gson.annotations.SerializedName;

public class RefreshToken extends AccessToken {
    @SerializedName("refresh_token")
    private String mRefreshToken;

    public String getRefreshToken() {
        return mRefreshToken;
    }
}
