package com.liskovsoft.youtubeapi.search;

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
    private SearchManager mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.youtube.com") // ignored in case of full url
                .addConverterFactory(JsonPathConverterFactory.create(SearchResult.class))
                .build();

        mService = retrofit.create(SearchManager.class);
    }

    @Test
    public void testThatSearchResultNotEmpty() throws IOException {
        Call<SearchResult> tabs = mService.getSearchResults(SEARCH_KEY, String.format(JSON_DATA_TEMPLATE, SEARCH_QUERY));
        assertTrue(tabs.execute().body().getResultList().size() > 2);
    }
}