package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.googlecommon.service.oauth.YouTubeAccount;
import com.liskovsoft.mediaserviceinterfaces.SignInService.OnAccountChange;
import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class YouTubeAccountRestoreTest {
    private final TestScheduler mScheduler = new TestScheduler();

    @Before
    public void setUp() {
        GlobalPreferences.sInstance = null;
        RxJavaPlugins.setIoSchedulerHandler(ignored -> mScheduler);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(ignored -> Schedulers.trampoline());
        RxAndroidPlugins.setMainThreadSchedulerHandler(ignored -> Schedulers.trampoline());
    }

    @After
    public void tearDown() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
        GlobalPreferences.sInstance = null;
    }

    @Test
    public void firstLookupRestoresLocalSelectionWithoutWaitingForBackgroundAuthentication() throws Exception {
        YouTubeAccountManager manager = manager();
        List<Account> delivered = new ArrayList<>();
        OnAccountChange listener = delivered::add;
        manager.addOnAccountChange(listener);
        assertNull(manager.getSelectedAccount());
        storeAccount("first@example.invalid");

        Account selected = manager.getSelectedAccount();
        assertEquals("youtube:email:first@example.invalid", selected.getProfileId());
        long restoredGeneration = AccountSession.getGeneration();
        manager.init();
        assertEquals(restoredGeneration, AccountSession.getGeneration());

        mScheduler.triggerActions();
        assertEquals(1, delivered.size());
        assertEquals(selected, delivered.get(0));
    }

    @Test
    public void queuedOldSelectionDoesNotOverrideNewAccountNotification() throws Exception {
        storeAccount("first@example.invalid");
        YouTubeAccountManager manager = manager();
        List<Account> delivered = new ArrayList<>();
        OnAccountChange listener = delivered::add;
        manager.addOnAccountChange(listener);
        manager.init();

        // Simulate a second selection before the first IO-to-main callback is delivered.
        manager.getAccounts().clear();
        Account second = syntheticAccount("second@example.invalid");
        manager.getAccounts().add(second);
        AccountSession.invalidate();
        Method notify = YouTubeAccountManager.class.getDeclaredMethod("notifyListeners");
        notify.setAccessible(true);
        notify.invoke(manager);

        mScheduler.triggerActions();

        assertEquals(1, delivered.size());
        assertEquals("youtube:email:second@example.invalid", delivered.get(0).getProfileId());
    }

    private static void storeAccount(String email) {
        GlobalPreferences.instance(RuntimeEnvironment.application)
                .setMediaServiceAccountData(Helpers.mergeArray(syntheticAccount(email)));
    }

    private static Account syntheticAccount(String email) {
        return YouTubeAccount.from(Helpers.mergeData(0, "Synthetic account", null, true,
                "synthetic-token", email, true, null, null));
    }

    private static YouTubeAccountManager manager() throws Exception {
        Constructor<YouTubeAccountManager> constructor = YouTubeAccountManager.class.getDeclaredConstructor(YouTubeSignInService.class);
        constructor.setAccessible(true);
        return constructor.newInstance((YouTubeSignInService) null);
    }
}
