package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemDetails;
import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Note: Robolectric doesn't support loading native libraries (*.so)
 */
public class YouTubeMediaServiceInstrumentedTest {
    private MediaService mService;

    @Before
    public void setUp() {
        mService = YouTubeMediaService.instance();
    }

    @Test
    public void testThatFormatInfoNotEmpty() {
        MediaGroup homeGroup = mService.getMediaGroupManager().getHomeGroup();

        List<MediaItem> mediaItems = homeGroup.getNestedGroups().get(0).getMediaItems();

        assertTrue("Media item not empty", mediaItems != null && mediaItems.size() > 0);

        MediaItem mediaItem = mediaItems.get(0);

        MediaItemDetails formatInfo = mService.getMediaItemManager().getMediaItemDetails(mediaItem);

        assertNotNull("Format info not empty", formatInfo);
        assertTrue("Format list not empty", formatInfo.getAdaptiveFormats().size() > 0);
    }
}