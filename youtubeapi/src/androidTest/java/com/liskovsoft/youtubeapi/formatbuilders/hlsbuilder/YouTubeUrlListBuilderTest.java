package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.yt.ServiceManager;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1;
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YouTubeUrlListBuilderTest {
    private ServiceManager mService;

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        Thread.sleep(3_000);

        mService = YouTubeServiceManager.instance();
    }

    @Test
    public void testThatUrlListNotEmpty() {
        testUrlList(TestHelpersV1.VIDEO_ID_CAPTIONS);
    }

    @Test
    public void testThatCipheredUrlListNotEmpty() {
        testUrlList(TestHelpersV1.VIDEO_ID_MUSIC_2);
    }

    private void testUrlList(String videoId) {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemService().getFormatInfo(videoId);

        List<String> urlList = mediaItemDetails.createUrlList();

        assertNotNull("Url list not empty", urlList);

        for (String url : urlList) {
            assertTrue("Video url is working", TestHelpersV1.urlExists(url));
        }
    }
}