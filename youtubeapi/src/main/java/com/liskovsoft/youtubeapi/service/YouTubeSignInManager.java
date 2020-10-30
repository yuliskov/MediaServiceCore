package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;
import com.liskovsoft.youtubeapi.service.internal.YouTubeAccountManager;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeSignInManager implements SignInManager {
    private static final String TAG = YouTubeSignInManager.class.getSimpleName();
    private static final long TOKEN_REFRESH_PERIOD_MS = 30 * 60 * 1_000; // 30 minutes
    private static YouTubeSignInManager sInstance;
    private final AuthService mAuthService;
    private final YouTubeAccountManager mAccountManager;
    private String mAuthorizationHeaderCached;
    private long mLastUpdateTime;

    private YouTubeSignInManager() {
        mAuthService = AuthService.instance();
        mAccountManager = YouTubeAccountManager.instance(this);

        GlobalPreferences.setOnInit(() -> {
            mAccountManager.init();
            this.updateAuthorizationHeader();
        });
    }

    public static YouTubeSignInManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeSignInManager();
        }

        return sInstance;
    }

    @Override
    public Observable<String> signInObserve() {
        return mAccountManager.signInObserve();
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

    @Override
    public void removeAccount(Account account) {
        mAccountManager.removeAccount(account);
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

            if ((currentTime - mLastUpdateTime) < TOKEN_REFRESH_PERIOD_MS) {
                return;
            }
        }

        Log.d(TAG, "Updating authorization header...");

        mAuthorizationHeaderCached = null;

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

        Account account = mAccountManager.getSelectedAccount();

        if (account != null) {
            token = mAuthService.getAccessToken(((YouTubeAccount) account).getRefreshToken());
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
