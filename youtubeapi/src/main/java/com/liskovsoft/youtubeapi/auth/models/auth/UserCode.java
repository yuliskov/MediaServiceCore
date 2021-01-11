package com.liskovsoft.youtubeapi.auth.models.auth;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class UserCode {
    /**
     * Example: AH-1Ng1K8lBZcrMezwnEzpZ23VwfE8Hn7HEuNL-GbQIMYrSZhBF1j3KZ-h47-bqBB4rC6-W9xG2WHM67ZGttecz_ALOZ-JoxDQ
     */
    @JsonPath("$.device_code")
    private String mDeviceCode;

    /**
     * Code to enter into the browser page at https://youtube.com/activate<br/>
     * Example: XWY-QRL-MNH
     */
    @JsonPath("$.user_code")
    private String mUserCode;
    private String mUserCodeAlt;

    @JsonPath("$.expires_in")
    private int mExpiresIn;

    @JsonPath("$.interval")
    private int mInterval;

    /**
     * Please use https://youtube.com/activate instead
     */
    @JsonPath("$.verification_url")
    private String mVerificationUrl;

    public String getDeviceCode() {
        return mDeviceCode;
    }

    public String getUserCode() {
        if (mUserCode == null) {
            return null;
        }

        // Make code more readable by removing unused characters
        if (mUserCodeAlt == null) {
            mUserCodeAlt = mUserCode.replace("-", " ");
        }

        return mUserCodeAlt;
    }
}
