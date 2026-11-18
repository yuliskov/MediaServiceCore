package com.liskovsoft.youtubeapi.lounge.models.info;

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

public class PairingCodeV2 {
    /**
     * get_pairing_code currently answers with text/plain containing only the digits (e.g. "836172621048").<br/>
     * The JSON form ({"code":"..."}) is still accepted in case the server switches back.
     */
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
