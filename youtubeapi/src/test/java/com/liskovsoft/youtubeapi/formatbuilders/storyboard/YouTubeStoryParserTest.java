package com.liskovsoft.youtubeapi.formatbuilders.storyboard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class)
public class YouTubeStoryParserTest {
    private static final String REGULAR_VIDEO_SB = "https://i.ytimg.com/sb/Pk2oW4SDDxY/storyboard3_L$L/$N.jpg|48#27#100#10#10#0#default#vpw4l5h3xmm2AkCT6nMZbvFIyJw|80#45#90#10#10#2000#M$M#hCWDvBSbgeV52mPYmOHjgdLFI1o|160#90#90#5#5#2000#M$M#ys1MKEnwYXA1QAcFiugAA_cZ81Q";
    private static final String LIVE_VIDEO_SB = "https://i.ytimg.com/sb/CFsd4UxzpLo/storyboard_live_90_3x3_b1/M$M.jpg?rs=AOn4CLAa9egpicnNt15TtgKW270vNRy5Bw#159#90#3#3";

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output
    }

    @Test
    public void testRegularVideoSB() {
        YouTubeStoryParser.Storyboard storyboard = YouTubeStoryParser.from(REGULAR_VIDEO_SB).extractStory();

        assertNotNull("Has group size", storyboard.getGroupSize());
        assertTrue("Has group duration", storyboard.getGroupDurationMS() > 0);
    }

    @Test
    public void testLiveVideoSB() {
        YouTubeStoryParser storyParser = YouTubeStoryParser.from(LIVE_VIDEO_SB);
        storyParser.setSegmentDurationUs(1_000_000);
        YouTubeStoryParser.Storyboard storyboard = storyParser.extractStory();

        assertNotNull("Has group size", storyboard.getGroupSize());
        assertTrue("Has group duration", storyboard.getGroupDurationMS() > 0);
    }
}