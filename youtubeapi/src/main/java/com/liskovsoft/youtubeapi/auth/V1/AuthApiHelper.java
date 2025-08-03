package com.liskovsoft.youtubeapi.auth.V1;

import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper;

public class AuthApiHelper {
    private static final String APP_SCOPE = "https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtube-paid-content";
    private static final String GRANT_TYPE_DEFAULT = "http://oauth.net/grant_type/device/1.0";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static String getAppScope() {
        return APP_SCOPE;
    }

    public static String getAccessGrantType() {
        return GRANT_TYPE_DEFAULT;
    }

    public static String getRefreshGrantType() {
        return GRANT_TYPE_REFRESH;
    }

    public static String getAccountsListQuery() {
        return PostDataHelper.createQueryTV("\"accountReadMask\":{\"returnOwner\":true}");
    }
}
