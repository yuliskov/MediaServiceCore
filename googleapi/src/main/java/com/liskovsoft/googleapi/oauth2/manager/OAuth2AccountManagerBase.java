package com.liskovsoft.googleapi.oauth2.manager;

import com.liskovsoft.googleapi.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googleapi.oauth2.impl.GoogleAccount;
import com.liskovsoft.googleapi.oauth2.models.auth.AccessToken;
import com.liskovsoft.mediaserviceinterfaces.google.data.Account;
import com.liskovsoft.sharedutils.mylogger.Log;

import java.util.Map;

public abstract class OAuth2AccountManagerBase {
    private static final String TAG = OAuth2AccountManagerBase.class.getSimpleName();
    private static final long TOKEN_REFRESH_PERIOD_MS = 60 * 60 * 1_000; // NOTE: auth token max lifetime is 60 min
    private String mCachedAuthorizationHeader;
    private String mCachedAuthorizationHeader2;
    private long mLastUpdateTime;

    protected abstract Account getSelectedAccount();
    protected abstract AccessToken obtainAccessToken(String refreshToken);

    public void checkAuth() {
        updateAuthHeaders();
    }

    protected void updateAuthHeaders() {
        if (mCachedAuthorizationHeader != null && System.currentTimeMillis() - mLastUpdateTime < TOKEN_REFRESH_PERIOD_MS) {
            return;
        }

        Account account = getSelectedAccount();
        String refreshToken = account != null ? ((GoogleAccount) account).getRefreshToken() : null;
        String refreshToken2 = account != null ? ((GoogleAccount) account).getRefreshToken2() : null;
        // get or create authorization on fly
        mCachedAuthorizationHeader = createAuthorizationHeader(refreshToken);
        mCachedAuthorizationHeader2 = createAuthorizationHeader(refreshToken2);
        syncWithRetrofit();

        mLastUpdateTime = System.currentTimeMillis();
    }

    /**
     * For testing purposes
     */
    public void setAuthorizationHeader(String authorizationHeader) {
        mCachedAuthorizationHeader = authorizationHeader;
        mCachedAuthorizationHeader2 = null;
        mLastUpdateTime = System.currentTimeMillis();

        syncWithRetrofit();
    }

    public void invalidateCache() {
        mCachedAuthorizationHeader = null;
    }

    /**
     * Authorization should be updated periodically (see expire_in field in response)
     */
    private synchronized String createAuthorizationHeader(String refreshToken) {
        Log.d(TAG, "Updating authorization header...");

        String authorizationHeader = null;

        AccessToken token = obtainAccessToken(refreshToken);

        if (token != null) {
            authorizationHeader = String.format("%s %s", token.getTokenType(), token.getAccessToken());
        } else {
            Log.e(TAG, "Access token is null!");
        }

        return authorizationHeader;
    }

    private void syncWithRetrofit() {
        Map<String, String> headers = RetrofitOkHttpHelper.getAuthHeaders();
        Map<String, String> headers2 = RetrofitOkHttpHelper.getAuthHeaders2();
        headers.clear();
        headers2.clear();

        if (mCachedAuthorizationHeader != null && getSelectedAccount() != null) {
            headers.put("Authorization", mCachedAuthorizationHeader);
            String pageIdToken = ((GoogleAccount) getSelectedAccount()).getPageIdToken();
            if (pageIdToken != null) {
                headers2.put("Authorization", mCachedAuthorizationHeader2);
                // Apply branded account rights (restricted videos). Branded refresh token with current account page id.
                headers.put("X-Goog-Pageid", pageIdToken);
            }
        }
    }
}
