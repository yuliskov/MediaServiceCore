package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.converter.JsonPathConverterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SearchManagerTest {
    private static final String SEARCH_KEY = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8";
    private static final String SEARCH_QUERY = "thrones season 8 trailer";
    private static final String JSON_DATA_TEMPLATE = "{'context':{'client':{'clientName':'TVHTML5','clientVersion':'6.20180807','screenWidthPoints':1280," +
            "'screenHeightPoints':720,'screenPixelDensity':1,'theme':'CLASSIC','utcOffsetMinutes':120,'webpSupport':false," +
            "'animatedWebpSupport':false,'tvAppInfo':{'livingRoomAppMode':'LIVING_ROOM_APP_MODE_MAIN'," +
            "'appQuality':'TV_APP_QUALITY_LIMITED_ANIMATION','isFirstLaunch':true},'acceptRegion':'UA','deviceMake':'LG'," +
            "'deviceModel':'42LA660S-ZA','platform':'TV'},'request':{},'user':{'enableSafetyMode':false}},'query':'%s'," +
            "'supportsVoiceSearch':false}";
    private static final String JSON_CONTINUATION_DATA_TEMPLATE = "{'context':{'client':{'clientName':'TVHTML5','clientVersion':'6.20180807'," +
            "'screenWidthPoints':1280,'screenHeightPoints':720,'screenPixelDensity':1,'theme':'CLASSIC','utcOffsetMinutes':120,'webpSupport':false," +
            "'animatedWebpSupport':false,'tvAppInfo':{'livingRoomAppMode':'LIVING_ROOM_APP_MODE_MAIN'," +
            "'appQuality':'TV_APP_QUALITY_LIMITED_ANIMATION'},'acceptRegion':'UA','deviceMake':'LG','deviceModel':'42LA660S-ZA','platform':'TV'}," +
            "'request':{},'user':{'enableSafetyMode':false},'clickTracking':{'clickTrackingParams':'CAQQybcCIhMIyI2g4f3t4AIV13SyCh1LPghk'}}," +
            "'continuation':'%s'}";

    private SearchManager mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.youtube.com") // ignored in case of full url
                .addConverterFactory(JsonPathConverterFactory.create())
                .build();

        mService = retrofit.create(SearchManager.class);
    }

    @Test
    public void testThatSearchResultNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SEARCH_KEY, String.format(JSON_DATA_TEMPLATE, SEARCH_QUERY));

        assertTrue("List > 2", wrapper.execute().body().getVideoItems().size() > 2);
    }

    @Test
    public void testThatSearchResultFieldsNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SEARCH_KEY, String.format(JSON_DATA_TEMPLATE, SEARCH_QUERY));
        SearchResult searchResult = wrapper.execute().body();
        VideoItem videoItem = searchResult.getVideoItems().get(0);

        assertNotNull(searchResult.getNextPageKey());
        assertNotNull(searchResult.getReloadPageKey());
        assertNotNull(videoItem.getVideoId());
        assertNotNull(videoItem.getTitle());
    }

    @Test
    public void testThatContinuationResultNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SEARCH_KEY, String.format(JSON_DATA_TEMPLATE, SEARCH_QUERY));
        SearchResult result = wrapper.execute().body();
        String nextPageKey = result.getNextPageKey();

        Call<NextSearchResult> wrapper2 = mService.getNextSearchResult(SEARCH_KEY, String.format(JSON_CONTINUATION_DATA_TEMPLATE, nextPageKey));
        NextSearchResult result2 = wrapper2.execute().body();

        assertTrue("List > 3", result2.getVideoItems().size() > 3);
    }
}