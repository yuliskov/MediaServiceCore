package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;

public class TestHelpers {
    private static String mAuthorization; // type: Bearer
    private static final String RAW_AUTH_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&refresh_token=1%2F%2F0ca0zVzDYAcWCCgYIARAAGAwSNwF-L9IrCkqjDqPyup8sXFA40LiTGh-8yW2jM4lLBOXyhcRa07fDM35jM-dU80PUemu1u1F8-AY&grant_type=refresh_token";

    // Mafia: Definitive Edition - Official Story Trailer | Summer of Gaming 2020
    public static final String VIDEO_ID_SIMPLE = "s2lGEhSlOTY";

    // LINDEMANN - Mathematik ft. Haftbefehl (Official Video)
    public static final String VIDEO_ID_CIPHERED = "0YEZiDtnbdA";

    // GECID.com
    public static final String CHANNEL_ID_SIMPLE = "UC3PyIqYQ7lw7YKHRLqIvXlw";

    // NEWS ONE
    public static final String VIDEO_ID_LIVE = "3e0FsU1N6OQ";

    public static String getAuthorization() {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        RefreshTokenResult token = AuthService.instance().getRawRefreshToken(RAW_AUTH_DATA);

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
        return OkHttpManager.instance(false).doGetOkHttpRequest(url) != null;
    }
}
