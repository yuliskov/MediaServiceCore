package com.liskovsoft.youtubeapi.app.models.cached;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.PlayerData;

public class PlayerDataCached extends PlayerData {
    private static final String DELIM = "%pdc%";
    private final String mPlayerUrl;
    private final String mClientPlaybackNonceFunction;
    private final String mRawClientPlaybackNonceFunction;
    private final String mDecipherFunction;
    private final String mSignatureTimestamp;

    public PlayerDataCached(String playerUrl,
                             String clientPlaybackNonceFunction,
                             String rawClientPlaybackNonceFunction,
                             String decipherFunction,
                             String signatureTimestamp) {
        mPlayerUrl = playerUrl;
        mClientPlaybackNonceFunction = clientPlaybackNonceFunction;
        mRawClientPlaybackNonceFunction = rawClientPlaybackNonceFunction;
        mDecipherFunction = decipherFunction;
        mSignatureTimestamp = signatureTimestamp;
    }

    public static PlayerDataCached fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.split(spec, DELIM);

        return new PlayerDataCached(
                Helpers.parseStr(split, 0),
                Helpers.parseStr(split, 1),
                Helpers.parseStr(split, 2),
                Helpers.parseStr(split, 3),
                Helpers.parseStr(split, 4));
    }

    public static PlayerDataCached from(String playerUrl, PlayerData playerData) {
        if (playerData == null) {
            return null;
        }

        return new PlayerDataCached(playerUrl,
                playerData.getClientPlaybackNonceFunction(),
                playerData.getRawClientPlaybackNonceFunction(),
                playerData.getDecipherFunction(),
                playerData.getSignatureTimestamp());
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.merge(DELIM, mPlayerUrl, mClientPlaybackNonceFunction, mRawClientPlaybackNonceFunction, mDecipherFunction, mSignatureTimestamp);
    }

    @Override
    public String getClientPlaybackNonceFunction() {
        return mClientPlaybackNonceFunction;
    }

    @Override
    public String getRawClientPlaybackNonceFunction() {
        return mRawClientPlaybackNonceFunction;
    }

    @Override
    public String getDecipherFunction() {
        return mDecipherFunction;
    }

    @Override
    public String getSignatureTimestamp() {
        return mSignatureTimestamp;
    }

    public String getPlayerUrl() {
        return mPlayerUrl;
    }

    public boolean validate() {
        return mClientPlaybackNonceFunction != null && mRawClientPlaybackNonceFunction != null && mDecipherFunction != null && mSignatureTimestamp != null;
    }
}
