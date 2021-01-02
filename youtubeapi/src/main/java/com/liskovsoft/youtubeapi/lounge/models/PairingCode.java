package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class PairingCode {
    @RegExp(".*")
    private String mPairingCode;

    public String getPairingCode() {
        return mPairingCode;
    }
}
