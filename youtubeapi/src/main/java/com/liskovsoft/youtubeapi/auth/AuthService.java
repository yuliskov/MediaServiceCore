package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.youtubeapi.auth.models.AccessTokenResult;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.auth.models.UserCodeResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

public class AuthService {
    private AuthManager mAuthManager;

    private AuthManager getAuthManager() {
        if (mAuthManager == null) {
            mAuthManager = RetrofitHelper.withGson(AuthManager.class);
        }

        return mAuthManager;
    }

    UserCodeResult getUserCode() {
        Call<UserCodeResult> wrapper = mAuthManager.getUserCode(
                AuthParams.getClientId(),
                AuthParams.getAppScope());
        return RetrofitHelper.get(wrapper);
    }

    AccessTokenResult getAuthToken(String deviceCode) {
        Call<AccessTokenResult> wrapper = mAuthManager.getAuthToken(
                deviceCode,
                AuthParams.getClientId(),
                AuthParams.getClientSecret(),
                AuthParams.getAccessGrantType());
        return RetrofitHelper.get(wrapper);
    }

    RefreshTokenResult getRefreshToken(String refreshToken) {
        Call<RefreshTokenResult> wrapper = mAuthManager.getRefreshToken(
                refreshToken,
                AuthParams.getClientId(),
                AuthParams.getClientSecret(),
                AuthParams.getRefreshGrantType());
        return RetrofitHelper.get(wrapper);
    }
}
