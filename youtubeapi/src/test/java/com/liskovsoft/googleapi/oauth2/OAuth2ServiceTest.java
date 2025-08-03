package com.liskovsoft.googleapi.oauth2;

import static org.junit.Assert.assertTrue;

import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.models.auth.UserCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class)
public class OAuth2ServiceTest {
    private OAuth2Service mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = OAuth2Service.instance();
        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().clear();
    }

    @Test
    public void testThatUserCodeNotEmpty() {
        UserCode code = mService.getUserCode();
        assertTrue(notEmpty(code.getUserCode()));
        assertTrue(notEmpty(code.getDeviceCode()));
        assertTrue(notEmpty(code.getVerificationUrl()));
        assertTrue(code.getInterval() > 0);
        assertTrue(code.getExpiresIn() > 0);
    }

    private boolean notEmpty(String userCode) {
        System.out.println("Important code is: " + userCode);
        return userCode != null && userCode.length() > 5;
    }
}