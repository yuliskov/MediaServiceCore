package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.browse.BrowseManagerParams;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.next.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.result.WatchNextResultContinuation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class WatchNextManagerSignedTest extends WatchNextManagerTestBase {
    private WatchNextManagerSigned mManager;

    @Before
    public void setUp() throws Exception {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mManager = RetrofitHelper.withJsonPath(WatchNextManagerSigned.class);
    }

    @Test
    public void testThatWatchNextContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(TestHelpers.VIDEO_ID_SUBSCRIBED), TestHelpers.getAuthorization());
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkSignedWatchNextResultFields(watchNextResult);
    }

    @Test
    public void testThatWatchNextPlaylistItemContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(
                        TestHelpers.MUSIC_VIDEO_ID, TestHelpers.MUSIC_VIDEO_PLAYLIST_ID, TestHelpers.MUSIC_VIDEO_INDEX), TestHelpers.getAuthorization());
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkSignedPlaylistWatchNextResultFields(watchNextResult);
    }

    @Test
    public void testThatWatchNextRowsCouldBeContinued() {
        WatchNextResult watchNextResult = getWatchNextResult();

        String rootNextPageKey = watchNextResult.getSuggestedSections().get(0).getNextPageKey();

        assertNotNull("Root contains next key", rootNextPageKey);

        WatchNextResultContinuation continuation = continueWatchNext(rootNextPageKey);

        String nextPageKey = continuation.getNextPageKey();

        assertNotNull("Continuations contains next key", nextPageKey);
    }

    private WatchNextResultContinuation continueWatchNext(String nextPageKey) {
        Call<WatchNextResultContinuation> wrapper =
                mManager.continueWatchNextResult(BrowseManagerParams.getContinuationQuery(nextPageKey), TestHelpers.getAuthorization());

        return RetrofitHelper.get(wrapper);
    }

    private WatchNextResult getWatchNextResult() {
        Call<WatchNextResult> wrapper = mManager.getWatchNextResult(
                WatchNextManagerParams.getWatchNextQuery(TestHelpers.VIDEO_ID_CAPTIONS), TestHelpers.getAuthorization());
        return RetrofitHelper.get(wrapper);
    }
}