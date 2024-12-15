package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.nsig.NSigExtractor;
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.auth.V1.AuthApi;
import com.liskovsoft.youtubeapi.common.js.V8Runtime;
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppService {
    private static final String TAG = AppService.class.getSimpleName();
    private static AppService sInstance;
    private final AppServiceInt mAppServiceInt;
    //private Duktape mDuktape;

    private AppService() {
        mAppServiceInt = new AppServiceIntCached();
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
        getPlayerData();

        if (throttled == null || getNSigExtractor() == null) {
            return null;
        }

        return getNSigExtractor().extractNSig(throttled);
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
        return PoTokenCloudService.getPoToken();
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
        // TODO: NPE 1.6K!!!
        return getClientData() != null ? getClientData().getClientId() : null;
    }

    /**
     * Constant used in {@link AuthApi}
     */
    public String getClientSecret() {
        return getClientData() != null ? getClientData().getClientSecret() : null;
    }

    /**
     * Used in get_video_info
     */
    public String getSignatureTimestamp() {
        // TODO: NPE 300!!!
        return getPlayerData() != null ? getPlayerData().getSignatureTimestamp() : null;
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        // TODO: NPE 300!!!
        return getAppInfoData() != null ? getAppInfoData().getVisitorData() : null;
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
        return getPlayerData() != null ? getPlayerData().getDecipherFunction() : null;
    }

    private String getClientPlaybackNonceFunction() {
        // NOTE: NPE 10K!!!
        return getPlayerData() != null ? getPlayerData().getClientPlaybackNonceFunction() : null;
    }

    private String getPlayerUrl() {
        // NOTE: NPE 2.5K
        //MediaServiceData data = MediaServiceData.instance();
        //return data.getPlayerUrl() != null ? data.getPlayerUrl() : mCachedAppInfo != null ? mCachedAppInfo.getPlayerUrl() : null;
        return getAppInfoData() != null ? getAppInfoData().getPlayerUrl() : null;
    }

    private String getClientUrl() {
        // NOTE: NPE 143K!!!
        return getAppInfoData() != null ? getAppInfoData().getClientUrl() : null;
    }

    private AppInfo getAppInfoData() {
        return mAppServiceInt.getAppInfo(DefaultHeaders.APP_USER_AGENT);
    }

    private ClientData getClientData() {
        return mAppServiceInt.getClientData(getClientUrl());
    }

    private PlayerData getPlayerData() {
        return mAppServiceInt.getPlayerData(getPlayerUrl());
    }

    private NSigExtractor getNSigExtractor() {
        if (getPlayerData() == null) {
            return null;
        }

        return mAppServiceInt.getNSigExtractor(getPlayerUrl());
    }

    private void updatePoTokenData() {
        PoTokenCloudService.updatePoToken();
    }

    public void invalidateCache() {
        mAppServiceInt.invalidateCache();
    }

    public void refreshCacheIfNeeded() {
        getAppInfoData();
        getPlayerData();
        getClientData();
    }

    public void refreshPoTokenIfNeeded() {
        updatePoTokenData();
    }

    /**
     * Visitor data is bound to specific js files versions.<br/>
     * After reset user will get the latest js file versions.
     */
    public void invalidateVisitorData() {
        mAppServiceInt.invalidateVisitorData();
    }

    public boolean isPlayerCacheActual() {
        return mAppServiceInt.isPlayerCacheActual();
    }
}
