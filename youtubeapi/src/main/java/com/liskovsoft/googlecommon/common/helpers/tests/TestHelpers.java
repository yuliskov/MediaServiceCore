package com.liskovsoft.googlecommon.common.helpers.tests;

import com.liskovsoft.googleapi.oauth2.OAuth2Service;
import com.liskovsoft.googlecommon.common.models.auth.AccessToken;
import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;

public class TestHelpers extends TestHelpersBase {
    private static String mAuthorization; // type: Bearer
    private static String mOAuth2Authorization; // type: Bearer

    public static String getAuthorization() {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        AccessToken token = AuthService.instance().getAccessTokenRaw(ApiKeys.RAW_JSON_AUTH_DATA_V2);

        if (token == null) {
            throw new IllegalStateException("Token is null");
        }

        if (token.getAccessToken() == null) {
            throw new IllegalStateException("Authorization is null");
        }

        mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());

        return mAuthorization;
    }

    public static String getOAuth2Authorization() {
        if (mOAuth2Authorization != null) {
            return mOAuth2Authorization;
        }

        AccessToken token = OAuth2Service.instance().updateAccessToken(ApiKeys.REFRESH_TOKEN);

        if (token == null) {
            throw new IllegalStateException("Token is null");
        }

        if (token.getAccessToken() == null) {
            throw new IllegalStateException("Authorization is null");
        }

        mOAuth2Authorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());

        return mOAuth2Authorization;
    }

    public static boolean urlExists(String url) {
        // disable profiler because it could cause out of memory error
        return OkHttpManager.instance(false).doHeadRequest(url) != null;
    }
}
