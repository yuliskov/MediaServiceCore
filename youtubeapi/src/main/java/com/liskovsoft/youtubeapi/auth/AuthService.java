package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.models.auth.RefreshToken;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.auth.UserCode;
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
    private final AppService mAppService;

    private AuthService() {
        mAuthManager = RetrofitHelper.withGson(AuthManager.class);
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
        Call<UserCode> wrapper = mAuthManager.getUserCode(
                mAppService.getClientId(),
                AuthParams.getAppScope());
        return RetrofitHelper.get(wrapper);
    }

    /**
     * Note, before calling this method user should apply the 'user code' on the page<br/>
     * <a href=https://youtube.com/activate>https://youtube.com/activate</a>
     * @param deviceCode the code contained inside the response of the method {@link #getUserCode()}
     * @return refresh token that should be stored inside the app registry for future use
     */
    public RefreshToken getRefreshToken(String deviceCode) {
        Call<RefreshToken> wrapper = mAuthManager.getRefreshToken(
                deviceCode,
                mAppService.getClientId(),
                mAppService.getClientSecret(),
                AuthParams.getAccessGrantType());
        return RetrofitHelper.get(wrapper);
    }

    /**
     * Returns temporal access token that should be refreshed after some period of time
     * @param refreshToken token obtained from previous method
     * @return temporal access token
     */
    public AccessToken getAccessToken(String refreshToken) {
        Call<AccessToken> wrapper = mAuthManager.getAccessToken(
                refreshToken,
                mAppService.getClientId(),
                mAppService.getClientSecret(),
                AuthParams.getRefreshGrantType());
        return RetrofitHelper.get(wrapper);
    }

    public AccessToken getAccessTokenRaw(String rawAuthData) {
        Call<AccessToken> wrapper = mAuthManager.getAccessToken(
                RequestBody.create(null, rawAuthData.getBytes()));
        return RetrofitHelper.get(wrapper);
    }

    public Observable<RefreshToken> getRefreshTokenObserve(String deviceCode) {
        return Observable.create(emitter -> {
            RefreshToken tokenResult = null;

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
                String msg = String.format("Error. Refresh token is empty! Debug data: device code: %s, client id: %s, client secret: %s",
                        deviceCode, mAppService.getClientId(), mAppService.getClientSecret());

                Log.e(TAG, msg);
                emitter.onError(new IllegalStateException(msg));
            }
        });
    }
}
