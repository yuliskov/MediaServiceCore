package com.liskovsoft.googlecommon.service.oauth;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class YouTubeAccountProfileTest {
    @Test
    public void accountsWithTheSameDisplayNameHaveSeparateProfiles() throws Exception {
        YouTubeAccount first = account("Shared name", "first@example.invalid", null);
        YouTubeAccount second = account("Shared name", "second@example.invalid", null);

        assertNotEquals(first.getProfileId(), second.getProfileId());
    }

    @Test
    public void brandedChannelsWithTheSameEmailHaveSeparateProfiles() throws Exception {
        YouTubeAccount first = account("Shared name", "owner@example.invalid", "page-one");
        YouTubeAccount second = account("Shared name", "owner@example.invalid", "page-two");

        assertNotEquals(first.getProfileId(), second.getProfileId());
    }

    @Test
    public void renamingAndRefreshingCredentialsPreservesProfileIdentity() throws Exception {
        YouTubeAccount first = account("Old name", "owner@example.invalid", "page-one");
        YouTubeAccount second = account("New name", "owner@example.invalid", "page-one");
        first.setRefreshToken("synthetic-old-token");
        second.setRefreshToken("synthetic-new-token");

        assertEquals(first.getProfileId(), second.getProfileId());
        assertEquals("youtube:page:page-one", first.getProfileId());
    }

    @Test
    public void incompleteAccountDoesNotUseNameOrCredentialsAsIdentity() throws Exception {
        YouTubeAccount account = account("Shared name", null, null);
        account.setRefreshToken("synthetic-token");

        assertNull(account.getProfileId());
    }

    private static YouTubeAccount account(String name, String email, String pageId) throws Exception {
        YouTubeAccount account = new YouTubeAccount();
        set(account, "mName", name);
        set(account, "mEmail", email);
        set(account, "mPageIdToken", pageId);
        return account;
    }

    private static void set(YouTubeAccount account, String name, String value) throws Exception {
        Field field = YouTubeAccount.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(account, value);
    }
}
