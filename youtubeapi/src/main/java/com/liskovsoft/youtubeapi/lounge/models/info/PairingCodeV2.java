package com.liskovsoft.youtubeapi.lounge.models.info;

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

public class PairingCodeV2 {
    // Plain text body (current) or {"code": "..."} (older answers)
    @RegExp({"\"code\"\\s*:\\s*\"?(\\d+)", "^\\s*(\\d+)\\s*$"})
    private String mPairingCode;
    private String mPairingCodeAlt;

    public String getPairingCode() {
        if (mPairingCode == null) {
            return null;
        }

        // Format pairing code to XXX-XXX-XXX-XXX
        if (mPairingCodeAlt == null) {
            mPairingCodeAlt = ServiceHelper.insertSeparator(mPairingCode, " ", 3);
        }

        return mPairingCodeAlt;
    }
}
