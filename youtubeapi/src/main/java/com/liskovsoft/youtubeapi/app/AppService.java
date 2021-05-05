package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.clientdata.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.auth.V1.AuthManager;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.squareup.duktape.Duktape;

import java.util.Arrays;
import java.util.List;

public class AppService {
    private static final String TAG = AppService.class.getSimpleName();
    // Interval doesn't matter because we have MediaService.invalidateCache()
    private static final long CACHE_REFRESH_PERIOD_MS = 30 * 60 * 1_000; // 30 min
    //private static final long CACHE_REFRESH_PERIOD_MS = 24 * 60 * 60 * 1_000; // one day
    private static AppService sInstance;
    private final AppManagerWrapper mAppManager;
    private Duktape mDuktape;
    private String mCachedDecipherFunction;
    private String mCachedClientPlaybackNonceFunction;
    private String mCachedPlayerUrl;
    private String mCachedBaseUrl;
    private String mCachedClientId;
    private String mCachedClientSecret;
    private long mAppInfoUpdateTimeMS;
    private long mPlayerDataUpdateTimeMS;
    private long mBaseDataUpdateTimeMS;

    private AppService() {
        mAppManager = new AppManagerWrapper();
    }

    public static AppService instance() {
        if (sInstance == null) {
            sInstance = new AppService();
        }

        return sInstance;
    }

    /**
     * Js evaluator. Contains native *.so libs.<br/>
     * Note, lazy init for easy testing.<br/>
     * Could be tested only inside instrumented tests!
     */
    private Duktape getDuktape() {
        if (mDuktape == null) {
            mDuktape = Duktape.create(); // js evaluator, contains native *.so libs
        }

        return mDuktape;
    }

    /**
     * Decipher strings using js code
     */
    public List<String> decipher(List<String> ciphered) {
        if (isAllNulls(ciphered)) {
            return ciphered;
        }

        String decipherCode = createDecipherCode(ciphered);

        if (decipherCode == null) {
            return ciphered;
        }

        return runDecipherCode(decipherCode);
    }

    /**
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    public String getClientPlaybackNonce() {
        String code = createClientPlaybackNonceCode();

        if (code == null) {
            return null;
        }

        return getDuktape().evaluate(code).toString();
    }

    /**
     * Constant used in {@link AuthManager}
     */
    public String getClientId() {
        updateBaseData();

        return mCachedClientId;
    }

    /**
     * Constant used in {@link AuthManager}
     */
    public String getClientSecret() {
        updateBaseData();

        return mCachedClientSecret;
    }

    private static boolean isAllNulls(List<String> ciphered) {
        for (String cipher : ciphered) {
            if (cipher != null) {
                return false;
            }
        }

        return true;
    }

    private String createDecipherCode(List<String> ciphered) {
        String decipherFunction = getDecipherFunction();

        if (decipherFunction == null) {
            Log.e(TAG, "Oops. DecipherFunction is null...");
            return null;
        }

        StringBuilder result = new StringBuilder();
        result.append(decipherFunction);
        result.append("var result = [];");

        for (String cipher : ciphered) {
            result.append(String.format("result.push(decipherSignature('%s'));", cipher));
        }

        result.append("result.toString();");

        return result.toString();
    }

    private List<String> runDecipherCode(String decipherCode) {
        String result = getDuktape().evaluate(decipherCode).toString();

        String[] values = result.split(",");

        return Arrays.asList(values);
    }

    private String createClientPlaybackNonceCode() {
        String playbackNonceFunction = getClientPlaybackNonceFunction();

        if (playbackNonceFunction == null) {
            return null;
        }

        return AppConstants.FUNCTION_RANDOM_BYTES + playbackNonceFunction + "getClientPlaybackNonce();";
    }

    private String getDecipherFunction() {
        updatePlayerData();

        return mCachedDecipherFunction;
    }

    private String getClientPlaybackNonceFunction() {
        updatePlayerData();

        return mCachedClientPlaybackNonceFunction;
    }

    private String getPlayerUrl() {
        updateAppInfoData();

        return mCachedPlayerUrl;
    }

    private String getBaseUrl() {
        updateAppInfoData();

        return mCachedBaseUrl;
    }

    private void updateAppInfoData() {
        if (System.currentTimeMillis() - mAppInfoUpdateTimeMS < CACHE_REFRESH_PERIOD_MS &&
            mCachedPlayerUrl != null && mCachedBaseUrl != null) {
            return;
        }

        Log.d(TAG, "updateAppInfoData");

        AppInfo appInfo = mAppManager.getAppInfo(AppConstants.APP_USER_AGENT);

        if (appInfo != null) {
            mCachedPlayerUrl = ServiceHelper.tidyUrl(appInfo.getPlayerUrl());

            mCachedBaseUrl = ServiceHelper.tidyUrl(appInfo.getBaseUrl());

            mAppInfoUpdateTimeMS = System.currentTimeMillis();
        }
    }

    private void updatePlayerData() {
        if (System.currentTimeMillis() - mPlayerDataUpdateTimeMS < CACHE_REFRESH_PERIOD_MS &&
                mCachedDecipherFunction != null && mCachedClientPlaybackNonceFunction != null) {
            return;
        }

        Log.d(TAG, "updatePlayerData");

        String playerUrl = getPlayerUrl();

        if (playerUrl != null) {
            PlayerData playerData = mAppManager.getPlayerData(playerUrl);

            if (playerData != null) {
                String decipherFunction = playerData.getDecipherFunction();

                if (decipherFunction != null) {
                    mCachedDecipherFunction = Helpers.replace(decipherFunction, AppConstants.SIGNATURE_DECIPHER, "function decipherSignature");
                }

                String clientPlaybackNonce = playerData.getClientPlaybackNonce();

                if (clientPlaybackNonce != null) {
                    mCachedClientPlaybackNonceFunction =
                            Helpers.replace(clientPlaybackNonce, AppConstants.SIGNATURE_CLIENT_PLAYBACK_NONCE, "function getClientPlaybackNonce()");
                }

                mPlayerDataUpdateTimeMS = System.currentTimeMillis();
            }
        }
    }

    private void updateBaseData() {
        if (System.currentTimeMillis() - mBaseDataUpdateTimeMS < CACHE_REFRESH_PERIOD_MS &&
                mCachedClientId != null && mCachedClientSecret != null) {
            return;
        }

        Log.d(TAG, "updateBaseData");

        String baseUrl = getBaseUrl();

        if (baseUrl != null) {
            ClientData baseData = mAppManager.getBaseData(baseUrl);

            if (baseData != null) {
                String clientId = baseData.getClientId();

                if (clientId != null) {
                    mCachedClientId = clientId;
                }

                String clientSecret = baseData.getClientSecret();

                if (clientSecret != null) {
                    mCachedClientSecret = clientSecret;
                }

                mBaseDataUpdateTimeMS = System.currentTimeMillis();
            }
        }
    }

    public void invalidateCache() {
        mAppInfoUpdateTimeMS = 0;
        mPlayerDataUpdateTimeMS = 0;
        mBaseDataUpdateTimeMS = 0;
    }
}
