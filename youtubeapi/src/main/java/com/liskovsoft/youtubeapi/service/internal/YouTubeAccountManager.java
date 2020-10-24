package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.auth.models.info.AccountInt;
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;

import java.util.ArrayList;
import java.util.List;

public class YouTubeAccountManager {
    private static final String TAG = YouTubeAccountManager.class.getSimpleName();
    private static YouTubeAccountManager sInstance;
    private final AuthService mAuthService;
    private final YouTubeSignInManager mSignInManager;
    private String mRefreshToken;

    private YouTubeAccountManager() {
        mAuthService = AuthService.instance();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static YouTubeAccountManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeAccountManager();
        }

        return sInstance;
    }

    public List<Account> getAccounts() {
        return restoreAccounts();
    }

    /**
     * Set selected account token
     */
    public void setRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            Log.e(TAG, "Refresh token is null");
            return;
        }

        mRefreshToken = refreshToken;

        List<AccountInt> accountsInt = mAuthService.getAccounts(mSignInManager.getAuthorizationHeader());

        List<Account> accounts = restoreAccounts();

        if (accountsInt != null) {
            if (accounts == null) {
                accounts = new ArrayList<>();
            }

            for (AccountInt accountInt : accountsInt) {
                if (accountInt.isSelected()) {
                    YouTubeAccount account = YouTubeAccount.from(accountInt);
                    account.setRefreshToken(refreshToken);
                    accounts.add(account);
                    break;
                }
            }
        }

        persistAccounts(accounts);

        Log.d(TAG, "Success. Refresh token stored successfully in registry: " + refreshToken);
    }

    /**
     * Get selected account token
     */
    public String getRefreshToken() {
        if (mRefreshToken != null) {
            return mRefreshToken;
        }

        List<Account> accounts = restoreAccounts();

        if (accounts != null) {
            for (Account account : accounts) {
                if (account.isSelected()) {
                    mRefreshToken = ((YouTubeAccount) account).getRefreshToken();
                    break;
                }
            }
        }

        return mRefreshToken;
    }

    public void selectAccount(Account account) {
        if (account != null) {
            mRefreshToken = ((YouTubeAccount) account).getRefreshToken();
            mSignInManager.invalidateCache();
        }
    }

    private void persistAccounts(List<Account> accounts) {
        if (accounts == null) {
            return;
        }

        StringBuilder result = new StringBuilder();

        for (Account account : accounts) {
            if (result.length() != 0) {
                result.append("|");
            }
            result.append(account);
        }

        setAccountManagerData(result.toString());
    }

    private List<Account> restoreAccounts() {
        String data = getAccountManagerData();
        List<Account> result = null;

        if (data != null) {
            result = new ArrayList<>();

            String[] split = data.split("\\|");

            for (String spec : split) {
                result.add(YouTubeAccount.from(spec));
            }
        }

        return result;
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
}
