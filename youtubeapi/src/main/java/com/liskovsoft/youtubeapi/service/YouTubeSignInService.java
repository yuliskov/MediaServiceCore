package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.SignInService;
import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;
import com.liskovsoft.youtubeapi.service.internal.YouTubeAccountManager;
import io.reactivex.Observable;

import java.util.List;
import java.util.Map;

public class YouTubeSignInService implements SignInService {
    private static final String TAG = YouTubeSignInService.class.getSimpleName();
    private static final long TOKEN_REFRESH_PERIOD_MS = 60 * 60 * 1_000; // NOTE: auth token max lifetime is 60 min
    private static YouTubeSignInService sInstance;
    private final AuthService mAuthService;
    private final YouTubeAccountManager mAccountManager;
    private String mCachedAuthorizationHeader;
    private long mLastUpdateTime;

    private YouTubeSignInService() {
        mAuthService = AuthService.instance();
        mAccountManager = YouTubeAccountManager.instance(this);

        GlobalPreferences.setOnInit(() -> {
            mAccountManager.init();
            try {
                this.updateAuthorizationHeader();
            } catch (Exception e) {
                // Host not found
                e.printStackTrace();
            }
        });
    }

    public static YouTubeSignInService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeSignInService();
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
        return RxHelper.create(emitter -> {
            signOut();
            emitter.onComplete();
        });
    }

    public void checkAuth() {
        // get or create authorization on fly
        updateAuthorizationHeader();
    }

    @Override
    public boolean isSigned() {
        // Condition created for the case when a device in offline mode.
        return mAccountManager.getSelectedAccount() != null;
    }

    @Override
    public Observable<Boolean> isSignedObserve() {
        return RxHelper.fromCallable(this::isSigned);
    }

    @Override
    public List<Account> getAccounts() {
        return mAccountManager.getAccounts();
    }

    @Override
    public Observable<List<Account>> getAccountsObserve() {
        return RxHelper.fromCallable(this::getAccounts);
    }

    /**
     * For testing purposes
     */
    public void setAuthorizationHeader(String authorizationHeader) {
        mCachedAuthorizationHeader = authorizationHeader;
        mLastUpdateTime = System.currentTimeMillis();

        syncWithRetrofit();
    }

    public void invalidateCache() {
        mCachedAuthorizationHeader = null;

        syncWithRetrofit();
    }

    @Override
    public void selectAccount(Account account) {
        mAccountManager.selectAccount(account);
    }

    @Override
    public void removeAccount(Account account) {
        mAccountManager.removeAccount(account);
    }

    /**
     * Authorization should be updated periodically (see expire_in field in response)
     */
    private synchronized void updateAuthorizationHeader() {
        if (mCachedAuthorizationHeader != null && System.currentTimeMillis() - mLastUpdateTime < TOKEN_REFRESH_PERIOD_MS) {
            return;
        }

        Log.d(TAG, "Updating authorization header...");

        mCachedAuthorizationHeader = null;

        AccessToken token = obtainAccessToken();

        if (token != null) {
            mCachedAuthorizationHeader = String.format("%s %s", token.getTokenType(), token.getAccessToken());
            mLastUpdateTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "Access token is null!");
        }

        syncWithRetrofit();
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

    private synchronized void syncWithRetrofit() {
        Map<String, String> headers = RetrofitOkHttpHelper.getAuthHeaders();

        if (mCachedAuthorizationHeader != null) {
            headers.put("Authorization", mCachedAuthorizationHeader);
        } else {
            headers.remove("Authorization");
        }
    }
}
