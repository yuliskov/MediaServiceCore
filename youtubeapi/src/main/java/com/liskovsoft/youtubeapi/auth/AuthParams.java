package com.liskovsoft.youtubeapi.auth;

public class AuthParams {
    private static final String DEFAULT_APP_CLIENT_ID = "861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com";
    private static final String DEFAULT_APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
    private static final String DEFAULT_APP_CLIENT_SECRET = "SboVhoG9s0rNafixCSGGKXAT";
    private static final String DEFAULT_APP_GRANT_TYPE = "http://oauth.net/grant_type/device/1.0";

    public static String getClientId() {
        return DEFAULT_APP_CLIENT_ID;
    }

    public static String getAppScope() {
        return DEFAULT_APP_SCOPE;
    }

    public static String getClientSecret() {
        return DEFAULT_APP_CLIENT_SECRET;
    }

    public static String getGrantType() {
        return DEFAULT_APP_GRANT_TYPE;
    }
}
