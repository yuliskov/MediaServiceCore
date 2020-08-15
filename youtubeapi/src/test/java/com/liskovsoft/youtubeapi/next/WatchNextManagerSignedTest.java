package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.IOException;

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
    public void testThatWatchNextContainsAllRequiredFields() throws IOException {
        Call<WatchNextResult> wrapper =
                mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(TestHelpers.VIDEO_ID_SIMPLE), TestHelpers.getAuthorization());
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkWatchNextResultFields(watchNextResult);
    }
}