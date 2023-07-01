package com.liskovsoft.youtubeapi.common.helpers.tests;

import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.auth.V1.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;

public class TestHelpersV1 extends TestHelpersBase {
    private static String mAuthorization; // type: Bearer
    private static final String RAW_AUTH_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&refresh_token=1//0cP_RZyC7ratPCgYIARAAGAwSNwF-L9IrlOZa8kjE0SWrL8vIwSg_QqkxdHjq5EyiDitGclVNBtGovxijwz2R2VWGDHywf_IHlVM&grant_type=refresh_token";

    public static String getAuthorization() {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        AccessToken token = AuthService.instance().getAccessTokenRaw(RAW_AUTH_DATA);

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
