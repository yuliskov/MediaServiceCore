package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.youtubeapi.service.internal.AccountSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class HistoryAccountSessionTest {
    @Before
    public void useJvmScheduler() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(ignored -> Schedulers.trampoline());
        RxAndroidPlugins.setMainThreadSchedulerHandler(ignored -> Schedulers.trampoline());
    }

    @After
    public void resetScheduler() {
        RxAndroidPlugins.reset();
    }

    @Test
    public void historyWriteCreatedBeforeSwitchFailsBeforeAuthenticationOrNetworkAccess() {
        Observable<Void> update = YouTubeMediaItemService.instance().updateHistoryPositionObserve("synthetic-video", 20);

        AccountSession.invalidate();

        update.test().awaitDone(5, TimeUnit.SECONDS).assertError(CancellationException.class).assertNotComplete();
    }

    @Test
    public void switchingAwayAndBackStillRejectsQueuedHistoryWrite() {
        Observable<Void> update = YouTubeMediaItemService.instance().updateHistoryPositionObserve("synthetic-video", 20);

        AccountSession.invalidate();
        AccountSession.invalidate();

        update.test().awaitDone(5, TimeUnit.SECONDS).assertError(CancellationException.class).assertNotComplete();
    }
}
