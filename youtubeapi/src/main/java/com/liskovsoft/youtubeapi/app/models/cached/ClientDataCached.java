package com.liskovsoft.youtubeapi.app.models.cached;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.ClientData;

public class ClientDataCached extends ClientData {
    private static final String DELIM = "%cdc%";
    private final String mClientId;
    private final String mClientSecret;
    private final String mClientUrl;

    private ClientDataCached(String clientUrl, String clientId, String clientSecret) {
        mClientUrl = clientUrl;
        mClientId = clientId;
        mClientSecret = clientSecret;
    }

    public static ClientDataCached fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.split(spec, DELIM);

        return new ClientDataCached(Helpers.parseStr(split, 0), Helpers.parseStr(split, 1), Helpers.parseStr(split, 2));
    }

    public static ClientDataCached from(String clientUrl, ClientData clientData) {
        if (clientData == null) {
            return null;
        }

        return new ClientDataCached(clientUrl, clientData.getClientId(), clientData.getClientSecret());
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.merge(DELIM, mClientUrl, mClientId, mClientSecret);
    }

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getClientSecret() {
        return mClientSecret;
    }

    public String getClientUrl() {
        return mClientUrl;
    }

    public boolean validate() {
        return mClientId != null && mClientSecret != null;
    }
}
