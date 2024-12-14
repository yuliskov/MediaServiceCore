package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import org.junit.Before;
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
    private AppServiceInt mAppServiceInt;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mAppServiceInt = new AppServiceInt();
    }

    @Test
    public void testThatAppInfoContainsAllRequiredFields() throws IOException {
        String playerUrl = getPlayerUrl(DefaultHeaders.USER_AGENT_TV);
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }

    @Test
    public void testThatDecipherFunctionIsValid() {
        String playerUrl = getPlayerUrl(DefaultHeaders.USER_AGENT_TV);

        PlayerData playerData = mAppServiceInt.getPlayerData(playerUrl);

        assertNotNull("Decipher result not null", playerData);

        String decipherFunctionContent = playerData.getDecipherFunction();
        assertNotNull("Decipher function not null", decipherFunctionContent);
        assertFalse("Decipher function is not empty", decipherFunctionContent.isEmpty());
        assertTrue("Decipher function has proper content",
                Helpers.startsWithAny(decipherFunctionContent, ";var ", ";const ") && decipherFunctionContent.contains("function ") &&
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

    @Test
    public void testThatClientIdAndSecretNotEmpty() {
        ClientData clientData = getClientData(DefaultHeaders.USER_AGENT_TV);

        assertNotNull("Client id not empty", clientData.getClientId());
        assertNotNull("Client secret not empty", clientData.getClientSecret());
    }

    private String getPlayerUrl(String userAgent) {
        AppInfo appInfo = mAppServiceInt.getAppInfo(userAgent);

        assertNotNull("AppInfo not null", appInfo);

        String playerUrl = appInfo.getPlayerUrl();

        assertNotNull("Player url not null", playerUrl);

        return playerUrl;
    }

    private String getBaseUrl(String userAgent) {
        AppInfo appInfo = mAppServiceInt.getAppInfo(userAgent);

        assertNotNull("AppInfo not null", appInfo);

        String baseUrl = appInfo.getClientUrl();

        assertNotNull("Base url not null", baseUrl);

        return baseUrl;
    }

    private PlayerData getPlayerData(String userAgent) {
        String playerUrl = getPlayerUrl(userAgent);

        PlayerData playerData = mAppServiceInt.getPlayerData(playerUrl);

        assertNotNull("PlayerData not null", playerData);

        return playerData;
    }

    private ClientData getClientData(String userAgent) {
        String baseUrl = getBaseUrl(userAgent);

        ClientData clientData = mAppServiceInt.getClientData(baseUrl);

        assertNotNull("Base data not null", clientData);
        return clientData;
    }
}