package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;

import java.util.List;

public class YouTubeAccountStorage {
    private static final String TAG = YouTubeAccountStorage.class.getSimpleName();
    private static YouTubeAccountStorage sInstance;
    private final AuthService mAuthService;

    private YouTubeAccountStorage() {
        mAuthService = AuthService.instance();
    }

    public static YouTubeAccountStorage instance() {
        if (sInstance == null) {
            sInstance = new YouTubeAccountStorage();
        }

        return sInstance;
    }

    public List<Account> getAccounts() {
        //return YouTubeAccount.from(mAuthService.getAccounts(getAuthorizationHeader()));
        return null;
    }

    private void storeRefreshToken(String refreshToken) {
        // We don't have context, so can't create instance here.
        // Let's hope someone already created one for us.
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "GlobalPreferences is null!");
            return;
        }

        if (refreshToken == null) {
            Log.e(TAG, "Refresh token is null");
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceRefreshToken(refreshToken);
        Log.d(TAG, "Success. Refresh token stored successfully in registry: " + refreshToken);
    }
}
