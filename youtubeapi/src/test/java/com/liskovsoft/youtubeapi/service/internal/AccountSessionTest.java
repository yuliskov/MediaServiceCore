package com.liskovsoft.youtubeapi.service.internal;

import org.junit.Test;

import java.util.concurrent.CancellationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountSessionTest {
    @Test
    public void cachedMetadataIsUnavailableAfterAccountSwitch() {
        AccountSession.Value<String> metadata = new AccountSession.Value<>(AccountSession.getGeneration(), "account-a-metadata");
        assertEquals("account-a-metadata", metadata.get());

        AccountSession.invalidate();

        assertNull(metadata.get());
    }

    @Test
    public void delayedResultCannotPopulateTheNewAccountsCache() {
        long requestGeneration = AccountSession.getGeneration();
        AccountSession.invalidate();

        AccountSession.Value<String> lateResult = new AccountSession.Value<>(requestGeneration, "account-a-result");
        AccountSession.Value<String> currentResult = new AccountSession.Value<>(AccountSession.getGeneration(), "account-b-result");

        assertNull(lateResult.get());
        assertEquals("account-b-result", currentResult.get());
    }

    @Test(expected = CancellationException.class)
    public void switchingAwayAndBackDoesNotReviveAnOldOperation() {
        long firstSession = AccountSession.getGeneration();
        AccountSession.invalidate();
        AccountSession.invalidate();

        AccountSession.checkCurrent(firstSession);
    }
}
