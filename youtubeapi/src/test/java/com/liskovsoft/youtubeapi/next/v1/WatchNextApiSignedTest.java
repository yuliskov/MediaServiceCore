package com.liskovsoft.youtubeapi.next.v1;

import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertNotNull;

@Ignore("Old api")
@RunWith(RobolectricTestRunner.class)
public class WatchNextApiSignedTest extends WatchNextManagerTestBase {
    private WatchNextApi mManager;

    @Before
    public void setUp() throws Exception {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mManager = RetrofitHelper.create(WatchNextApi.class);

        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());
    }

    @Test
    public void testThatWatchNextContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(TestHelpers.VIDEO_ID_SUBSCRIBED));
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkSignedWatchNextResultFields(watchNextResult);
    }

    @Test
    public void testThatWatchNextPlaylistItemContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(
                        TestHelpers.VIDEO_ID_MUSIC, TestHelpers.PLAYLIST_ID, TestHelpers.PLAYLIST_VIDEO_INDEX));
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkSignedPlaylistWatchNextResultFields(watchNextResult);
    }

    @Test
    public void testThatWatchNextNullPlaylistItemContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(
                        null, TestHelpers.PLAYLIST_ID, 0));
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
                mManager.continueWatchNextResult(BrowseApiHelper.getContinuationQuery(nextPageKey));

        return RetrofitHelper.get(wrapper);
    }

    private WatchNextResult getWatchNextResult() {
        Call<WatchNextResult> wrapper = mManager.getWatchNextResult(
                WatchNextApiHelper.getWatchNextQuery(TestHelpers.VIDEO_ID_CAPTIONS));
        return RetrofitHelper.get(wrapper);
    }
}