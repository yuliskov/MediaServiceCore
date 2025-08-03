package com.liskovsoft.youtubeapi.next.v1;

import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.googlecommon.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.next.v1.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore("Old api")
@RunWith(RobolectricTestRunner.class)
public class WatchNextApiUnsignedTest extends WatchNextManagerTestBase {
    private WatchNextApi mManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mManager = RetrofitHelper.create(WatchNextApi.class);
    }

    @Test
    public void testThatWatchNextContainsAllRequiredFields() {
        checkWatchNextResultFields(getWatchNextResult());
    }
    
    @Test
    public void testThatResultProperlyLocalized() {
        LocaleManager.instance().setLanguage("en");

        WatchNextResult watchNextResult = getWatchNextResult();

        SuggestedSection firstSuggesting = watchNextResult.getSuggestedSections().get(0);

        assertEquals("Suggestion title is localized to english", "Suggestions", firstSuggesting.getTitle());

        LocaleManager.instance().setLanguage("ru");

        watchNextResult = getWatchNextResult();

        firstSuggesting = watchNextResult.getSuggestedSections().get(0);

        assertEquals("Suggestion title is localized to russian", "Рекомендации", firstSuggesting.getTitle());
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
        Call<WatchNextResultContinuation> wrapper = mManager.continueWatchNextResult(BrowseApiHelper.getContinuationQuery(nextPageKey));

        return RetrofitHelper.get(wrapper);
    }

    private WatchNextResult getWatchNextResult() {
        Call<WatchNextResult> wrapper = mManager.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(TestHelpers.VIDEO_ID_CAPTIONS));
        return RetrofitHelper.get(wrapper);
    }
}