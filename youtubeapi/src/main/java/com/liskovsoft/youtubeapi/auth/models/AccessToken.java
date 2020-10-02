package com.liskovsoft.youtubeapi.auth.models;

import com.google.gson.annotations.SerializedName;

public class AccessToken extends ErrorResponse {
    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("expires_in")
    private int mExpiresIn;

    @SerializedName("scope")
    private String mScope;

    @SerializedName("token_type")
    private String mTokenType;

    public String getAccessToken() {
        return mAccessToken;
    }

    public int getExpiresIn() {
        return mExpiresIn;
    }

    public String getScope() {
        return mScope;
    }

    public String getTokenType() {
        return mTokenType;
    }
}
