package com.liskovsoft.youtubeapi.app.models.cached;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.PlayerData;

public class PlayerDataCached extends PlayerData {
    private static final String DELIM = "%pd%";
    private final String mClientPlaybackNonceFunction;
    private final String mRawClientPlaybackNonceFunction;
    private final String mDecipherFunction;
    private final String mSignatureTimestamp;

    private PlayerDataCached(String clientPlaybackNonceFunction, String rawClientPlaybackNonceFunction, String decipherFunction, String signatureTimestamp) {
        mClientPlaybackNonceFunction = clientPlaybackNonceFunction;
        mRawClientPlaybackNonceFunction = rawClientPlaybackNonceFunction;
        mDecipherFunction = decipherFunction;
        mSignatureTimestamp = signatureTimestamp;
    }

    public static PlayerData fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.split(DELIM, spec);

        return new PlayerDataCached(Helpers.parseStr(split, 0), Helpers.parseStr(split, 1), Helpers.parseStr(split, 2), Helpers.parseStr(split, 3));
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.merge(DELIM, mClientPlaybackNonceFunction, mRawClientPlaybackNonceFunction, mDecipherFunction, mSignatureTimestamp);
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
}
