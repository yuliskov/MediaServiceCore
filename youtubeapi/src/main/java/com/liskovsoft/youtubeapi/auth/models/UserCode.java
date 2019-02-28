package com.liskovsoft.youtubeapi.auth.models;

import com.google.gson.annotations.SerializedName;

public class UserCode {
    /**
     * Example: AH-1Ng1K8lBZcrMezwnEzpZ23VwfE8Hn7HEuNL-GbQIMYrSZhBF1j3KZ-h47-bqBB4rC6-W9xG2WHM67ZGttecz_ALOZ-JoxDQ
     */
    @SerializedName("device_code")
    private String mDeviceCode;

    /**
     * Code to enter into the browser page at https://youtube.com/activate<br/>
     * Example: XWY-QRL-MNH
     */
    @SerializedName("user_code")
    private String mUserCode;

    @SerializedName("expires_in")
    private int mExpiresIn;

    @SerializedName("interval")
    private int mInterval;

    /**
     * Please use https://youtube.com/activate instead
     */
    @SerializedName("verification_url")
    private String mVerificationUrl;

    public String getDeviceCode() {
        return mDeviceCode;
    }

    public String getUserCode() {
        return mUserCode;
    }
}
