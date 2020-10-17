package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.playlist.models.Playlist;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

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
        Call<PlaylistsInfo> wrapper = mService.getPlaylistsInfo(PlaylistManagerParams.getAllPlaylistsQuery(TestHelpers.VIDEO_ID_AGE_RESTRICTED),
                TestHelpers.getAuthorization());

        PlaylistsInfo playlistsInfo = RetrofitHelper.get(wrapper);

        assertTrue("Playlist info not empty", playlistsInfo != null && playlistsInfo.getPlaylists() != null);

        testFields(playlistsInfo.getPlaylists().get(0));
    }

    private void testFields(Playlist playlist) {
        assertNotNull("Playlist contains title", playlist.getTitle());
        assertNotNull("Playlist contains id", playlist.getPlaylistId());
        assertNotNull("Playlist contains other data", playlist.getContainsSelected());
    }
}