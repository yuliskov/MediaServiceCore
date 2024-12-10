package com.liskovsoft.youtubeapi.app.models.cached;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.ClientData;

public class ClientDataCached extends ClientData {
    private static final String DELIM = "%cd%";
    private final String mClientId;
    private final String mClientSecret;

    private ClientDataCached(String clientId, String clientSecret) {
        mClientId = clientId;
        mClientSecret = clientSecret;
    }

    public static ClientData fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.split(DELIM, spec);

        return new ClientDataCached(Helpers.parseStr(split, 0), Helpers.parseStr(split, 1));
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.merge(DELIM, mClientId, mClientSecret);
    }

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getClientSecret() {
        return mClientSecret;
    }
}
