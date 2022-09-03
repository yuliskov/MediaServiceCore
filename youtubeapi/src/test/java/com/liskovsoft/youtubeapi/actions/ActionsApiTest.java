package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
import org.junit.Before;
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

        mActionsManager = RetrofitHelper.withJsonPath(ActionsApi.class);
    }

    @Test
    public void testThatLikeIsWorking() {
        Call<ActionResult> wrapper =
                mActionsManager.setLike(ActionsApiParams.getLikeActionQuery(TestHelpersV2.VIDEO_ID_CAPTIONS), TestHelpersV2.getAuthorization());

        ActionResult actionResult = RetrofitHelper.get(wrapper);
        assertNotNull("Like result not null", actionResult);
        assertNotNull("Like result is successful", actionResult.getVisitorData());
    }

    @Test
    public void testThatSubscribeIsWorking() {
        Call<ActionResult> wrapper =
                mActionsManager.subscribe(ActionsApiParams.getSubscribeActionQuery(TestHelpersV2.CHANNEL_ID_UNSUBSCRIBED, null), TestHelpersV2.getAuthorization());

        ActionResult actionResult = RetrofitHelper.get(wrapper);
        assertNotNull("Subscribe result not null", actionResult);
        assertNotNull("Subscribe result is successful", actionResult.getVisitorData());
    }
}