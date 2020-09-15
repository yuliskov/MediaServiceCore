package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.auth.models.AccessTokenResult;
import com.liskovsoft.youtubeapi.auth.models.UserCodeResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AuthService {
    private static final String TAG = AuthService.class.getSimpleName();
    private static AuthService sInstance;
    private final AuthManager mAuthManager;
    private static final int REFRESH_TOKEN_ATTEMPTS = 10;
    private static final long REFRESH_TOKEN_ATTEMPT_INTERVAL_MS = 7_000;

    private AuthService() {
        mAuthManager = RetrofitHelper.withGson(AuthManager.class);
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
    public UserCodeResult getUserCode() {
        Call<UserCodeResult> wrapper = mAuthManager.getUserCode(
                AuthParams.getClientId(),
                AuthParams.getAppScope());
        return RetrofitHelper.get(wrapper);
    }

    /**
     * Note, before calling this method user should apply the 'user code' on the page<br/>
     * <a href=https://youtube.com/activate>https://youtube.com/activate</a>
     * @param deviceCode the code contained inside the response of the method {@link #getUserCode()}
     * @return refresh token that should be stored inside the app registry for future use
     */
    public RefreshTokenResult getRefreshToken(String deviceCode) {
        Call<RefreshTokenResult> wrapper = mAuthManager.getRefreshToken(
                deviceCode,
                AuthParams.getClientId(),
                AuthParams.getClientSecret(),
                AuthParams.getAccessGrantType());
        return RetrofitHelper.get(wrapper);
    }

    /**
     * Returns temporal access token that should be refreshed after some period of time
     * @param refreshToken token obtained from previous method
     * @return temporal access token
     */
    public AccessTokenResult getAccessToken(String refreshToken) {
        Call<AccessTokenResult> wrapper = mAuthManager.getAccessToken(
                refreshToken,
                AuthParams.getClientId(),
                AuthParams.getClientSecret(),
                AuthParams.getRefreshGrantType());
        return RetrofitHelper.get(wrapper);
    }

    public AccessTokenResult getAccessTokenRaw(String rawAuthData) {
        Call<AccessTokenResult> wrapper = mAuthManager.getAccessToken(
                RequestBody.create(null, rawAuthData.getBytes()));
        return RetrofitHelper.get(wrapper);
    }

    public Observable<RefreshTokenResult> getRefreshTokenObserve(String deviceCode) {
        return Observable.create(emitter -> {
            RefreshTokenResult tokenResult = null;

            for (int i = 0; i < REFRESH_TOKEN_ATTEMPTS; i++) {
                Thread.sleep(REFRESH_TOKEN_ATTEMPT_INTERVAL_MS);

                tokenResult = getRefreshToken(deviceCode);

                if (tokenResult != null && tokenResult.getRefreshToken() != null) {
                    break;
                }
            }

            if (tokenResult != null && tokenResult.getRefreshToken() != null) {
                emitter.onNext(tokenResult);
                emitter.onComplete();
            } else {
                Log.e(TAG, "Error. Refresh token is empty!");
                emitter.onError(new IllegalStateException("Error. Refresh token is empty!"));
            }
        });
    }
}
