package com.liskovsoft.youtubeapi.app;

import android.Manifest;
import android.text.TextUtils;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor;
import com.liskovsoft.youtubeapi.common.helpers.AppConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Note: Robolectric doesn't support loading native libraries (*.so)
 */
public class AppServiceTest {
    private AppService mAppService;

    @Rule
    public GrantPermissionRule mInternetPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET);

    @Before
    public void setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mAppService = AppService.instance();
    }

    /**
     * Note: Robolectric doesn't support loading native libraries (*.so)
     */
    @Test
    public void testThatItemsDecipheredCorrectly() {
        List<String> ciphered = new ArrayList<>();
        String cipher = "ADBVCGD2934FBBBBBDDDFFF";
        ciphered.add(cipher);
        ciphered.add(cipher);
        ciphered.add(cipher);

        List<String> deciphered = mAppService.extractSig(ciphered);

        assertNotNull("Deciphered not null", deciphered);
        assertFalse("Deciphered not empty", deciphered.isEmpty());

        for (String decipher : deciphered) {
             assertNotEquals("Cipher and decipher not the same", cipher, decipher);
        }
    }

    @Test
    public void testThatItemsDecipheredCorrectly2() {
        List<String> ciphered = new ArrayList<>();
        String cipher = "ADBVCGD2934FBBBBBDDDFFF";
        ciphered.add(cipher);
        ciphered.add(cipher);
        ciphered.add(cipher);

        PlayerDataExtractor extractor = new PlayerDataExtractor(AppConstants.playerUrls.get(0));

        List<String> deciphered = extractor.extractSig(ciphered);

        assertNotNull("Deciphered not null", deciphered);
        assertFalse("Deciphered not empty", deciphered.isEmpty());

        for (String decipher : deciphered) {
            assertNotEquals("Cipher and decipher not the same", cipher, decipher);
        }
    }

    //@Ignore("Removed in the newer versions")
    //@Test
    //public void testCipherExtractor() {
    //    for (String url : AppConstants.playerUrls) {
    //        PlayerDataExtractor dataExtractor = new PlayerDataExtractor(url);
    //
    //        assertNotNull("Cipher found for url: " + url, dataExtractor.extractCipher());
    //    }
    //}

    @Test
    public void testPlaybackNonce() {
        String playbackNonce = mAppService.getClientPlaybackNonce();

        assertTrue("Playback nonce not empty", playbackNonce != null && !playbackNonce.isEmpty());
    }

    @Test
    public void testThrottleFunction() {
        List<String> throttled = new ArrayList<>();
        String throttleSignature = "ADBVCGD2934FBBBBBDDDFFF";
        throttled.add(throttleSignature);
        throttled.add(throttleSignature);
        throttled.add(throttleSignature);
        List<String> normalized = mAppService.extractNSig(throttled);

        assertNotNull("Normalized not null", normalized);
        assertFalse("Normalized not empty", normalized.isEmpty());
        assertFalse("Normalized not empty", TextUtils.isEmpty(normalized.get(0)));
        assertNotEquals("Throttled not equals", throttled.get(0), normalized.get(0));
    }
}