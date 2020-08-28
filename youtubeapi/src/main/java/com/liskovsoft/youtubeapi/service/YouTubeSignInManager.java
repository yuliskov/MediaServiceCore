package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.auth.models.UserCodeResult;
import io.reactivex.Observable;

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

    @Override
    public String signIn() {
        UserCodeResult userCodeResult = mAuthService.getUserCode();
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
        return mAuthorization != null;
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

        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return;
        }

        String rawAuthData = GlobalPreferences.sInstance.getRawAuthData();

        if (rawAuthData == null) {
            Log.e(TAG, "RawAuthData is null!");
            return;
        }

        RefreshTokenResult token = mAuthService.getRawRefreshToken(rawAuthData);

        if (token != null) {
            mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());
            mLastUpdateTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "Token is null!");
        }
    }
}
