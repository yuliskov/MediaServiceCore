package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.service.YouTubeMediaService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YouTubeMPDBuilderInstrumentedTest {
    private MediaService mService;

    @Before
    public void setUp() {
        mService = YouTubeMediaService.instance();
    }

    @Test
    public void testThatCipheredFormatIsValid() throws IOException {
        testVideoFormatUrl(TestHelpers.VIDEO_ID_CIPHERED);
    }

    @Test
    public void testThatSimpleFormatIsValid() throws IOException {
        testVideoFormatUrl(TestHelpers.VIDEO_ID_CAPTIONS);
    }

    @Test
    public void testThatMpdNotEmpty() {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemManager().getFormatInfo(TestHelpers.VIDEO_ID_CAPTIONS);

        assertTrue("Is dash", mediaItemDetails.containsDashInfo());

        InputStream mpdStream = mediaItemDetails.createMpdStream();

        assertNotNull("Mpd stream not null", mpdStream);
        String mpdContent = Helpers.toString(mpdStream);
        assertFalse("Mpd content not empty", mpdContent.isEmpty());
    }

    private MediaItemFormatInfo getMediaItemDetails() {
        List<MediaGroup> homeGroups = mService.getMediaGroupManager().getHome();

        List<MediaItem> mediaItems = homeGroups.get(0).getMediaItems();

        assertTrue("Media item not empty", mediaItems != null && mediaItems.size() > 0);

        MediaItem mediaItem = mediaItems.get(0);

        return mService.getMediaItemManager().getFormatInfo(mediaItem);
    }

    private void testVideoFormatUrl(String videoId) {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemManager().getFormatInfo(videoId);

        assertNotNull("Format info not empty", mediaItemDetails);
        assertTrue("Format list not empty", mediaItemDetails.getAdaptiveFormats().size() > 0);

        String url = mediaItemDetails.getAdaptiveFormats().get(0).getUrl();

        assertTrue("Video url is working", TestHelpers.urlExists(url));
    }
}