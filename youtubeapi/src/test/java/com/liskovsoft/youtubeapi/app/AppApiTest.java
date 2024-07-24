package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AppApiTest {
    private static final String POTOKEN_INPUT = "102307470137119736718||104417232878503778010";
    private AppApiWrapper mAppApiWrapper;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mAppApiWrapper = new AppApiWrapper();
    }

    @Test
    public void testThatAppInfoContainsAllRequiredFields() throws IOException {
        String playerUrl = getPlayerUrl(DefaultHeaders.USER_AGENT_TV);
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }

    @Test
    public void testThatDecipherFunctionIsValid() {
        String playerUrl = getPlayerUrl(DefaultHeaders.USER_AGENT_TV);

        PlayerData playerData = mAppApiWrapper.getPlayerData(playerUrl);

        assertNotNull("Decipher result not null", playerData);

        String decipherFunctionContent = playerData.getDecipherFunction();
        assertNotNull("Decipher function not null", decipherFunctionContent);
        assertFalse("Decipher function is not empty", decipherFunctionContent.isEmpty());
        assertTrue("Decipher function has proper content",
                decipherFunctionContent.startsWith(";var ") && decipherFunctionContent.contains("function ") &&
                        decipherFunctionContent.endsWith(".join(\"\")}"));
    }

    @Test
    public void testThatPlaybackNonceFunctionIsValid() {
        PlayerData playerData = getPlayerData(DefaultHeaders.USER_AGENT_TV);

        String playbackNonceFunctionContent = playerData.getRawClientPlaybackNonceFunction();
        assertNotNull("Playback nonce function not null", playbackNonceFunctionContent);
        assertFalse("Playback nonce function not empty", playbackNonceFunctionContent.isEmpty());
        assertTrue("Playback nonce has valid content", playbackNonceFunctionContent.startsWith("function ") &&
                playbackNonceFunctionContent.contains("function getClientPlaybackNonce") && playbackNonceFunctionContent.endsWith("}"));
    }

    @Ignore
    @Test
    public void testThrottleFunctionIsValid() {
        PlayerData playerData = getPlayerData(DefaultHeaders.USER_AGENT_TV);

        String throttleFunction = playerData.getThrottleFunction();
        assertNotNull("Throttle function not null", throttleFunction);
        assertFalse("Throttle function not empty", throttleFunction.isEmpty());
        assertTrue("Throttle function has valid content", throttleFunction.startsWith("function throttleSignature")
                && throttleFunction.endsWith(".join(\"\")}"));
    }

    @Test
    public void testThatClientIdAndSecretNotEmpty() {
        ClientData clientData = getClientData(DefaultHeaders.USER_AGENT_TV);

        assertNotNull("Client id not empty", clientData.getClientId());
        assertNotNull("Client secret not empty", clientData.getClientSecret());
    }

    @Ignore
    @Test
    public void testPoTokenFunctionIsValid() {
        PlayerData playerData = getPlayerData(DefaultHeaders.USER_AGENT_TV);

        String poTokenFunction = playerData.getPoTokenFunction();
        assertNotNull("Player data contains poToken function", poTokenFunction);
        assertTrue("poToken function is valid", poTokenFunction.contains("getPoToken"));
        assertTrue("poToken function len is valid", poTokenFunction.length() < 700);
    }

    @Ignore
    @Test
    public void testPoTokenConcatFunctionIsValid() {
        PlayerData playerData = getPlayerData(DefaultHeaders.USER_AGENT_TV);

        String poTokenConcatFunction = playerData.getPoTokenConcatFunction();
        assertNotNull("Player data contains poToken function", poTokenConcatFunction);
        assertTrue("poToken function is valid", poTokenConcatFunction.contains("poTokenConcat"));
        assertTrue("poToken function len is valid", poTokenConcatFunction.length() < 700);
    }

    @Ignore
    @Test
    public void testPoTokenResultFunctionIsValid() {
        PlayerData playerData = getPlayerData(DefaultHeaders.USER_AGENT_TV);

        String poTokenResultFunction = playerData.getPoTokenResultFunction();
        assertNotNull("Player data contains poToken function", poTokenResultFunction);
        assertTrue("poToken function is valid", poTokenResultFunction.contains("getPoTokenResult"));
        assertTrue("poToken function len is valid", poTokenResultFunction.length() < 1500);
    }

    private String getPlayerUrl(String userAgent) {
        AppInfo appInfo = mAppApiWrapper.getAppInfo(userAgent);

        assertNotNull("AppInfo not null", appInfo);

        String playerUrl = appInfo.getPlayerUrl();

        assertNotNull("Player url not null", playerUrl);

        return playerUrl;
    }

    private String getBaseUrl(String userAgent) {
        AppInfo appInfo = mAppApiWrapper.getAppInfo(userAgent);

        assertNotNull("AppInfo not null", appInfo);

        String baseUrl = appInfo.getClientUrl();

        assertNotNull("Base url not null", baseUrl);

        return baseUrl;
    }

    private PlayerData getPlayerData(String userAgent) {
        String playerUrl = getPlayerUrl(userAgent);

        PlayerData playerData = mAppApiWrapper.getPlayerData(playerUrl);

        assertNotNull("PlayerData not null", playerData);

        return playerData;
    }

    private ClientData getClientData(String userAgent) {
        String baseUrl = getBaseUrl(userAgent);

        ClientData clientData = mAppApiWrapper.getClientData(baseUrl);

        assertNotNull("Base data not null", clientData);
        return clientData;
    }
}