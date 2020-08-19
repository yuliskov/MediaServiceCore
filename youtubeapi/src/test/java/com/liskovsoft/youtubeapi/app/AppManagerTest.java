package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfoResult;
import com.liskovsoft.youtubeapi.app.models.ClientPlaybackNonceFunctionResult;
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
        String playerUrl = getPlayerUrl();
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }

    @Test
    public void testThatDecipherFunctionIsValid() {
        String playerUrl = getPlayerUrl();

        Call<DecipherFunctionResult> wrapper = mManager.getDecipherFunction(playerUrl);
        DecipherFunctionResult decipherFunction = RetrofitHelper.get(wrapper);

        assertNotNull("Decipher result not null", decipherFunction);

        String decipherFunctionContent = decipherFunction.getContent();
        assertNotNull("Decipher function not null", decipherFunctionContent);
        assertFalse("Decipher function is not empty", decipherFunctionContent.isEmpty());
        assertTrue("Decipher function has proper content",
                decipherFunctionContent.startsWith("var ") && decipherFunctionContent.contains("function ") &&
                        decipherFunctionContent.endsWith(".join(\"\")}"));
    }

    @Test
    public void testThatPlaybackNonceFunctionIsValid() {
        String playerUrl = getPlayerUrl();

        Call<ClientPlaybackNonceFunctionResult> wrapper = mManager.getClientPlaybackNonceFunction(playerUrl);
        ClientPlaybackNonceFunctionResult clientPlaybackNonceFunction = RetrofitHelper.get(wrapper);

        assertNotNull("Playback nonce result not null", clientPlaybackNonceFunction);

        String playbackNonceFunctionContent = clientPlaybackNonceFunction.getContent();
        assertNotNull("Playback nonce function not null", playbackNonceFunctionContent);
        assertFalse("Playback nonce function not empty", playbackNonceFunctionContent.isEmpty());
        assertTrue("Playback nonce has valid content", playbackNonceFunctionContent.startsWith(";function ") &&
                playbackNonceFunctionContent.contains("\nfunction ") && playbackNonceFunctionContent.endsWith(".join(\"\")}"));
    }

    private String getPlayerUrl() {
        Call<AppInfoResult> wrapper = mManager.getAppInfo(AppConstants.USER_AGENT_SAMSUNG_1);
        AppInfoResult appInfo = RetrofitHelper.get(wrapper);

        assertNotNull("AppInfo not null", appInfo);

        String playerUrl = appInfo.getPlayerUrl();

        assertNotNull("Player url not null", playerUrl);

        return AppConstants.SCRIPTS_URL_BASE + playerUrl.replace("\\/", "/");
    }
}