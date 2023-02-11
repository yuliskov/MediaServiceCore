package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class YouTubeMediaServiceTest {
    private MediaService mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        mService = YouTubeMediaService.instance();
    }

    /**
     * <a href="https://www.ibm.com/developerworks/ru/library/j-5things5/index.html">More info about concurrent utils...</a>
     */
    @Test
    public void testThatSearchNotEmpty() {
        MediaGroup mediaGroup = mService.getMediaGroupService().getSearch("hello world");

        List<MediaItem> list = new ArrayList<>();

        MediaItem mediaItem = mediaGroup.getMediaItems().get(0);
        list.add(mediaItem);
        assertNotNull(mediaItem);

        assertTrue("Has media items", list.size() > 0);
    }
    
    @Test
    public void testThatRecommendedNotEmpty() throws InterruptedException {
        MediaGroup mediaGroup = mService.getMediaGroupService().getRecommended();

        List<MediaItem> list = new ArrayList<>();

        MediaItem mediaItem = mediaGroup.getMediaItems().get(0);
        list.add(mediaItem);
        assertNotNull(mediaItem);

        assertTrue("Has media items", list.size() > 0);
    }

    @Test
    public void testThatRecommendedNotEmpty2() {
        MediaGroup result = mService.getMediaGroupService().getRecommended();

        assertTrue("Has media items", !result.isEmpty());
    }
}