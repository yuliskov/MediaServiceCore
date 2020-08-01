package com.liskovsoft.youtubeapi.auth;

public class AuthParams {
    private static final String CLIENT_ID = "861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com";
    // TODO: find out how to obtain unique client secret
    private static final String CLIENT_SECRET = "SboVhoG9s0rNafixCSGGKXAT";
    private static final String APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
    private static final String GRANT_TYPE_DEFAULT = "http://oauth.net/grant_type/device/1.0";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getAppScope() {
        return APP_SCOPE;
    }

    public static String getClientSecret() {
        return CLIENT_SECRET;
    }

    public static String getAccessGrantType() {
        return GRANT_TYPE_DEFAULT;
    }

    public static String getRefreshGrantType() {
        return GRANT_TYPE_REFRESH;
    }
}
