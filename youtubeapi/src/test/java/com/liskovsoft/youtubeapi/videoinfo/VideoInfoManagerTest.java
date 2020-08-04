package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.browse.BrowseManager;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class VideoInfoManagerTest {
    private VideoInfoManager mService;
    private static final String VIDEO_ID = "npXw2ddniHM";

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        RetrofitHelper.sForceEnableProfiler = true;

        mService = RetrofitHelper.withQueryString(VideoInfoManager.class);
    }

    @Test
    public void testThatVideoInfoContainsAllRequiredFields() throws IOException {
        Call<VideoInfoResult> wrapper = mService.getVideoInfo(VIDEO_ID);
        VideoInfoResult result = wrapper.execute().body();

        assertNotNull("Result not null", result);
        List<AdaptiveFormat> formats = result.getAdaptiveFormats();
        assertTrue("Formats not empty", formats.size() > 0);
        assertNotNull("Contains range", formats.get(0).getIndexRange());
        assertTrue("Contains fps", formats.get(0).getFps() != 0);
        assertTrue("Contains bitrate", formats.get(0).getBitrate() != 0);
        assertNotNull("Has watch tracking url", result.getVideostatsWatchtimeUrl());
    }
}