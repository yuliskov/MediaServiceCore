package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaItemDetails;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.service.YouTubeMediaService;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleMPDBuilderInstrumentedTest {
    private MediaService mService;

    @Before
    public void setUp() {
        mService = YouTubeMediaService.instance();
    }

    @Test
    public void testThatFormatInfoNotEmpty() {
        MediaItemDetails mediaItemDetails = getMediaItemDetails();

        assertNotNull("Format info not empty", mediaItemDetails);
        assertTrue("Format list not empty", mediaItemDetails.getAdaptiveFormats().size() > 0);
    }

    @Test
    public void testThatMpdNotEmpty() {
        MediaItemDetails mediaItemDetails = getMediaItemDetails();

        MPDBuilder builder = SimpleMPDBuilder.from(mediaItemDetails);

        InputStream mpdStream = builder.build();

        assertNotNull("Mpd stream not null", mpdStream);
        String mpdContent = Helpers.toString(mpdStream);
        assertFalse("Mpd content not empty", mpdContent.isEmpty());
    }

    private MediaItemDetails getMediaItemDetails() {
        MediaGroup homeGroup = mService.getMediaGroupManager().getHomeGroup();

        List<MediaItem> mediaItems = homeGroup.getNestedGroups().get(0).getMediaItems();

        assertTrue("Media item not empty", mediaItems != null && mediaItems.size() > 0);

        MediaItem mediaItem = mediaItems.get(0);

        return mService.getMediaItemManager().getMediaItemDetails(mediaItem);
    }
}