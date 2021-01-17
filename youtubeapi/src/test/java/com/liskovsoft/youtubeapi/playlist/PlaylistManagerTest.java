package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistInfo;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class PlaylistManagerTest {
    private PlaylistManager mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(PlaylistManager.class);
    }

    @Test
    public void testThatPlaylistResultNotEmpty() {
        PlaylistsResult playlistsInfo = getPlaylistsInfo(TestHelpersV2.VIDEO_ID_AGE_RESTRICTED);

        assertTrue("Playlist info not empty", playlistsInfo != null && playlistsInfo.getPlaylists() != null);

        testFields(playlistsInfo.getPlaylists().get(0));
    }

    @Test
    public void testAddToPlaylist() throws InterruptedException {
        Thread.sleep(10_000);

        PlaylistsResult playlistsInfo = getPlaylistsInfo(TestHelpersV2.VIDEO_ID_AGE_RESTRICTED);
        PlaylistInfo firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        Call<ActionResult> wrapper = mService.editPlaylist(PlaylistManagerParams.getAddToPlaylistQuery(firstPlaylistItem.getPlaylistId(),
                TestHelpersV2.VIDEO_ID_AGE_RESTRICTED), TestHelpersV2.getAuthorization());

        ActionResult actionResult = RetrofitHelper.get(wrapper);

        assertNotNull("Action result success", actionResult);

        Thread.sleep(10_000);

        playlistsInfo = getPlaylistsInfo(TestHelpersV2.VIDEO_ID_AGE_RESTRICTED);
        firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        assertTrue("Action successful", firstPlaylistItem.isSelected());
    }

    @Test
    public void testRemoveFromPlaylist() throws InterruptedException {
        Thread.sleep(10_000);

        PlaylistsResult playlistsInfo = getPlaylistsInfo(TestHelpersV2.VIDEO_ID_AGE_RESTRICTED);
        PlaylistInfo firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        Call<ActionResult> wrapper = mService.editPlaylist(PlaylistManagerParams.getRemoveFromPlaylistsQuery(firstPlaylistItem.getPlaylistId(),
                TestHelpersV2.VIDEO_ID_AGE_RESTRICTED), TestHelpersV2.getAuthorization());

        ActionResult actionResult = RetrofitHelper.get(wrapper);

        assertNotNull("Action result success", actionResult);

        Thread.sleep(10_000);

        playlistsInfo = getPlaylistsInfo(TestHelpersV2.VIDEO_ID_AGE_RESTRICTED);
        firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        assertFalse("Action successful", firstPlaylistItem.isSelected());
    }

    private PlaylistsResult getPlaylistsInfo(String videoId) {
        Call<PlaylistsResult> wrapper = mService.getPlaylistsInfo(PlaylistManagerParams.getAllPlaylistsQuery(videoId),
                TestHelpersV2.getAuthorization());

        return RetrofitHelper.get(wrapper);
    }

    private void testFields(PlaylistInfo playlist) {
        assertNotNull("Playlist contains title", playlist.getTitle());
        assertNotNull("Playlist contains id", playlist.getPlaylistId());
        assertNotNull("Playlist contains other data", playlist.getContainsSelected());
    }
}