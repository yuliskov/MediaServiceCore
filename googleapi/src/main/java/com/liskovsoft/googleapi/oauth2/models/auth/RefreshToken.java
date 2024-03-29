package com.liskovsoft.googleapi.oauth2.models.auth;

import com.liskovsoft.googleapi.common.converters.jsonpath.JsonPath;

public class RefreshToken extends AccessToken {
    @JsonPath("$.refresh_token")
    private String mRefreshToken;

    public String getRefreshToken() {
        return mRefreshToken;
    }
}
