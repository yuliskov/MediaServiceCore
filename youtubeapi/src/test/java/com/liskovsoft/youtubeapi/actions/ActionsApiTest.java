package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class ActionsApiTest {
    private ActionsApi mActionsManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mActionsManager = RetrofitHelper.create(ActionsApi.class);

        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());
    }

    @Test
    public void testThatLikeIsWorking() {
        testThatLikeIsWorking(TestHelpers.VIDEO_ID_3);
        testThatLikeIsWorking(TestHelpers.VIDEO_ID_CAPTIONS);
    }

    @Ignore("Error 429, Resource has been exhausted (e.g. check quota)")
    @Test
    public void testThatSubscribeIsWorking() throws InterruptedException {
        // Error 429, Resource has been exhausted (e.g. check quota)
        //Thread.sleep(10_000);

        testThatSubscribeIsWorking(TestHelpers.CHANNEL_ID_UNSUBSCRIBED);
    }

    private void testThatLikeIsWorking(String videoId) {
        Call<ActionResult> wrapper =
                mActionsManager.setLike(ActionsApiHelper.getLikeActionQuery(videoId));

        ActionResult actionResult = RetrofitHelper.get(wrapper);
        assertNotNull("Like result not null", actionResult);
    }

    private void testThatSubscribeIsWorking(String channelId) {
        Call<ActionResult> wrapper =
                mActionsManager.subscribe(ActionsApiHelper.getSubscribeActionQuery(channelId, null));

        ActionResult actionResult = RetrofitHelper.get(wrapper);
        assertNotNull("Subscribe result not null", actionResult);
    }
}