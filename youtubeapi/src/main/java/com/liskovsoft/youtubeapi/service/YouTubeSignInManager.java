package com.liskovsoft.youtubeapi.service;

import android.annotation.SuppressLint;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.auth.models.AccessTokenResult;
import com.liskovsoft.youtubeapi.auth.models.UserCodeResult;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class YouTubeSignInManager implements SignInManager {
    private static final String TAG = YouTubeSignInManager.class.getSimpleName();
    private static YouTubeSignInManager sInstance;
    private final AuthService mAuthService;
    private String mAuthorization;
    private long mLastUpdateTime;
    private static final long UPDATE_PERIOD_MS = 30 * 60 * 1000; // 30 minutes

    private YouTubeSignInManager() {
        mAuthService = AuthService.instance();

        GlobalPreferences.setOnInit(this::updateAuthorizationHeader);
    }

    public static YouTubeSignInManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeSignInManager();
        }

        return sInstance;
    }

    @SuppressLint("CheckResult")
    @Override
    public String signIn() {
        UserCodeResult userCodeResult = mAuthService.getUserCode();

        mAuthService.getRefreshTokenObserve(userCodeResult.getDeviceCode())
                .subscribeOn(Schedulers.newThread())
                .subscribe(refreshTokenResult -> {
                    if (refreshTokenResult != null) {
                        Log.d(TAG, "Success. Refresh token successfully created!");
                        storeRefreshToken(refreshTokenResult.getRefreshToken());
                    } else {
                        Log.e(TAG, "Error. Refresh token is empty!");
                    }
                });

        return userCodeResult.getUserCode();
    }

    @Override
    public Observable<String> signInObserve() {
        return Observable.fromCallable(this::signIn);
    }

    @Override
    public void signOut() {
        // TODO: not implemented
    }

    @Override
    public Observable<Void> signOutObserve() {
        return Observable.create(emitter -> {
            signOut();
            emitter.onComplete();
        });
    }

    @Override
    public boolean isSigned() {
        // get or create authorization on fly
        updateAuthorizationHeader();

        return mAuthorization != null;
    }

    @Override
    public Observable<Boolean> isSignedObserve() {
        return Observable.fromCallable(this::isSigned);
    }

    public String getAuthorization() {
        // get or create authorization on fly
        updateAuthorizationHeader();

        return mAuthorization;
    }

    /**
     * Authorization should be updated periodically (see expire_in field in response)
     */
    private synchronized void updateAuthorizationHeader() {
        if (mAuthorization != null) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - mLastUpdateTime) < UPDATE_PERIOD_MS) {
                return;
            }
        }

        AccessTokenResult token = obtainAccessToken();

        if (token != null) {
            mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());
            mLastUpdateTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "Access token is null!");
        }
    }

    private AccessTokenResult obtainAccessToken() {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return null;
        }

        String rawAuthData = GlobalPreferences.sInstance.getRawAuthData();
        String mediaServiceRefreshToken = GlobalPreferences.sInstance.getMediaServiceRefreshToken();

        AccessTokenResult token = null;

        if (rawAuthData != null) {
            token = mAuthService.getAccessTokenRaw(rawAuthData);
        } else if (mediaServiceRefreshToken != null) {
            token = mAuthService.getAccessToken(mediaServiceRefreshToken);
        } else {
            Log.e(TAG, "Refresh token data doesn't stored in the app registry!");
        }

        return token;
    }

    private void storeRefreshToken(String refreshToken) {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceRefreshToken(refreshToken);
        Log.d(TAG, "Success. Refresh token stored successfully in registry: " + refreshToken);
    }
}
