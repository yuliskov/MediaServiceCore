package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfoResult;
import com.liskovsoft.youtubeapi.app.models.DecipherFunctionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AppManagerTest {
    private AppManager mManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mManager = RetrofitHelper.withRegExp(AppManager.class);
    }

    @Test
    public void testThatAppInfoContainsAllRequiredFields() throws IOException {
        AppInfoResult appInfo = getAppInfo();

        String playerUrl = appInfo.getPlayerUrl();
        assertNotNull("Player url not null", playerUrl);
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }

    @Test
    public void testThatDecipherFunctionNotNull() throws IOException {
        AppInfoResult appInfo = getAppInfo();

        String playerUrl = appInfo.getPlayerUrl();

        assertNotNull("Player url not null", playerUrl);

        Call<DecipherFunctionResult> wrapper = mManager.getDecipherFunction(AppConstants.SCRIPTS_URL_BASE + playerUrl.replace("\\/", "/"));
        DecipherFunctionResult decipherFunction = wrapper.execute().body();

        String decipherFunctionContent = decipherFunction.getContent();
        assertNotNull("Decipher function not null", decipherFunctionContent);
        assertFalse("Decipher function is not empty", decipherFunctionContent.isEmpty());
        assertTrue("Decipher function has proper signature", decipherFunctionContent.startsWith("function ") && decipherFunctionContent.endsWith("}"));
    }

    private AppInfoResult getAppInfo() throws IOException {
        Call<AppInfoResult> wrapper = mManager.getAppInfo(AppConstants.USER_AGENT_SAMSUNG_1);
        return wrapper.execute().body();
    }
}