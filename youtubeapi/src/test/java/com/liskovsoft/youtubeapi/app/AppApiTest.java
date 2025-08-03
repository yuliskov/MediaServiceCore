package com.liskovsoft.youtubeapi.app;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.liskovsoft.youtubeapi.app.models.ClientData;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class)
public class AppApiTest {
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
    public void testThatAppInfoContainsAllRequiredFields() {
        String playerUrl = mAppServiceInt.getPlayerUrl();
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }

    @Ignore("Robolectric doesn't support loading native libraries (*.so)")
    @Test
    public void testThatDecipherFunctionIsValid() {
        String playerUrl = mAppServiceInt.getPlayerUrl();

        mAppServiceInt.getPlayerDataExtractor(playerUrl).validate();
    }

    @Test
    public void testThatClientIdAndSecretNotEmpty() {
        ClientData clientData = getClientData();

        assertNotNull("Client id not empty", clientData.getClientId());
        assertNotNull("Client secret not empty", clientData.getClientSecret());
    }

    private String getBaseUrl() {
        String baseUrl = mAppServiceInt.getClientUrl();

        assertNotNull("Base url not null", baseUrl);

        return baseUrl;
    }

    private ClientData getClientData() {
        String baseUrl = getBaseUrl();

        ClientData clientData = mAppServiceInt.getClientData(baseUrl);

        assertNotNull("Base data not null", clientData);
        return clientData;
    }
}