package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.service.internal.AccountSession;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CancellationException;

import kotlin.Triple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrackingAccountSessionTest {
    @Test(expected = CancellationException.class)
    public void originalGenerationIsCheckedBeforeNonceLookupOrTransportAccess() {
        TrackingService service = new TrackingService(null);
        long generation = AccountSession.getGeneration();
        AccountSession.invalidate();

        service.updateWatchTime("synthetic-video", 20, 100, "event", "visitor", "of", generation);
    }

    @Test
    public void sameVideoInAnotherAccountStartsANewHistoryRecord() throws Exception {
        TrackingService service = new TrackingService(null);
        setPosition(service, AccountSession.getGeneration(), 45);
        assertFalse(needNewRecord(service));
        assertEquals(45f, oldPosition(service), 0);

        AccountSession.invalidate();

        assertTrue(needNewRecord(service));
        assertEquals(0f, oldPosition(service), 0);
    }

    @Test
    public void oldRequestCompletingAfterSwitchCannotSupplyTheNewAccountsPosition() throws Exception {
        TrackingService service = new TrackingService(null);
        long oldGeneration = AccountSession.getGeneration();
        AccountSession.invalidate();
        setPosition(service, oldGeneration, 45);

        assertTrue(needNewRecord(service));
        assertEquals(0f, oldPosition(service), 0);

        setPosition(service, AccountSession.getGeneration(), 12);
        assertFalse(needNewRecord(service));
        assertEquals(12f, oldPosition(service), 0);
    }

    private static void setPosition(TrackingService service, long generation, float position) throws Exception {
        Field field = TrackingService.class.getDeclaredField("mPosition");
        field.setAccessible(true);
        field.set(service, new AccountSession.Value<>(generation, new Triple<>("synthetic-video", position, System.currentTimeMillis())));
    }

    private static boolean needNewRecord(TrackingService service) throws Exception {
        Method method = TrackingService.class.getDeclaredMethod("needNewRecord", String.class);
        method.setAccessible(true);
        return (boolean) method.invoke(service, "synthetic-video");
    }

    private static float oldPosition(TrackingService service) throws Exception {
        Method method = TrackingService.class.getDeclaredMethod("getOldPositionSec", String.class, float.class);
        method.setAccessible(true);
        return (float) method.invoke(service, "synthetic-video", 20f);
    }
}
