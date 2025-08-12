package com.liskovsoft.youtubeapi.videoinfo;

import static org.junit.Assert.assertTrue;

import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

import org.junit.Before;
import org.junit.Test;

/**
 * NOTE: testing with Duktape (native libs)!!!
 */
public class VideoInfoApiSignedTest extends BaseVideoInfoApiTest {
    @Before
    public void setUp() {
        initBase();
        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());
        RetrofitOkHttpHelper.getAuthHeaders().put("X-Goog-Pageid", TestHelpers.getPageIdToken());
    }

    @Test
    public void testThatAgeRestrictedVideoContainsRequiredFields() {
        testThatNonLiveVideoInfoContainsRequiredFields(getVideoInfo(AppClient.TV, TestHelpers.VIDEO_ID_AGE_RESTRICTED));
    }

    @Test
    public void testThatUnavailableVideoContainsRequiredFields() {
        testThatNonLiveVideoInfoContainsRequiredFields(getVideoInfo(AppClient.TV, TestHelpers.VIDEO_ID_UNAVAILABLE));
    }

    @Test
    public void testThatLiveVideoContainsSpecificFields()  {
        testThatLiveVideoContainsSpecificFields(getVideoInfo(AppClient.TV, TestHelpers.VIDEO_ID_LIVE));
    }

    @Test
    public void testThatVideoWithCaptionsContainsRequiredFields() {
        testThatVideoWithCaptionsContainsRequiredFields(getVideoInfo(AppClient.TV, TestHelpers.VIDEO_ID_CAPTIONS));
    }

    @Test
    public void initialResponseTest() {
        testThatVideoInfoContainsRequiredFields(InitialResponse.getVideoInfo(TestHelpers.VIDEO_ID_MUSIC_2, false));
    }

    @Test
    public void testThatClientsHaveNonEmptyResponses() {
        for (AppClient client : AppClient.values()) {
            if (client.isPlayerQueryBroken()) {
                continue;
            }

            VideoInfo result = getVideoInfo(client, TestHelpers.VIDEO_ID_KIDS);
            assertTrue("Result not null for client " + client.name(), result != null && !result.isUnplayable());
            testThatVideoInfoContainsRequiredFields(result);
        }
    }
}