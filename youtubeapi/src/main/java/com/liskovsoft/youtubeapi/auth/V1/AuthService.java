package com.liskovsoft.youtubeapi.auth.V1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.models.auth.AccessToken;
import com.liskovsoft.googlecommon.common.models.auth.UserCode;
import com.liskovsoft.googlecommon.common.models.auth.info.AccountInt;
import com.liskovsoft.googlecommon.common.models.auth.info.AccountsList;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.util.List;

public class AuthService {
    private static final String TAG = AuthService.class.getSimpleName();
    private static AuthService sInstance;
    private final AuthApi mAuthApi;
    private static final int REFRESH_TOKEN_ATTEMPTS = 20;
    private static final long REFRESH_TOKEN_ATTEMPT_INTERVAL_MS = 3_000;
    private final AppService mAppService;

    private AuthService() {
        mAuthApi = RetrofitHelper.create(AuthApi.class);
        mAppService = AppService.instance();
    }

    public static AuthService instance() {
        if (sInstance == null) {
            sInstance = new AuthService();
        }

        return sInstance;
    }

    /**
     * Returns user code that user should apply on the page<br/>
     * <a href=https://youtube.com/activate>https://youtube.com/activate</a>
     * @return response with user code and device code
     */
    public UserCode getUserCode() {
        Call<UserCode> wrapper = mAuthApi.getUserCode(
                mAppService.getClientId(),
                AuthApiHelper.getAppScope());
        return RetrofitHelper.get(wrapper);
    }

    /**
     * Note, before calling this method user should apply the 'user code' on the page<br/>
     * <a href=https://youtube.com/activate>https://youtube.com/activate</a>
     * @param deviceCode the code contained inside the response of the method {@link #getUserCode()}
     * @return refresh token that should be stored inside the app registry for future use
     */
    public AccessToken getAccessToken(String deviceCode) {
        Call<AccessToken> wrapper = mAuthApi.getAccessToken(
                deviceCode,
                mAppService.getClientId(),
                mAppService.getClientSecret(),
                AuthApiHelper.getAccessGrantType());
        return RetrofitHelper.get(wrapper);
    }

    /**
     * Returns temporal access token that should be refreshed after some period of time
     * @param refreshToken token obtained from previous method
     * @return temporal access token
     */
    public AccessToken updateAccessToken(String refreshToken) {
        Call<AccessToken> wrapper = mAuthApi.updateAccessToken(
                refreshToken,
                mAppService.getClientId(),
                mAppService.getClientSecret(),
                AuthApiHelper.getRefreshGrantType());
        return RetrofitHelper.get(wrapper);
    }

    public AccessToken getAccessTokenRaw(String rawAuthData) {
        Call<AccessToken> wrapper = mAuthApi.updateAccessToken(
                RequestBody.create(null, rawAuthData.getBytes()));
        return RetrofitHelper.get(wrapper);
    }

    public AccessToken getRefreshTokenWait(String deviceCode) throws InterruptedException {
        AccessToken tokenResult = null;

        for (int i = 0; i < REFRESH_TOKEN_ATTEMPTS; i++) {
            Thread.sleep(REFRESH_TOKEN_ATTEMPT_INTERVAL_MS);

            tokenResult = getAccessToken(deviceCode);

            if (tokenResult != null && tokenResult.getRefreshToken() != null) {
                break;
            }
        }

        if (tokenResult != null && tokenResult.getRefreshToken() != null) {
            return tokenResult;
        } else {
            String msg = String.format("Error. Refresh token is empty!\nDebug data: device code: %s, client id: %s, client secret: %s\nError msg: %s",
                    deviceCode,
                    mAppService.getClientId(),
                    mAppService.getClientSecret(),
                    tokenResult != null ? tokenResult.getError() : "");

            Log.e(TAG, msg);
            throw new IllegalStateException(msg);
        }
    }

    public List<AccountInt> getAccounts(String authorization) {
        Call<AccountsList> wrapper = mAuthApi.getAccountsList(AuthApiHelper.getAccountsListQuery(), authorization);

        AccountsList accountsList = RetrofitHelper.get(wrapper);

        return accountsList != null ? accountsList.getAccounts() : null;
    }
}
