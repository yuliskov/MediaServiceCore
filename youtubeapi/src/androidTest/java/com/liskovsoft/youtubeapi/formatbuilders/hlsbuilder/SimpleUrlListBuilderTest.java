package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.MediaItemDetails;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.youtubeapi.service.YouTubeMediaService;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class SimpleUrlListBuilderTest {
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
    public void testThatUrlListNotEmpty() {
        testUrlList(VIDEO_ID_SIMPLE);
    }

    @Test
    public void testThatCipheredUrlListNotEmpty() {
        testUrlList(VIDEO_ID_CIPHERED);
    }

    private void testUrlList(String videoId) {
        MediaItemDetails mediaItemDetails = mService.getMediaItemManager().getMediaItemDetails(videoId);

        List<String> urlList = mService.getMediaItemManager().getUrlList(mediaItemDetails);

        assertNotNull("Url list not empty", urlList);

        for (String url : urlList) {
            testUrl(url);
        }
    }

    private void testUrl(String url) {
        Response response = mOkHttpHelper.doGetOkHttpRequest(url);

        assertNotNull("Video url is working", response);
    }
}