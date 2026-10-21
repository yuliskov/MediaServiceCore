package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.ServiceManager;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

public class YouTubeUrlListBuilderTest {
    private ServiceManager mService;

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        Thread.sleep(3_000);

        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mService = YouTubeServiceManager.instance();
        mService.invalidateCache();
    }

    @Test
    public void testThatUrlListNotEmpty() {
        testUrlList(TestHelpers.VIDEO_ID_CAPTIONS);
    }

    @Test
    public void testThatCipheredUrlListNotEmpty() {
        testUrlList(TestHelpers.VIDEO_ID_MUSIC_2);
    }

    private void testUrlList(String videoId) {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemService().getFormatInfo(videoId);

        boolean isMusicVideo = Helpers.equals(videoId, TestHelpers.VIDEO_ID_MUSIC_2);
        boolean isTVClient = mediaItemDetails.getClientInfo() == AppClient.TV;
        assertTrue("Ciphered URL list is supported only for non-TV clients", !isMusicVideo || !isTVClient);

        List<String> urlList = mediaItemDetails.createUrlList();

        assertNotNull("Url list not empty", urlList);

        for (String url : urlList) {
            assertTrue("Video url is working. Client: " + mediaItemDetails.getClientInfo().getClientName() + " Url: "  + url, TestHelpers.urlExists(url));
        }
    }
}