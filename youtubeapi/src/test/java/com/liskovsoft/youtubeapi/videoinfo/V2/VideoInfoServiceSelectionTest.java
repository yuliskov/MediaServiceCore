package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.videoinfo.LoginRequiredException;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.*;

public class VideoInfoServiceSelectionTest {
    private static final String REASON = "Sign in to confirm you're not a bot";

    private VideoInfo info(boolean login, boolean playable, boolean regular) {
        return new VideoInfo() {
            @Override public boolean isLoginRequired() { return login; }
            @Override public boolean isUnplayable() { return !playable; }
            @Override public String getPlayabilityStatus() { return REASON; }
            @Override public List<RegularVideoFormat> getRegularFormats() {
                return regular ? Collections.singletonList(new RegularVideoFormat()) : null;
            }
        };
    }

    @Test public void loginRestrictionPreservesReasonAfterBothFallbackPasses() {
        AtomicInteger calls = new AtomicInteger();
        try {
            VideoInfoService.firstPlayable(AppClient.WEB_EMBED, client -> {
                calls.incrementAndGet();
                return info(true, false, false);
            });
            fail("Expected a sign-in restriction");
        } catch (LoginRequiredException e) {
            assertEquals(REASON, e.getMessage());
            assertEquals(18, calls.get());
        }
    }

    @Test public void playableFallbackWinsOverEarlierLoginRestriction() {
        VideoInfo playable = info(false, true, false);
        assertSame(playable, VideoInfoService.firstPlayable(AppClient.WEB_EMBED,
                client -> client == AppClient.VISIONOS ? playable : info(true, false, false)));
    }

    @Test public void regularFormatsFallbackStillWorks() {
        VideoInfo regular = info(false, false, true);
        assertSame(regular, VideoInfoService.firstPlayable(AppClient.WEB_EMBED,
                client -> client == AppClient.IOS ? regular : info(true, false, false)));
    }

    @Test public void transportFailureStillReturnsNull() {
        assertNull(VideoInfoService.firstPlayable(AppClient.WEB_EMBED, client -> null));
    }

    @Test public void unrelatedRestrictionDoesNotBecomeLoginError() {
        assertNull(VideoInfoService.firstPlayable(AppClient.WEB_EMBED,
                client -> info(false, false, false)));
    }

    @Test public void restrictionDoesNotLeakIntoNextRequest() {
        try {
            VideoInfoService.firstPlayable(AppClient.WEB_EMBED, client -> info(true, false, false));
            fail("Expected a sign-in restriction");
        } catch (LoginRequiredException expected) {
            assertNull(VideoInfoService.firstPlayable(AppClient.WEB_EMBED, client -> null));
        }
    }
}
