package com.liskovsoft.youtubeapi.service;

import android.annotation.SuppressLint;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.auth.UserCode;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.service.internal.YouTubeAccountManager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class YouTubeSignInManager implements SignInManager {
    private static final String TAG = YouTubeSignInManager.class.getSimpleName();
    private static final long UPDATE_PERIOD_MS = 30 * 60 * 1000; // 30 minutes
    private static YouTubeSignInManager sInstance;
    private final AuthService mAuthService;
    private final YouTubeAccountManager mAccountManager;
    private String mAuthorizationHeaderCached;
    private Disposable mTokenAction;
    private long mLastUpdateTime;

    private YouTubeSignInManager() {
        mAuthService = AuthService.instance();
        mAccountManager = YouTubeAccountManager.instance();

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
        ObservableHelper.disposeActions(mTokenAction);

        UserCode userCodeResult = mAuthService.getUserCode();

        mTokenAction = mAuthService.getRefreshTokenObserve(userCodeResult.getDeviceCode())
                .subscribeOn(Schedulers.newThread())
                .subscribe(refreshTokenResult -> {
                    Log.d(TAG, "Success. Refresh token successfully created!");
                    mAccountManager.setRefreshToken(refreshTokenResult.getRefreshToken());
                }, error -> Log.e(TAG, error));

        return userCodeResult.getUserCode();
    }

    @Override
    public Observable<String> signInObserve() {
        ObservableHelper.disposeActions(mTokenAction);

        return Observable.create(emitter -> {
            UserCode userCodeResult = mAuthService.getUserCode();

            emitter.onNext(userCodeResult.getUserCode());

            mTokenAction = mAuthService.getRefreshTokenObserve(userCodeResult.getDeviceCode())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(refreshTokenResult -> {
                            Log.d(TAG, "Success. Refresh token successfully created!");
                            mAccountManager.setRefreshToken(refreshTokenResult.getRefreshToken());
                            emitter.onComplete();
                        }, error -> {
                            Log.e(TAG, "Error. Can't obtain refresh token!");
                            emitter.onError(error);
                        });
        });
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

        return mAuthorizationHeaderCached != null;
    }

    @Override
    public Observable<Boolean> isSignedObserve() {
        return Observable.fromCallable(this::isSigned);
    }

    @Override
    public List<Account> getAccounts() {
        return mAccountManager.getAccounts();
    }

    @Override
    public Observable<List<Account>> getAccountsObserve() {
        return Observable.fromCallable(this::getAccounts);
    }

    public String getAuthorizationHeader() {
        // get or create authorization on fly
        updateAuthorizationHeader();

        return mAuthorizationHeaderCached;
    }

    @Override
    public void selectAccount(Account account) {
        mAccountManager.selectAccount(account);
    }

    public void invalidateCache() {
        mLastUpdateTime = 0;
    }

    /**
     * Authorization should be updated periodically (see expire_in field in response)
     */
    private synchronized void updateAuthorizationHeader() {
        if (mAuthorizationHeaderCached != null) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - mLastUpdateTime) < UPDATE_PERIOD_MS) {
                return;
            }
        }

        Log.d(TAG, "Updating authorization header...");

        AccessToken token = obtainAccessToken();

        if (token != null) {
            mAuthorizationHeaderCached = String.format("%s %s", token.getTokenType(), token.getAccessToken());
            mLastUpdateTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "Access token is null!");
        }
    }

    private AccessToken obtainAccessToken() {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return null;
        }

        AccessToken token = null;

        String refreshToken = mAccountManager.getRefreshToken();

        if (refreshToken != null) {
            token = mAuthService.getAccessToken(refreshToken);
        } else {
            String rawAuthData = GlobalPreferences.sInstance.getRawAuthData();

            if (rawAuthData != null) {
                token = mAuthService.getAccessTokenRaw(rawAuthData);
            } else {
                Log.e(TAG, "Refresh token data doesn't stored in the app registry!");
            }
        }

        return token;
    }
}
