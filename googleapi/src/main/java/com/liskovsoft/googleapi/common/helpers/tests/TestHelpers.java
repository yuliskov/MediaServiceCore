package com.liskovsoft.googleapi.common.helpers.tests;

import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.googleapi.oauth2.OAuth2Service;
import com.liskovsoft.googleapi.oauth2.models.auth.AccessToken;

public class TestHelpers {
    private static String mAuthorization; // type: Bearer

    public static String getAuthorization() {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        AccessToken token = OAuth2Service.instance().updateAccessToken(ApiKeys.REFRESH_TOKEN);

        if (token == null) {
            throw new IllegalStateException("Token is null");
        }

        if (token.getAccessToken() == null) {
            throw new IllegalStateException("Authorization is null");
        }

        mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());

        return mAuthorization;
    }

    public static boolean urlExists(String url) {
        // disable profiler because it could cause out of memory error
        return OkHttpManager.instance(false).doGetRequest(url) != null;
    }
}
