package com.liskovsoft.youtubeapi.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class AppServiceTest {
    private AppService mAppService;

    @Before
    public void setUp() throws Exception {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mAppService = AppService.instance();
    }

    // Fail: Robolectric doesn't support loading native libraries
    @Test
    public void testThatItemsDecipheredCorrectly() {
        List<String> ciphered = new ArrayList<>();
        String cipher = "ADBVCGD2934FBBBBBDDDFFF";
        ciphered.add(cipher);
        ciphered.add(cipher);
        ciphered.add(cipher);

        List<String> deciphered = mAppService.decipher(ciphered);

        assertNotNull("Deciphered not null", deciphered);
        assertFalse("Deciphered not empty", deciphered.isEmpty());

        for (String decipher : deciphered) {
             assertNotEquals("Cipher and decipher not the same", decipher, cipher);
        }
    }
}