package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.models.clientdata.ClientData;
import com.liskovsoft.youtubeapi.auth.V1.AuthManager;
import com.liskovsoft.youtubeapi.common.helpers.JavaScript;
import com.squareup.duktape.Duktape;

import java.util.Arrays;
import java.util.List;

public class AppService {
    private static final String TAG = AppService.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 30 * 60 * 1_000; // NOTE: auth token max lifetime is 60 min
    private static AppService sInstance;
    private final AppManagerWrapper mAppManager;
    private Duktape mDuktape;
    private AppInfo mCachedAppInfo;
    private PlayerData mCachedPlayerData;
    private ClientData mCachedBaseData;
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

        return runCode(decipherCode);
    }

    /**
     * Throttle strings using js code
     */
    public List<String> throttleFix(List<String> throttled) {
        if (isAllNulls(throttled)) {
            return throttled;
        }

        String throttleCode = createThrottleCode(throttled);

        if (throttleCode == null) {
            return throttled;
        }

        return runCode(throttleCode);
    }

    /**
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    //public String getClientPlaybackNonce() {
    //    String code = createClientPlaybackNonceCode();
    //
    //    if (code == null) {
    //        return null;
    //    }
    //
    //    return JavaScript.evaluate(code);
    //}

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

        // TODO: NPE 1.6K!!!
        return mCachedBaseData != null ? mCachedBaseData.getClientId() : null;
    }

    /**
     * Constant used in {@link AuthManager}
     */
    public String getClientSecret() {
        updateBaseData();

        return mCachedBaseData != null ? mCachedBaseData.getClientSecret() : null;
    }

    /**
     * Used in get_video_info
     */
    public String getSignatureTimestamp() {
        updatePlayerData();

        // TODO: NPE 300!!!
        return mCachedPlayerData != null ? mCachedPlayerData.getSignatureTimestamp() : null;
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorId() {
        updateAppInfoData();

        // TODO: NPE 300!!!
        return mCachedAppInfo != null ? mCachedAppInfo.getVisitorData() : null;
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

    private String createThrottleCode(List<String> throttled) {
        String throttleFunction = getThrottleFunction();

        if (throttleFunction == null) {
            Log.e(TAG, "Oops. ThrottleFunction is null...");
            return null;
        }

        StringBuilder result = new StringBuilder();
        result.append(throttleFunction);
        result.append("var result = [];");

        for (String cipher : throttled) {
            result.append(String.format("result.push(throttleSignature('%s'));", cipher));
        }

        result.append("result.toString();");

        return result.toString();
    }

    private List<String> runCode(String code) {
        String result = JavaScript.evaluate(code);

        String[] values = result.split(",");

        return Arrays.asList(values);
    }

    private List<String> runCodeOld(String code) {
        String result = getDuktape().evaluate(code).toString();

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

        return mCachedPlayerData != null ? mCachedPlayerData.getDecipherFunction() : null;
    }

    private String getThrottleFunction() {
        updatePlayerData();

        // TODO: NPE 24 events
        return mCachedPlayerData != null ? mCachedPlayerData.getThrottleFunction() : null;
    }

    private String getClientPlaybackNonceFunction() {
        updatePlayerData();

        // TODO: NPE 10K!!!
        return mCachedPlayerData != null ? mCachedPlayerData.getClientPlaybackNonceFunction() : null;
    }

    private String getPlayerUrl() {
        updateAppInfoData();

        // TODO: NPE 2.5K
        return mCachedAppInfo != null ? mCachedAppInfo.getPlayerUrl() : null;
    }

    private String getBaseUrl() {
        updateAppInfoData();

        // TODO: NPE 143K!!!
        return mCachedAppInfo != null ? mCachedAppInfo.getBaseUrl() : null;
    }

    private synchronized void updateAppInfoData() {
        if (mCachedAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMS < CACHE_REFRESH_PERIOD_MS) {
            return;
        }

        Log.d(TAG, "updateAppInfoData");

        mCachedAppInfo = mAppManager.getAppInfo(AppConstants.APP_USER_AGENT);

        if (mCachedAppInfo != null) {
            mAppInfoUpdateTimeMS = System.currentTimeMillis();
        }
    }

    private synchronized void updatePlayerData() {
        if (mCachedPlayerData != null && System.currentTimeMillis() - mPlayerDataUpdateTimeMS < CACHE_REFRESH_PERIOD_MS) {
            return;
        }

        Log.d(TAG, "updatePlayerData");

        mCachedPlayerData = mAppManager.getPlayerData(getPlayerUrl());

        if (mCachedPlayerData != null) {
            mPlayerDataUpdateTimeMS = System.currentTimeMillis();
        }
    }

    private synchronized void updateBaseData() {
        if (mCachedBaseData != null && System.currentTimeMillis() - mBaseDataUpdateTimeMS < CACHE_REFRESH_PERIOD_MS) {
            return;
        }

        Log.d(TAG, "updateBaseData");

        mCachedBaseData = mAppManager.getBaseData(getBaseUrl());

        if (mCachedBaseData != null) {
            mBaseDataUpdateTimeMS = System.currentTimeMillis();
        }
    }

    public void invalidateCache() {
        mCachedAppInfo = null;
        mCachedPlayerData = null;
        mCachedBaseData = null;
    }

    public void refreshCacheIfNeeded() {
        updateAppInfoData();
        updatePlayerData();
        updateBaseData();
    }

    public boolean isCacheActual() {
        return System.currentTimeMillis() - mPlayerDataUpdateTimeMS < CACHE_REFRESH_PERIOD_MS;
    }
}
