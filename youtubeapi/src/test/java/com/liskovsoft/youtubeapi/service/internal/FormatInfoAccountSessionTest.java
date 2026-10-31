package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FormatInfoAccountSessionTest {
    private static final String VIDEO_ID = "synthetic-video";

    @Before
    public void clearCache() {
        FormatInfoWrapper.invalidateCache();
    }

    @After
    public void resetCache() {
        FormatInfoWrapper.invalidateCache();
    }

    @Test
    public void trackingParametersAreSetBeforeTheEntryIsVisible() throws Exception {
        AtomicReference<String> trackingParameters = new AtomicReference<>();
        MediaItemFormatInfo format = format(trackingParameters, () -> assertNull(cached(VIDEO_ID)));

        cache(format, "synthetic-click-parameters", AccountSession.getGeneration());

        assertEquals("synthetic-click-parameters", trackingParameters.get());
        assertSame(format, cached(VIDEO_ID));
    }

    @Test
    public void sameVideoDoesNotReusePreviousAccountsMetadata() throws Exception {
        MediaItemFormatInfo format = format(new AtomicReference<>(), () -> {});
        cache(format, null, AccountSession.getGeneration());
        assertSame(format, cached(VIDEO_ID));
        assertNull(cached("different-video"));

        AccountSession.invalidate();

        assertNull(cached(VIDEO_ID));
    }

    @Test
    public void delayedOldResultIsUnavailableToTheNewAccount() throws Exception {
        long oldGeneration = AccountSession.getGeneration();
        AccountSession.invalidate();
        MediaItemFormatInfo oldFormat = format(new AtomicReference<>(), () -> {});
        cache(oldFormat, null, oldGeneration);
        assertNull(cached(VIDEO_ID));

        MediaItemFormatInfo currentFormat = format(new AtomicReference<>(), () -> {});
        cache(currentFormat, null, AccountSession.getGeneration());

        assertSame(currentFormat, cached(VIDEO_ID));
    }

    private interface OnTrackingParameters {
        void run() throws Exception;
    }

    private static MediaItemFormatInfo format(AtomicReference<String> trackingParameters, OnTrackingParameters onSet) {
        return (MediaItemFormatInfo) Proxy.newProxyInstance(MediaItemFormatInfo.class.getClassLoader(),
                new Class<?>[] {MediaItemFormatInfo.class}, (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "isUnplayable":
                            return false;
                        case "isCacheActual":
                            return true;
                        case "getVideoId":
                            return VIDEO_ID;
                        case "setClickTrackingParams":
                            onSet.run();
                            trackingParameters.set((String) args[0]);
                            return null;
                        default:
                            throw new AssertionError("Unexpected format access: " + method.getName());
                    }
                });
    }

    private static MediaItemFormatInfo cached(String videoId) throws Exception {
        Method method = FormatInfoWrapper.class.getDeclaredMethod("getCachedFormatInfo", String.class);
        method.setAccessible(true);
        return (MediaItemFormatInfo) method.invoke(FormatInfoWrapper.INSTANCE, videoId);
    }

    private static void cache(MediaItemFormatInfo format, String trackingParameters, long generation) throws Exception {
        Method method = FormatInfoWrapper.class.getDeclaredMethod("setCachedFormatInfo", MediaItemFormatInfo.class, String.class, long.class);
        method.setAccessible(true);
        method.invoke(FormatInfoWrapper.INSTANCE, format, trackingParameters, generation);
    }
}
