package com.liskovsoft.youtubeapi.lounge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class LoungeServiceTest {
    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output
    }

    @Test
    public void testCommandPackaging() {
        Map<String, String> onVolumeChanged = CommandParams.getOnVolumeChanged(50);
        Map<String, String> nowPlaying =
                CommandParams.getNowPlaying("videoId", 0, 90_000, 1, "ctt", "playlistId", "playlistIndex");

        Map<String, String> packagedCommands = CommandParams.packageCommands(onVolumeChanged, nowPlaying, null, null);

        Assert.assertEquals("Has proper command length", packagedCommands.get("count"), String.valueOf(2));
        String ofs = packagedCommands.get("ofs");
        Assert.assertTrue("Has proper command offset", ofs != null && Integer.parseInt(ofs) >= 0);

        Map<String, String> packagedCommands2 = CommandParams.packageCommands(null, null);
        Assert.assertNull("Packed result is null", packagedCommands2);
    }

    @Test
    public void testIsInterrupted() {
        Assert.assertTrue("Direct InterruptedException is detected",
                LoungeService.isInterrupted(new InterruptedException()));
        Assert.assertTrue("Wrapped InterruptedIOException from RetrofitHelper is detected",
                LoungeService.isInterrupted(new IllegalStateException(new InterruptedIOException("thread interrupted"))));
        Assert.assertFalse("Wrapped SocketTimeoutException is not treated as thread interruption",
                LoungeService.isInterrupted(new IllegalStateException(new SocketTimeoutException("Read timed out"))));
        Assert.assertFalse("Wrapped SSLHandshakeException is not treated as thread interruption",
                LoungeService.isInterrupted(new IllegalStateException(new SSLHandshakeException("Connection reset by peer"))));
    }
}