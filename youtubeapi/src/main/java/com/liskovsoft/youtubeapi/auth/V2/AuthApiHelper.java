package com.liskovsoft.youtubeapi.auth.V2;

import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

public class AuthApiHelper {
    private static final String USER_CODE = "{\"client_id\":\"%s\",\"device_id\":\"%s\",\"model_name\":\"%s\",\"scope\":\"%s\"}";
    private static final String REFRESH_TOKEN = "{\"code\":\"%s\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"%s\"}";
    private static final String ACCESS_TOKEN = "{\"refresh_token\":\"%s\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"%s\"}";
    private static final String APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
    private static final String GRANT_TYPE_DEFAULT = "http://oauth.net/grant_type/device/1.0";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";
    private static final String MODEL_NAME = "ytlr::";

    public static String getAccountsListQuery() {
        return PostDataHelper.createQueryTV("\"accountReadMask\":{\"returnOwner\":true,\"returnBrandAccounts\":true,\"returnPersonaAccounts\":false}");
    }

    public static String getUserCodeQuery(String clientId) {
        return String.format(USER_CODE, clientId, MediaServiceData.instance().getDeviceId(), MODEL_NAME, APP_SCOPE);
    }

    public static String getRefreshTokenQuery(String deviceCode, String clientId, String clientSecret) {
        return String.format(REFRESH_TOKEN, deviceCode, clientId, clientSecret, GRANT_TYPE_DEFAULT);
    }

    public static String getAccessTokenQuery(String refreshToken, String clientId, String clientSecret) {
        return String.format(ACCESS_TOKEN, refreshToken, clientId, clientSecret, GRANT_TYPE_REFRESH);
    }
}
