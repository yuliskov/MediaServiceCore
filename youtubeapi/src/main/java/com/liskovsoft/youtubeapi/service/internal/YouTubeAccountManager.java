package com.liskovsoft.youtubeapi.service.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.googlecommon.common.models.auth.AccessToken;
import com.liskovsoft.mediaserviceinterfaces.SignInService.OnAccountChange;
import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.misc.WeakHashSet;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.googlecommon.common.models.auth.UserCode;
import com.liskovsoft.googlecommon.common.models.auth.info.AccountInt;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;
import com.liskovsoft.googlecommon.service.oauth.YouTubeAccount;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService;

import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class YouTubeAccountManager {
    private static final String TAG = YouTubeAccountManager.class.getSimpleName();
    private static YouTubeAccountManager sInstance;
    private boolean mStorageSynced;
    private final YouTubeSignInService mSignInService;
    private final WeakHashSet<OnAccountChange> mListeners = new WeakHashSet<>();
    /**
     * Fix ConcurrentModificationException when using {@link #getSelectedAccount()}
     */
    private final List<Account> mAccounts = new CopyOnWriteArrayList<Account>() {
        @Override
        public boolean add(Account account) {
            if (account == null) {
                return false;
            }

            mergeAndRemove(account);

            return super.add(account);
        }

        private void mergeAndRemove(Account account) {
            int index = indexOf(account);

            if (index != -1) {
                Account matched = get(index);

                // Don't remove these lines or you won't be able to enter to the account.
                while (contains(account)) {
                    remove(account);
                }

                // Do merge after the remove not before!!!
                ((YouTubeAccount) account).merge(matched);
            }
        }
    };

    private YouTubeAccountManager(YouTubeSignInService signInService) {
        mSignInService = signInService;
    }

    public static YouTubeAccountManager instance(YouTubeSignInService signInService) {
        if (sInstance == null) {
            sInstance = new YouTubeAccountManager(signInService);
        }

        return sInstance;
    }

    public Observable<String> signInObserve() {
        return RxHelper.createLong(emitter -> {
            UserCode userCodeResult = getAuthService().getUserCode();

            if (userCodeResult == null) {
                RxHelper.onError(emitter, "User code result is empty");
                return;
            }

            emitter.onNext(userCodeResult.getUserCode());

            try {
                AccessToken token = getAuthService().getAccessTokenWait(userCodeResult.getDeviceCode());

                persistRefreshToken(token.getRefreshToken());

                emitter.onComplete();
            } catch (InterruptedException e) {
                // NOP
            }
        });
    }

    @Nullable
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

        // Create initial account (with only refresh key)
        YouTubeAccount tempAccount = YouTubeAccount.fromToken(refreshToken);
        addAccount(tempAccount);

        // Use initial account to create auth header and fetch the accounts below
        mSignInService.invalidateCache();
        mSignInService.checkAuth();

        // Remove initial account (with only refresh key)
        mAccounts.remove(tempAccount); // multi thread fix

        List<AccountInt> accountsInt = getAuthService().getAccounts(); // runs under auth header from above

        if (accountsInt != null) {
            for (AccountInt accountInt : accountsInt) {
                YouTubeAccount account = YouTubeAccount.from(accountInt);
                account.setRefreshToken(refreshToken);
                addAccount(account);
            }
        }

        fixSelectedAccount();

        // Apply merged tokens
        mSignInService.checkAuth();

        persistAccounts();
        onAccountChanged();
    }

    private void addAccount(Account newAccount) {
        if (newAccount.isSelected()) {
            for (Account account : mAccounts) {
                ((YouTubeAccount) account).setSelected(false);
            }
        }

        mAccounts.add(newAccount);
    }

    public void selectAccount(Account newAccount) {
        if (Helpers.equals(newAccount, getSelectedAccount())) {
            return;
        }

        for (Account account : mAccounts) {
            ((YouTubeAccount) account).setSelected(newAccount != null && newAccount.equals(account));
        }

        persistAccounts();

        onAccountChanged();
    }

    public void removeAccount(Account account) {
        if (account != null && mAccounts.contains(account)) {
            mAccounts.remove(account);
            persistAccounts();

            onAccountChanged();
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
        setAccountManagerData(Helpers.mergeArray(mAccounts.toArray()));
    }

    private void restoreAccounts() {
        String data = getAccountManagerData();

        if (data != null) {
            String[] split = Helpers.splitArray(data);
            mAccounts.clear();

            for (String spec : split) {
                mAccounts.add(YouTubeAccount.from(spec));
            }
        }

        //notifyListeners();
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
    }

    public void addOnAccountChange(OnAccountChange listener) {
        if (!mListeners.contains(listener)) {
            if (listener instanceof MediaServicePrefs) {
                mListeners.add(0, listener);
            } else {
                mListeners.add(listener);
            }
        }
    }

    /**
     * Fix situations when there is no selected account<br/>
     * Mark first one as selected.
     */
    private void fixSelectedAccount() {
        if (mAccounts.isEmpty()) {
            return;
        }

        if (getSelectedAccount() == null) {
            selectAccount(mAccounts.get(0));
        }
    }

    private void onAccountChanged() {
        mSignInService.invalidateCache();
        AppService.instance().invalidateCache(); // regenerate visitor data
        VideoInfoService.instance().resetInfoType(); // reset to the default format

        notifyListeners();
    }

    /**
     * Sync avatars, names and emails
     */
    public void syncStorage() {
        if (mStorageSynced)
            return;

        List<Account> storedAccounts = getAccounts();

        if (storedAccounts != null && !storedAccounts.isEmpty()) {
            List<AccountInt> newAccounts = getAuthService().getAccounts();

            Account selectedAccount = getSelectedAccount();

            if (newAccounts != null) {
                for (AccountInt newAccount : newAccounts) {
                    YouTubeAccount account = YouTubeAccount.from(newAccount);
                    account.setSelected(account.equals(selectedAccount));
                    addAccount(account);
                }
                persistAccounts();
            }
        }

        mStorageSynced = true;
    }

    private void notifyListeners() {
        Account account = getSelectedAccount();

        // Fix sign in bug
        mListeners.forEach(listener -> {
            if (listener instanceof MediaServicePrefs) {
                listener.onAccountChanged(account);
            } else {
                RxHelper.runUser(() -> listener.onAccountChanged(account));
            }
        });
    }

    @NonNull
    private static AuthService getAuthService() {
        return AuthService.instance();
    }
}
