package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.youtubeapi.auth.models.AccessTokenResult;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.auth.models.UserCodeResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AuthService {
    private static AuthService sInstance;
    private AuthManager mAuthManager;

    private AuthService() {
    }

    public static AuthService instance() {
        if (sInstance == null) {
            sInstance = new AuthService();
        }

        return sInstance;
    }

    private AuthManager getAuthManager() {
        if (mAuthManager == null) {
            mAuthManager = RetrofitHelper.withGson(AuthManager.class);
        }

        return mAuthManager;
    }

    public UserCodeResult getUserCode() {
        Call<UserCodeResult> wrapper = mAuthManager.getUserCode(
                AuthParams.getClientId(),
                AuthParams.getAppScope());
        return RetrofitHelper.get(wrapper);
    }

    public AccessTokenResult getAuthToken(String deviceCode) {
        Call<AccessTokenResult> wrapper = mAuthManager.getAuthToken(
                deviceCode,
                AuthParams.getClientId(),
                AuthParams.getClientSecret(),
                AuthParams.getAccessGrantType());
        return RetrofitHelper.get(wrapper);
    }

    public RefreshTokenResult getRefreshToken(String refreshToken) {
        Call<RefreshTokenResult> wrapper = mAuthManager.getRefreshToken(
                refreshToken,
                AuthParams.getClientId(),
                AuthParams.getClientSecret(),
                AuthParams.getRefreshGrantType());
        return RetrofitHelper.get(wrapper);
    }

    public RefreshTokenResult getRawRefreshToken(String rawAuthData) {
        Call<RefreshTokenResult> wrapper = mAuthManager.getRefreshToken(
                RequestBody.create(null, rawAuthData.getBytes()));
        return RetrofitHelper.get(wrapper);
    }
}
