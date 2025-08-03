package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistInfoItem;
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
public class PlaylistApiTest {
    private PlaylistApi mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.create(PlaylistApi.class);

        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());
    }

    @Test
    public void testThatPlaylistResultNotEmpty() {
        PlaylistsResult playlistsInfo = getPlaylistsInfo(TestHelpers.VIDEO_ID_AGE_RESTRICTED);

        assertTrue("Playlist info not empty", playlistsInfo != null && playlistsInfo.getPlaylists() != null);

        testFields(playlistsInfo.getPlaylists().get(0));
    }

    @Test
    public void testAddToPlaylist() throws InterruptedException {
        PlaylistsResult playlistsInfo = getPlaylistsInfo(TestHelpers.VIDEO_ID_AGE_RESTRICTED);
        PlaylistInfoItem firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        Call<ActionResult> wrapper = mService.editPlaylist(PlaylistApiHelper.getAddToPlaylistQuery(firstPlaylistItem.getPlaylistId(),
                TestHelpers.VIDEO_ID_AGE_RESTRICTED));

        ActionResult actionResult = RetrofitHelper.get(wrapper);

        assertNotNull("Action result success", actionResult);

        Thread.sleep(10_000);

        playlistsInfo = getPlaylistsInfo(TestHelpers.VIDEO_ID_AGE_RESTRICTED);
        firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        assertTrue("Action successful", firstPlaylistItem.isSelected());
    }

    @Test
    public void testRemoveFromPlaylist() throws InterruptedException {
        PlaylistsResult playlistsInfo = getPlaylistsInfo(TestHelpers.VIDEO_ID_AGE_RESTRICTED);
        PlaylistInfoItem firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        Call<ActionResult> wrapper = mService.editPlaylist(PlaylistApiHelper.getRemoveFromPlaylistsQuery(firstPlaylistItem.getPlaylistId(),
                TestHelpers.VIDEO_ID_AGE_RESTRICTED));

        ActionResult actionResult = RetrofitHelper.get(wrapper);

        assertNotNull("Action result success", actionResult);

        Thread.sleep(10_000);

        playlistsInfo = getPlaylistsInfo(TestHelpers.VIDEO_ID_AGE_RESTRICTED);
        firstPlaylistItem = playlistsInfo.getPlaylists().get(0);

        assertFalse("Action successful", firstPlaylistItem.isSelected());
    }

    private PlaylistsResult getPlaylistsInfo(String videoId) {
        Call<PlaylistsResult> wrapper = mService.getPlaylistsInfo(PlaylistApiHelper.getPlaylistsInfoQuery(videoId));

        return RetrofitHelper.get(wrapper);
    }

    private void testFields(PlaylistInfoItem playlist) {
        assertNotNull("Playlist contains title", playlist.getTitle());
        assertNotNull("Playlist contains id", playlist.getPlaylistId());
        assertNotNull("Playlist contains other data", playlist.getContainsSelected());
    }
}