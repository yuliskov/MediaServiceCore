package com.liskovsoft.youtubeapi.auth.models;

import com.google.gson.annotations.SerializedName;

public class AccessToken extends ErrorResponse {
    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("expires_in")
    private int mExpiresIn;

    @SerializedName("refresh_token")
    private String mRefreshToken;

    @SerializedName("scope")
    private String mScope;

    @SerializedName("token_type")
    private String mTokenType;

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public int getExpiresIn() {
        return mExpiresIn;
    }
}
