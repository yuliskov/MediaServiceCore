package com.liskovsoft.googleapi.oauth2;

import com.liskovsoft.googleapi.common.helpers.ServiceHelper;
import com.liskovsoft.googleapi.common.helpers.tests.ApiKeys;

/**
 * https://developers.google.com/identity/protocols/oauth2/limited-input-device#allowedscopes<br/>
 * https://developers.google.com/identity/protocols/oauth2/limited-input-device#step-4:-poll-googles-authorization-server
 */
class OAuth2ApiHelper {
    public static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:device_code";
    public static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static final String CLIENT_ID = ApiKeys.CLIENT_ID;
    public static final String CLIENT_SECRET = ApiKeys.CLIENT_SECRET;

    //public static final String DRIVE_SCOPE = "https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/drive.file";
    public static final String DRIVE_SCOPE = "https://www.googleapis.com/auth/drive.file";
    public static final String YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtube.readonly";
    public static final String SIGN_IN_DATA_SCOPE = "email openid profile";

    private static final String USER_CODE = "{\"client_id\":\"%s\",\"device_id\":\"%s\",\"model_name\":\"%s\",\"scope\":\"%s\"}";
    private static final String REFRESH_TOKEN = "{\"code\":\"%s\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"%s\"}";
    private static final String ACCESS_TOKEN = "{\"refresh_token\":\"%s\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"%s\"}";
    private static final String APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
    private static final String GRANT_TYPE_DEFAULT = "http://oauth.net/grant_type/device/1.0";
    private static final String MODEL_NAME = "ytlr::";

    public static String getAccountsListQuery() {
        return ServiceHelper.createQueryTV("\"accountReadMask\":{\"returnOwner\":true,\"returnBrandAccounts\":true,\"returnPersonaAccounts\":false}");
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
