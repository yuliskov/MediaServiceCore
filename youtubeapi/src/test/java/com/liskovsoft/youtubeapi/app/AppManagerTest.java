package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.IOException;

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
    public void testThatPlayerInfoContainsAllRequiredFields() throws IOException {
        Call<AppInfo> wrapper = mManager.getAppInfo(AppConstants.USER_AGENT_SAMSUNG_1);
        AppInfo playerInfo = wrapper.execute().body();

        String playerUrl = playerInfo.getPlayerUrl();
        assertNotNull("Player url not null", playerUrl);
        assertTrue("Player url should ends with js", playerUrl.endsWith(".js"));
    }
}