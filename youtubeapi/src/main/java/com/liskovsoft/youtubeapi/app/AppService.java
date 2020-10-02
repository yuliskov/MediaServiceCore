package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.squareup.duktape.Duktape;
import retrofit2.Call;

import java.util.Arrays;
import java.util.List;

public class AppService {
    private static final String TAG = AppService.class.getSimpleName();
    private static AppService sInstance;
    private final AppManager mAppManager;
    private Duktape mDuktape;
    private String mCachedDecipherFunction;
    private String mCachedClientPlaybackNonceFunction;
    private String mCachedPlayerUrl;
    private static final long CACHE_REFRESH_PERIOD_MS = 60 * 60 * 1_000;
    private long mCacheUpdateTimeMS;

    private AppService() {
        mAppManager = RetrofitHelper.withRegExp(AppManager.class);
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

    private static boolean isAllNulls(List<String> ciphered) {
        for (String cipher : ciphered) {
            if (cipher != null) {
                return false;
            }
        }

        return true;
    }

    private String getDecipherFunction() {
        invalidateCache();

        if (mCachedDecipherFunction != null) {
            return mCachedDecipherFunction;
        }

        String playerUrl = getPlayerUrl();

        if (playerUrl != null) {
            Call<PlayerData> wrapper = mAppManager.getPlayerData(playerUrl);
            PlayerData playerData = RetrofitHelper.get(wrapper);

            if (playerData != null) {
                String decipherFunction = playerData.getDecipherFunction();

                if (decipherFunction != null) {
                    mCachedDecipherFunction = Helpers.replace(decipherFunction, AppConstants.SIGNATURE_DECIPHER, "function decipherSignature");
                }
            }
        }

        return mCachedDecipherFunction;
    }

    private String getClientPlaybackNonceFunction() {
        invalidateCache();

        if (mCachedClientPlaybackNonceFunction != null) {
            return mCachedClientPlaybackNonceFunction;
        }

        String playerUrl = getPlayerUrl();

        if (playerUrl != null) {
            Call<PlayerData> wrapper = mAppManager.getPlayerData(playerUrl);
            PlayerData playerData = RetrofitHelper.get(wrapper);

            if (playerData != null) {
                String clientPlaybackNonce = playerData.getClientPlaybackNonce();

                if (clientPlaybackNonce != null) {
                    mCachedClientPlaybackNonceFunction =
                            Helpers.replace(clientPlaybackNonce, AppConstants.SIGNATURE_CLIENT_PLAYBACK_NONCE, "function getClientPlaybackNonce()");
                }
            }
        }

        return mCachedClientPlaybackNonceFunction;
    }

    private String getPlayerUrl() {
        invalidateCache();

        if (mCachedPlayerUrl != null) {
            return mCachedPlayerUrl;
        }

        Call<AppInfo> wrapper = mAppManager.getAppInfo(AppConstants.USER_AGENT_SAMSUNG_1);
        AppInfo appInfo = RetrofitHelper.get(wrapper);

        if (appInfo != null) {
            String playerUrl = appInfo.getPlayerUrl();

            if (playerUrl != null) {
                mCachedPlayerUrl = AppConstants.SCRIPTS_URL_BASE + playerUrl.replace("\\/", "/");
                notifyCacheUpdated();
            }
        }

        return mCachedPlayerUrl;
    }

    private String createDecipherCode(List<String> ciphered) {
        String decipherFunction = getDecipherFunction();

        if (decipherFunction == null) {
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

    private void invalidateCache() {
        long currentTimeTimeMs = System.currentTimeMillis();
        boolean cacheIsStalled = (currentTimeTimeMs - mCacheUpdateTimeMS) > CACHE_REFRESH_PERIOD_MS;

        if (cacheIsStalled) {
            Log.d(TAG, "Cache is stalled. Resetting...");
            mCachedDecipherFunction = null;
            mCachedClientPlaybackNonceFunction = null;
            mCachedPlayerUrl = null;
        }
    }

    private void notifyCacheUpdated() {
        mCacheUpdateTimeMS = System.currentTimeMillis();
    }
}
