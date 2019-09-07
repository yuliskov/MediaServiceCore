package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class YouTubeVideoServiceTest {
    private YouTubeVideoService mService;

    @Before
    public void setUp() {
        mService = new YouTubeVideoService();
    }

    /**
     * <a href="https://www.ibm.com/developerworks/ru/library/j-5things5/index.html">More info about concurrent utils...</a>
     */
    @Test
    public void testThatFindReturnsMultiplePages() throws InterruptedException {
        Observable<List<Video>> result = mService.getSearchObserve("hello world");

        CountDownLatch finish = new CountDownLatch(2);

        List<Video> list = new ArrayList<>();

        result.subscribe(videos -> {
            Video video = videos.get(0);
            list.add(video);
            assertNotNull(video);
            finish.countDown();
        }, throwable -> fail());

        result.subscribe(videos -> {
            Video video = videos.get(0);
            Video video2 = list.get(0);
            assertTrue(!video.getTitle().equals(video2.getTitle()));
            finish.countDown();
        }, throwable -> fail());

        boolean await = finish.await(5_000, TimeUnit.MILLISECONDS);
        assertTrue("Counter not zero", await);
    }
}