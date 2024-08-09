package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.yt.ServiceManager;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemFormatInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YouTubeMPDBuilderTest {
    private ServiceManager mService;

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        Thread.sleep(3_000);

        mService = YouTubeServiceManager.instance();
    }

    @After
    public void tearDown() {
        YouTubeSignInService.instance().setAuthorizationHeader(null);
    }

    @Test
    public void testThatCipheredFormatValid() {
        testVideoFormatUrl(TestHelpersV1.VIDEO_ID_MUSIC_2);
    }

    @Test
    public void testThatSignedCipheredFormatValid() {
        YouTubeSignInService.instance().setAuthorizationHeader(TestHelpersV2.getAuthorization());

        testVideoFormatUrl(TestHelpersV1.VIDEO_ID_MUSIC_2);
    }

    @Test
    public void testThatSimpleFormatValid() throws IOException {
        testVideoFormatUrl(TestHelpersV1.VIDEO_ID_CAPTIONS);
    }

    @Test
    public void testThatMpdNotEmpty() {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemService().getFormatInfo(TestHelpersV1.VIDEO_ID_CAPTIONS);

        assertTrue("Is dash", mediaItemDetails.containsDashInfo());

        InputStream mpdStream = mediaItemDetails.createMpdStream();

        assertNotNull("Mpd stream not null", mpdStream);
        String mpdContent = Helpers.toString(mpdStream);
        assertFalse("Mpd content not empty", mpdContent.isEmpty());
    }

    private MediaItemFormatInfo getMediaItemDetails() {
        List<MediaGroup> homeGroups = mService.getContentService().getHome();

        List<MediaItem> mediaItems = homeGroups.get(0).getMediaItems();

        assertTrue("Media item not empty", mediaItems != null && mediaItems.size() > 0);

        MediaItem mediaItem = mediaItems.get(0);

        return mService.getMediaItemService().getFormatInfo(mediaItem);
    }

    private void testVideoFormatUrl(String videoId) {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemService().getFormatInfo(videoId);

        assertNotNull("Format info not empty", mediaItemDetails);
        assertTrue("Format list not empty", mediaItemDetails.getDashFormats().size() > 0);

        String url = mediaItemDetails.getDashFormats().get(0).getUrl();

        assertTrue("Video url is working", TestHelpersV1.urlExists(url));
    }
}