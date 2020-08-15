package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.service.YouTubeMediaService;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleMPDBuilderInstrumentedTest {
    // Mafia: Definitive Edition - Official Story Trailer | Summer of Gaming 2020
    private static final String VIDEO_ID_SIMPLE = "s2lGEhSlOTY";

    // LINDEMANN - Mathematik ft. Haftbefehl (Official Video)
    private static final String VIDEO_ID_CIPHERED = "0YEZiDtnbdA";

    private MediaService mService;
    private OkHttpManager mOkHttpHelper;

    @Before
    public void setUp() {
        mService = YouTubeMediaService.instance();
        mOkHttpHelper = OkHttpManager.instance();
    }

    @Test
    public void testThatCipheredFormatIsValid() throws IOException {
        testVideoFormatUrl(VIDEO_ID_CIPHERED);
    }

    @Test
    public void testThatSimpleFormatIsValid() throws IOException {
        testVideoFormatUrl(VIDEO_ID_SIMPLE);
    }

    @Test
    public void testThatMpdNotEmpty() {
        MediaItemFormatInfo mediaItemDetails = mService.getMediaItemManager().getFormatInfo(VIDEO_ID_SIMPLE);

        assertTrue("Is dash", mediaItemDetails.containsDashInfo());

        InputStream mpdStream = mService.getMediaItemManager().getMpdStream(mediaItemDetails);

        assertNotNull("Mpd stream not null", mpdStream);
        String mpdContent = Helpers.toString(mpdStream);
        assertFalse("Mpd content not empty", mpdContent.isEmpty());
    }

    private MediaItemFormatInfo getMediaItemDetails() {
        List<MediaGroup> homeGroups = mService.getMediaGroupManager().getHomeGroup();

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

        Response response = mOkHttpHelper.doGetOkHttpRequest(url);

        assertNotNull("Video url is working", response);
    }
}