package com.liskovsoft.googlecommon.common.models.auth;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

/**
 * https://developers.google.com/identity/protocols/oauth2/limited-input-device#access-granted
 */
public class AccessToken extends ErrorResponse {
    @JsonPath("$.access_token")
    private String mAccessToken;

    @JsonPath("$.expires_in")
    private int mExpiresIn;

    @JsonPath("$.refresh_token")
    private String mRefreshToken;

    @JsonPath("$.scope")
    private String mScope;

    @JsonPath("$.token_type")
    private String mTokenType;

    public String getAccessToken() {
        return mAccessToken;
    }

    public int getExpiresIn() {
        return mExpiresIn;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public String getScope() {
        return mScope;
    }

    public String getTokenType() {
        return mTokenType;
    }
}
