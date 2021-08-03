package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.browse.BrowseManagerParams;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
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
                mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(TestHelpersV2.VIDEO_ID_SUBSCRIBED), TestHelpersV2.getAuthorization());
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkSignedWatchNextResultFields(watchNextResult);
    }

    @Test
    public void testThatWatchNextPlaylistItemContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(
                        TestHelpersV2.VIDEO_ID_MUSIC, TestHelpersV2.PLAYLIST_ID, TestHelpersV2.PLAYLIST_VIDEO_INDEX), TestHelpersV2.getAuthorization());
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkSignedPlaylistWatchNextResultFields(watchNextResult);
    }

    @Test
    public void testThatWatchNextNullPlaylistItemContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(
                        null, TestHelpersV2.PLAYLIST_ID, 0), TestHelpersV2.getAuthorization());
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
                mManager.continueWatchNextResult(BrowseManagerParams.getContinuationQuery(nextPageKey), TestHelpersV2.getAuthorization());

        return RetrofitHelper.get(wrapper);
    }

    private WatchNextResult getWatchNextResult() {
        Call<WatchNextResult> wrapper = mManager.getWatchNextResult(
                WatchNextManagerParams.getWatchNextQuery(TestHelpersV2.VIDEO_ID_CAPTIONS), TestHelpersV2.getAuthorization());
        return RetrofitHelper.get(wrapper);
    }
}