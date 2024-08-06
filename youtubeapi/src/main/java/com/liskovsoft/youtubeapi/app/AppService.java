package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.nsig.NSigExtractor;
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.auth.V1.AuthApi;
import com.liskovsoft.youtubeapi.common.js.V8Runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppService {
    private static final String TAG = AppService.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private static AppService sInstance;
    private final AppApiWrapper mAppApiWrapper;
    //private Duktape mDuktape;
    private AppInfo mCachedAppInfo;
    private PlayerData mCachedPlayerData;
    private ClientData mCachedClientData;
    private long mAppInfoUpdateTimeMs;
    private long mPlayerDataUpdateTimeMs;
    private long mClientDataUpdateTimeMs;
    private NSigExtractor mNSigExtractor;

    private AppService() {
        mAppApiWrapper = new AppApiWrapper();
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
    //private Duktape getDuktape() {
    //    if (mDuktape == null) {
    //        mDuktape = Duktape.create(); // js evaluator, contains native *.so libs
    //    }
    //
    //    return mDuktape;
    //}

    public String decipher(String ciphered) {
        if (ciphered == null) {
            return null;
        }

        return decipher(Collections.singletonList(ciphered)).get(0);
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

    public List<String> fixThrottling(List<String> throttledList) {
        if (isAllNulls(throttledList)) {
            return throttledList;
        }

        List<String> result = new ArrayList<>();

        String previousThrottled = null;
        String previousThrottleFixResult = null;

        for (String throttled : throttledList) {
            if (Helpers.equals(throttled, previousThrottled)) {
                result.add(previousThrottleFixResult);
                continue;
            }

            String throttleFixResult = fixThrottling(throttled);

            result.add(throttleFixResult);

            previousThrottled = throttled;
            previousThrottleFixResult = throttleFixResult;
        }

        return result;
    }

    public String fixThrottling(String throttled) {
        updatePlayerData();

        if (throttled == null || mNSigExtractor == null) {
            return null;
        }

        return mNSigExtractor.extractNSig(throttled);
    }

    /**
     * Throttle strings using js code
     */
    private List<String> fixThrottlingOld(List<String> throttled) {
        if (isAllNulls(throttled)) {
            return throttled;
        }

        String throttleCode = createThrottleCode(throttled);

        if (throttleCode == null) {
            return throttled;
        }

        return runCode(throttleCode);
    }

    private String fixThrottlingOld(String throttled) {
        if (throttled == null) {
            return null;
        }

        return fixThrottlingOld(Collections.singletonList(throttled)).get(0);
    }

    /**
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    public String getClientPlaybackNonce() {
        String function = getClientPlaybackNonceFunction();

        if (function == null) {
            return null;
        }

        return V8Runtime.instance().evaluate(function);
    }

    public String getPoTokenResult() {
        String function = getPoTokenResultFunction();

        if (function == null) {
            return null;
        }

        return V8Runtime.instance().evaluate(function);
    }

    /**
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    //public String getClientPlaybackNonceDuktape() {
    //    String code = createClientPlaybackNonceCode();
    //
    //    if (code == null) {
    //        return null;
    //    }
    //
    //    return getDuktape().evaluate(code).toString();
    //}

    /**
     * Constant used in {@link AuthApi}
     */
    public String getClientId() {
        updateClientData();

        // TODO: NPE 1.6K!!!
        return mCachedClientData != null ? mCachedClientData.getClientId() : null;
    }

    /**
     * Constant used in {@link AuthApi}
     */
    public String getClientSecret() {
        updateClientData();

        return mCachedClientData != null ? mCachedClientData.getClientSecret() : null;
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
        String result = V8Runtime.instance().evaluate(code);

        String[] values = result.split(",");

        return Arrays.asList(values);
    }

    //private List<String> runCodeDuktape(String code) {
    //    String result = getDuktape().evaluate(code).toString();
    //
    //    String[] values = result.split(",");
    //
    //    return Arrays.asList(values);
    //}

    private String getDecipherFunction() {
        updatePlayerData();

        return mCachedPlayerData != null ? mCachedPlayerData.getDecipherFunction() : null;
    }

    private String getThrottleFunction() {
        updatePlayerData();

        // NOTE: NPE 24 events
        return mCachedPlayerData != null ? mCachedPlayerData.getThrottleFunction() : null;
    }

    private String getClientPlaybackNonceFunction() {
        updatePlayerData();

        // NOTE: NPE 10K!!!
        return mCachedPlayerData != null ? mCachedPlayerData.getClientPlaybackNonceFunction() : null;
    }

    private String getPoTokenResultFunction() {
        updatePlayerData();

        // NOTE: NPE
        return mCachedPlayerData != null ? mCachedPlayerData.getPoTokenResultFunction() : null;
    }

    private String getPlayerUrl() {
        updateAppInfoData();

        // TODO: temporal fix
        // NOTE: NPE 2.5K
        return mCachedAppInfo != null ? mCachedAppInfo.getPlayerUrl() : null;
        //return "https://www.youtube.com/s/player/1f8742dc/tv-player-ias.vflset/tv-player-ias.js";
    }

    private String getClientUrl() {
        updateAppInfoData();

        // NOTE: NPE 143K!!!
        return mCachedAppInfo != null ? mCachedAppInfo.getClientUrl() : null;
    }

    private synchronized void updateAppInfoData() {
        if (mCachedAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return;
        }

        Log.d(TAG, "updateAppInfoData");

        mCachedAppInfo = mAppApiWrapper.getAppInfo(DefaultHeaders.APP_USER_AGENT);

        if (mCachedAppInfo != null) {
            mAppInfoUpdateTimeMs = System.currentTimeMillis();
        }
    }

    private synchronized void updatePlayerData() {
        if (mCachedPlayerData != null && System.currentTimeMillis() - mPlayerDataUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return;
        }

        if (getPlayerUrl() == null) {
            return;
        }

        Log.d(TAG, "updatePlayerData");

        mCachedPlayerData = mAppApiWrapper.getPlayerData(getPlayerUrl());

        if (mCachedPlayerData != null) {
            mPlayerDataUpdateTimeMs = System.currentTimeMillis();
            mNSigExtractor = new NSigExtractor(getPlayerUrl());
        }
    }

    private synchronized void updateClientData() {
        if (mCachedClientData != null && System.currentTimeMillis() - mClientDataUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return;
        }

        Log.d(TAG, "updateClientData");

        mCachedClientData = mAppApiWrapper.getClientData(getClientUrl());

        if (mCachedClientData != null) {
            mClientDataUpdateTimeMs = System.currentTimeMillis();
        }
    }

    public void invalidateCache() {
        mCachedAppInfo = null;
        mCachedPlayerData = null;
        mCachedClientData = null;
    }

    public void refreshCacheIfNeeded() {
        updateAppInfoData();
        updatePlayerData();
        updateClientData();
    }

    public boolean isCacheActual() {
        return System.currentTimeMillis() - mPlayerDataUpdateTimeMs < CACHE_REFRESH_PERIOD_MS;
    }

    /**
     * Visitor data is bound to specific js files versions.<br/>
     * After reset user will get the latest js file versions.
     */
    public void invalidateVisitorData() {
        mAppApiWrapper.invalidateVisitorData();
    }
}
