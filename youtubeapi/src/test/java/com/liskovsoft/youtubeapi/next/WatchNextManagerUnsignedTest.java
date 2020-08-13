package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

@RunWith(RobolectricTestRunner.class)
public class WatchNextManagerUnsignedTest extends WatchNextManagerTestBase {
    private static final String VIDEO_ID = "npXw2ddniHM";
    private WatchNextManagerUnsigned mManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mManager = RetrofitHelper.withJsonPath(WatchNextManagerUnsigned.class);
    }

    @Test
    public void testThatWatchNextContainsAllRequiredFields() {
        Call<WatchNextResult> wrapper = mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(VIDEO_ID));
        WatchNextResult watchNextResult = RetrofitHelper.get(wrapper);

        checkWatchNextResultFields(watchNextResult);
    }
}