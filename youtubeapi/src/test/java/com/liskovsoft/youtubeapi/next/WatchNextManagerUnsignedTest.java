package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.next.models.WatchNextItem;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.next.models.WatchNextSection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class WatchNextManagerUnsignedTest {
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

        assertNotNull("Watch next not empty", watchNextResult);
        List<WatchNextSection> allRows = watchNextResult.getSections();
        assertNotNull("Watch next contains rows", allRows);

        WatchNextSection firstRow = allRows.get(0);

        assertFalse("Row has title", firstRow.getTitle().isEmpty());

        WatchNextItem watchNextItem = firstRow.getWatchNextItems().get(0);

        testFields(watchNextItem);
    }

    private void testFields(WatchNextItem watchNextItem) {
        assertFalse("Watch next item has title", watchNextItem.getTitle().isEmpty());
        assertFalse("Watch next item has video id", watchNextItem.getVideoId().isEmpty());
        assertFalse("Watch next item has user name", watchNextItem.getUserName().isEmpty());
        assertFalse("Watch next item has channel id", watchNextItem.getChannelId().isEmpty());
        assertFalse("Watch next item has view count", watchNextItem.getViewCount().isEmpty());
        assertFalse("Watch next item has length", watchNextItem.getLengthText().isEmpty());
        assertTrue("Watch next item has thumbnails", watchNextItem.getThumbnails().size() > 0);
    }
}