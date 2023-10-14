package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.youtubeapi.auth.models.auth.RefreshToken;
import com.liskovsoft.youtubeapi.auth.models.auth.UserCode;
import com.liskovsoft.youtubeapi.auth.models.info.AccountInt;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class YouTubeAccountManager {
    private static final String TAG = YouTubeAccountManager.class.getSimpleName();
    private static YouTubeAccountManager sInstance;
    private final AuthService mAuthService;
    private final YouTubeSignInService mSignInService;
    private Runnable mOnChange;
    /**
     * Fix ConcurrentModificationException when using {@link #getSelectedAccount()}
     */
    private final List<Account> mAccounts = new CopyOnWriteArrayList<Account>() {
        @Override
        public boolean add(Account account) {
            if (account == null) {
                return false;
            }

            // Don't remove these lines or you won't be able to enter to the account.
            while (contains(account)) {
                remove(account);
            }

            return super.add(account);
        }
    };

    private YouTubeAccountManager(YouTubeSignInService signInService) {
        mAuthService = AuthService.instance();
        mSignInService = signInService;
    }

    public static YouTubeAccountManager instance(YouTubeSignInService signInManager) {
        if (sInstance == null) {
            sInstance = new YouTubeAccountManager(signInManager);
        }

        return sInstance;
    }

    public Observable<String> signInObserve() {
        return RxHelper.createLong(emitter -> {
            UserCode userCodeResult = mAuthService.getUserCode();

            if (userCodeResult == null) {
                RxHelper.onError(emitter, "User code result is empty");
                return;
            }

            emitter.onNext(userCodeResult.getUserCode());

            try {
                RefreshToken token = mAuthService.getRefreshTokenWait(userCodeResult.getDeviceCode());

                persistRefreshToken(token.getRefreshToken());

                emitter.onComplete();
            } catch (InterruptedException e) {
                // NOP
            }
        });
    }

    public List<Account> getAccounts() {
        return mAccounts;
    }

    /**
     * Set selected account token
     */
    private void persistRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            Log.e(TAG, "Refresh token is null");
            return;
        }

        addAccount(YouTubeAccount.fromToken(refreshToken));

        mSignInService.checkAuth();

        List<AccountInt> accountsInt = mAuthService.getAccounts();

        if (accountsInt != null) {
            for (AccountInt accountInt : accountsInt) {
                // Refresh token should be unique per account but we only have one.
                if (accountInt.isSelected()) {
                    YouTubeAccount account = YouTubeAccount.from(accountInt);
                    account.setRefreshToken(refreshToken);
                    addAccount(account);
                    break;
                }
            }
        }

        Log.d(TAG, "Success. Refresh token stored successfully in registry: " + refreshToken);
    }

    private void addAccount(Account newAccount) {
        for (Account account : mAccounts) {
            ((YouTubeAccount) account).setSelected(false);
        }

        mAccounts.add(newAccount);

        persistAccounts();
    }

    public void selectAccount(Account newAccount) {
        if (Helpers.equals(newAccount, getSelectedAccount())) {
            return;
        }

        for (Account account : mAccounts) {
            ((YouTubeAccount) account).setSelected(newAccount != null && newAccount.equals(account));
        }

        persistAccounts();
    }

    public void removeAccount(Account account) {
        if (account != null && mAccounts.contains(account)) {
            mAccounts.remove(account);
            persistAccounts();
        }
    }

    public Account getSelectedAccount() {
        for (Account account : mAccounts) {
            if (account != null && account.isSelected()) {
                return account;
            }
        }

        return null;
    }

    private void persistAccounts() {
        List<Account> nonEmptyAccounts = new ArrayList<>();

        for (Account account : mAccounts) {
            if (account.isEmpty()) {
                continue;
            }

            nonEmptyAccounts.add(account);
        }

        setAccountManagerData(Helpers.mergeArray(nonEmptyAccounts.toArray()));

        mSignInService.invalidateCache();

        if (mOnChange != null) {
            // Fix sign in bug
            RxHelper.runUser(mOnChange);
        }
    }

    private void restoreAccounts() {
        String data = getAccountManagerData();

        if (data != null) {
            String[] split = Helpers.splitArrayLegacy(data);
            mAccounts.clear();

            for (String spec : split) {
                mAccounts.add(YouTubeAccount.from(spec));
            }
        }

        if (mOnChange != null) {
            mOnChange.run();
        }
    }

    private void setAccountManagerData(String data) {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceAccountData(data);
    }

    private String getAccountManagerData() {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return null;
        }

        return GlobalPreferences.sInstance.getMediaServiceAccountData();
    }

    public void init() {
        restoreAccounts();
        applyLegacyAccounts();
    }

    private void applyLegacyAccounts() {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return;
        }

        String token = GlobalPreferences.sInstance.getMediaServiceRefreshToken();

        if (token != null) {
            persistRefreshToken(token);
            GlobalPreferences.sInstance.setMediaServiceRefreshToken(null);
        }
    }

    public void setOnChange(Runnable onChange) {
        mOnChange = onChange;
    }
}
