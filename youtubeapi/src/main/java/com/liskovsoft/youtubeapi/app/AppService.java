package com.liskovsoft.youtubeapi.app;

import android.content.Context;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.V1.AuthApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppService {
    private static AppService sInstance;
    private final AppServiceInt mAppServiceInt;
    private String mClientPlaybackNonce;

    private AppService() {
        mAppServiceInt = new AppServiceIntCached();
    }

    public static AppService instance() {
        if (sInstance == null) {
            sInstance = new AppService();
        }

        return sInstance;
    }

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
        if (mAppServiceInt.getPlayerDataExtractor() == null) {
            return null;
        }

        return mAppServiceInt.getPlayerDataExtractor().decipherItems(ciphered);
    }

    public List<String> fixThrottling(List<String> throttledList) {
        if (Helpers.allNulls(throttledList)) {
            return null;
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
        if (throttled == null || mAppServiceInt.getPlayerDataExtractor() == null) {
            return null;
        }

        return mAppServiceInt.getPlayerDataExtractor().extractNSig(throttled);
    }

    public void resetClientPlaybackNonce() {
        mClientPlaybackNonce = null;
    }

    /**
     * NOTE: Unique per video info instance<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    public synchronized String getClientPlaybackNonce() {
        if (mClientPlaybackNonce != null) {
            return mClientPlaybackNonce;
        }

        if (mAppServiceInt.getPlayerDataExtractor() == null) {
            return null;
        }

        mClientPlaybackNonce = mAppServiceInt.getPlayerDataExtractor().createClientPlaybackNonce();

        return mClientPlaybackNonce;
    }

    public String getSessionPoToken() {
        return PoTokenGate.getSessionPoToken();
    }

    /**
     * Constant used in {@link AuthApi}
     */
    public String getClientId() {
        return mAppServiceInt.getClientId();
    }

    /**
     * Constant used in {@link AuthApi}
     */
    public String getClientSecret() {
        return mAppServiceInt.getClientSecret();
    }

    /**
     * Used in get_video_info
     */
    public String getSignatureTimestamp() {
        if (mAppServiceInt.getPlayerDataExtractor() == null) {
            return null;
        }

        return mAppServiceInt.getPlayerDataExtractor().getSignatureTimestamp();
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        return mAppServiceInt.getVisitorData();
    }

    private void updatePoTokenData() {
        PoTokenGate.updatePoToken();
    }

    public void invalidateCache() {
        mAppServiceInt.invalidateCache();
    }

    public void refreshCacheIfNeeded() {
        mAppServiceInt.refreshCacheIfNeeded();
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

    public Context getContext() {
        return GlobalPreferences.isInitialized() ? GlobalPreferences.sInstance.getContext() : null;
    }
}
