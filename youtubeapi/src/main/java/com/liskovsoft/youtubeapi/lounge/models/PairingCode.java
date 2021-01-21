package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;

public class PairingCode {
    @RegExp(".*")
    private String mPairingCode;
    private String mPairingCodeAlt;

    public String getPairingCode() {
        if (mPairingCode == null) {
            return null;
        }

        // Format pairing code to XXX-XXX-XXX-XXX
        if (mPairingCodeAlt == null) {
            mPairingCodeAlt = AppHelper.insertSeparator(mPairingCode, " ", 3);
        }

        return mPairingCodeAlt;
    }
}
