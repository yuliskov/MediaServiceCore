package com.liskovsoft.googlecommon.common.models.auth;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

/**
 * Manual: https://developers.google.com/identity/protocols/oauth2/limited-input-device#success-response
 */
public class UserCode {
    /**
     * Example: AH-1Ng1K8lBZcrMezwnEzpZ23VwfE8Hn7HEuNL-GbQIMYrSZhBF1j3KZ-h47-bqBB4rC6-W9xG2WHM67ZGttecz_ALOZ-JoxDQ
     */
    @JsonPath("$.device_code")
    private String mDeviceCode;

    /**
     * Code to enter into the browser page at mVerificationUrl<br/>
     * Example: XWY-QRL-MNH
     */
    @JsonPath("$.user_code")
    private String mUserCode;

    @JsonPath("$.expires_in")
    private int mExpiresIn;

    /**
     * In seconds to poll the server
     */
    @JsonPath("$.interval")
    private int mInterval;
    
    @JsonPath("$.verification_url")
    private String mVerificationUrl;

    private String mUserCodePretty;

    public String getDeviceCode() {
        return mDeviceCode;
    }

    public String getUserCode() {
        if (mUserCode == null) {
            return null;
        }

        // Make code more readable by removing unused characters
        if (mUserCodePretty == null) {
            mUserCodePretty = mUserCode.replace("-", " ");
        }

        return mUserCodePretty;
    }

    public String getVerificationUrl() {
        return mVerificationUrl;
    }

    public int getInterval() {
        return mInterval;
    }

    public int getExpiresIn() {
        return mExpiresIn;
    }
}
