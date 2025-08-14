package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.ServiceManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

public class YouTubeMPDBuilderTest {
    private ServiceManager mService;

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        Thread.sleep(3_000);

        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mService = YouTubeServiceManager.instance();
    }

    @After
    public void tearDown() {
        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", null);
    }

    @Test
    public void testThatCipheredFormatValid() {
        testVideoFormatUrl(TestHelpers.VIDEO_ID_MUSIC_2);
    }

    @Test
    public void testThatSignedCipheredFormatValid() {
        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());

        testVideoFormatUrl(TestHelpers.VIDEO_ID_MUSIC_2);
    }

    @Test
    public void testThatSimpleFormatValid() throws IOException {
        testVideoFormatUrl(TestHelpers.VIDEO_ID_CAPTIONS);
    }

    @Test
    public void testThatMpdNotEmpty() {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemService().getFormatInfo(TestHelpers.VIDEO_ID_CAPTIONS);

        assertTrue("Is dash", mediaItemDetails.containsDashFormats());

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
        assertTrue("Format list not empty", mediaItemDetails.getAdaptiveFormats().size() > 0);

        String url = mediaItemDetails.getAdaptiveFormats().get(0).getUrl();

        assertTrue("Video url is not empty", url != null);
        assertTrue("Video url is working", TestHelpers.urlExists(url));
    }
}