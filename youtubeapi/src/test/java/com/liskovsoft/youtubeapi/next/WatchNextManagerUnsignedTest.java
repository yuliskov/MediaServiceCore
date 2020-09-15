package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.next.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WatchNextManagerUnsignedTest extends WatchNextManagerTestBase {
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

        assertEquals("Suggestion title is localized to russian", "Похожие видео", firstSuggesting.getTitle());
    }

    private WatchNextResult getWatchNextResult() {
        Call<WatchNextResult> wrapper = mManager.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(TestHelpers.VIDEO_ID_SIMPLE));
        return RetrofitHelper.get(wrapper);
    }
}