package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.ServiceManager;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

public class YouTubeUrlListBuilderTest {
    private ServiceManager mService;
    private MediaServiceData mMediaServiceData;

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        Thread.sleep(3_000);

        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mService = YouTubeServiceManager.instance();
        mService.invalidateCache();
        mMediaServiceData = MediaServiceData.instance();
        mMediaServiceData.setFormatEnabled(MediaServiceData.FORMATS_ALL, false);
        mMediaServiceData.setFormatEnabled(MediaServiceData.FORMATS_URL, true);
    }

    @After
    public void tearDown() {
        mMediaServiceData.setFormatEnabled(MediaServiceData.FORMATS_ADAPTIVE, true);
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

        List<String> urlList = mediaItemDetails.createUrlList();

        assertNotNull("Url list not empty", urlList);

        for (String url : urlList) {
            assertTrue("Video url is working", TestHelpers.urlExists(url));
        }
    }
}