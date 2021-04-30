package com.liskovsoft.youtubeapi.auth.V2;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class AuthParams {
    private static final String USER_CODE = "{\"client_id\":\"%s\",\"scope\":\"%s\"}";
    private static final String REFRESH_TOKEN = "{\"code\":\"%s\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"%s\"}";
    private static final String ACCESS_TOKEN = "{\"refresh_token\":\"%s\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"%s\"}";
    private static final String APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
    private static final String GRANT_TYPE_DEFAULT = "http://oauth.net/grant_type/device/1.0";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static String getAccountsListQuery() {
        return ServiceHelper.createQuery("\"accountReadMask\":{\"returnOwner\":true}");
    }

    public static String getUserCodeQuery(String clientId) {
        return String.format(USER_CODE, clientId, APP_SCOPE);
    }

    public static String getRefreshTokenQuery(String deviceCode, String clientId, String clientSecret) {
        return String.format(REFRESH_TOKEN, deviceCode, clientId, clientSecret, GRANT_TYPE_DEFAULT);
    }

    public static String getAccessTokenQuery(String refreshToken, String clientId, String clientSecret) {
        return String.format(ACCESS_TOKEN, refreshToken, clientId, clientSecret, GRANT_TYPE_REFRESH);
    }
}
