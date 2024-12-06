package com.liskovsoft.youtubeapi.common.helpers.tests;

import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.auth.V1.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;

public class TestHelpersV1 extends TestHelpersBase {
    private static String mAuthorization; // type: Bearer

    public static String getAuthorization() {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        AccessToken token = AuthService.instance().getAccessTokenRaw(ApiKeys.RAW_AUTH_DATA_V1);

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
        return OkHttpManager.instance(false).doHeadRequest(url) != null;
    }
}
