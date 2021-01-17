package com.liskovsoft.youtubeapi.feedback;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.feedback.models.FeedbackResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FeedbackManagerTest {
    private static final String SAMPLE_TOKEN = "AB9zfpIM9cPZbJlLPimo5tD6dMauYqUvQdwDIKB3mOBOnh1nwz5zztz28BZ75alUCedlk2Tfe6qzogkqvHWGY9rSEQNcMugsEAuapP5WDnszLi5GLCZg2D8uK2bSJbnGocCchc5KKoE2";
    private FeedbackManager mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(FeedbackManager.class);
    }

    @Test
    public void testThatFeedbackIsSuccessful() {
        Call<FeedbackResponse> wrapper = mService.setNotInterested(FeedbackManagerParams.getNotInterestedQuery(SAMPLE_TOKEN),
                TestHelpersV2.getAuthorization());

        FeedbackResponse feedbackResponse = RetrofitHelper.get(wrapper);

        assertTrue("Feedback successful", feedbackResponse.isFeedbackProcessed());
    }
}