package com.liskovsoft.youtubeapi.common.helpers.tests;

import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;

public class TestHelpersV2 {
    private static String mAuthorization; // type: Bearer
    private static final String RAW_JSON_AUTH_DATA = "{\"client_id\":\"861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com\"," +
            "\"client_secret\":\"SboVhoG9s0rNafixCSGGKXAT\"," +
            "\"refresh_token\":\"1//0cXvGwadlFQ4ZCgYIARAAGAwSNwF-L9IrTZKtg_17mTcwUBMsJiSHXTnjWiW6A9Fddq9sHGfKZRIbKSh-7KgJ22ChDOTDtkbsmvU\"," +
            "\"grant_type\":\"refresh_token\"}";

    // In the morning
    public static final String VIDEO_ID_MUSIC = "5_ARibfCMhw";
    public static final String PLAYLIST_ID = "PLbl01QFpbBY3k5A8412DEqxwNuHBDudBz";
    public static final int PLAYLIST_VIDEO_INDEX = 1;

    // News One
    public static final String VIDEO_ID_1 = "x26FXCaUR7E";

    // Kvartal 95
    public static final String VIDEO_ID_2 = "XemGObKTF0o";

    // Creed
    public static final String VIDEO_ID_3 = "4oO-X3RkeLk";

    // Mafia Trailer
    public static final String VIDEO_ID_CAPTIONS = "s2lGEhSlOTY";

    // Strana: Yasno i ponyatno
    public static final String VIDEO_ID_SUBSCRIBED = "ftrpxWYDIJU";

    // 4K HDR 60FPS | Gemini Man
    public static final String VIDEO_ID_UNAVAILABLE = "vX2vsvdq8nw";

    // Cyberpunk
    public static final String VIDEO_ID_AGE_RESTRICTED = "8X2kIfS6fb8";

    // Lofi Girl
    public static final String VIDEO_ID_LIVE = "jfKfPfyJRdk";

    // Mathematik
    public static final String VIDEO_ID_MUSIC_2 = "0YEZiDtnbdA";

    // GECID.com
    public static final String CHANNEL_ID = "UC3PyIqYQ7lw7YKHRLqIvXlw";

    // Strana.ua
    public static final String CHANNEL_ID_2 = "UCDuSNexflm9nFKXLeMlqN2Q";

    // Hayls World
    public static final String CHANNEL_ID_UNSUBSCRIBED = "UCIxLxlan8q9WA7sjuq6LdTQ";

    public static String getAuthorization() {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        AccessToken token = AuthService.instance().getAccessTokenRaw(RAW_JSON_AUTH_DATA);

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
