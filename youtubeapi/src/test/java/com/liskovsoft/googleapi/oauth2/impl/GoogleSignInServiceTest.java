package com.liskovsoft.googleapi.oauth2.impl;

import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.mediaserviceinterfaces.oauth.SignInService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import io.reactivex.disposables.Disposable;

@RunWith(RobolectricTestRunner.class)
public class GoogleSignInServiceTest {
    private SignInService mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = GoogleSignInService.instance();
        RetrofitOkHttpHelper.setDisableCompression(true);
    }

    @Test
    public void signInTest() throws InterruptedException {
        Disposable result = mService.signInObserve()
                .subscribe(
                        signInCode -> {
                            System.out.println("Generated user code is " + signInCode.getSignInCode() + ", open this url to sign in " + signInCode.getSignInUrl());
                        },
                        error -> {
                            fail("Sign in error: " + error.getMessage());
                        },
                        () -> {
                            System.out.println("Sign in success!");
                        }
                );

        Thread.sleep(30_000);
    }
}