package com.liskovsoft.youtubeapi.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor;
import com.liskovsoft.youtubeapi.auth.V1.AuthApi;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.Pair;

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

    /**
     * Extracts signature used in music videos
     */
    public String extractSig(String sParam) {
        if (sParam == null) {
            return null;
        }

        return extractSig(Collections.singletonList(sParam)).get(0);
    }

    /**
     * Extracts signature used in music videos
     */
    public List<String> extractSig(List<String> sParams) {
        PlayerDataExtractor playerDataExtractor = mAppServiceInt.getPlayerDataExtractor();

        if (playerDataExtractor == null) {
            return null;
        }

        return playerDataExtractor.extractSig(sParams);
    }

    public String extractNSig(String nParam) {
        if (nParam == null) {
            return null;
        }

        PlayerDataExtractor playerDataExtractor = mAppServiceInt.getPlayerDataExtractor();

        if (playerDataExtractor == null) {
            return null;
        }

        return playerDataExtractor.extractNSig(nParam);
    }

    /**
     * nParams - throttle params<br/>
     * sParams - signature used in music videos
     */
    public Pair<List<String>, List<String>> bulkSigExtract(List<String> nParams, List<String> sParams) {
        return bulkSigExtract(null, nParams, sParams);
    }

    /**
     * nParams - throttle params<br/>
     * sParams - signature used in music videos
     */
    public Pair<List<String>, List<String>> bulkSigExtract(@Nullable AppClient client, List<String> nParams, List<String> sParams) {
        if (Helpers.allNulls(nParams, sParams)) {
            return null;
        }

        PlayerDataExtractor playerDataExtractor = mAppServiceInt.getPlayerDataExtractor(client);

        if (playerDataExtractor == null) {
            return null;
        }

        return playerDataExtractor.bulkSigExtract(nParams, sParams);
    }

    public List<String> extractNSig(List<String> nParams) {
        if (Helpers.allNulls(nParams)) {
            return null;
        }

        List<String> result = new ArrayList<>();

        String previousNParam = null;
        String previousNSig = null;

        for (String nParam : nParams) {
            if (Helpers.equals(nParam, previousNParam)) {
                result.add(previousNSig);
                continue;
            }

            String nSig = extractNSig(nParam);

            result.add(nSig);

            previousNParam = nParam;
            previousNSig = nSig;
        }

        return result;
    }

    /**
     * NOTE: Unique per video info instance<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    public synchronized String getClientPlaybackNonce() {
        return getClientPlaybackNonce(null);
    }

    /**
     * NOTE: Unique per video info instance<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    public synchronized String getClientPlaybackNonce(@Nullable AppClient client) {
        if (mClientPlaybackNonce != null) {
            return mClientPlaybackNonce;
        }

        PlayerDataExtractor playerDataExtractor = mAppServiceInt.getPlayerDataExtractor(client);

        if (playerDataExtractor == null) {
            return null;
        }

        mClientPlaybackNonce = playerDataExtractor.createClientPlaybackNonce();

        return mClientPlaybackNonce;
    }

    public void resetClientPlaybackNonce() {
        mClientPlaybackNonce = null;
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
        return getSignatureTimestamp(null);
    }

    /**
     * Used in get_video_info
     */
    public String getSignatureTimestamp(@Nullable AppClient client) {
        PlayerDataExtractor playerDataExtractor = mAppServiceInt.getPlayerDataExtractor(client);

        if (playerDataExtractor == null) {
            return null;
        }

        return playerDataExtractor.getSignatureTimestamp();
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        return mAppServiceInt.getVisitorData();
    }

    public void invalidateCache() {
        mAppServiceInt.invalidateCache();
    }

    public void refreshCacheIfNeeded() {
        mAppServiceInt.refreshCacheIfNeeded();
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

    @NonNull
    public Context getContext() {
        Context context = GlobalPreferences.isInitialized() ? GlobalPreferences.sInstance.getContext() : null;

        if (context == null) {
            throw new IllegalStateException("The Context isn't initialized yet");
        }

        return context;
    }
}
