package com.liskovsoft.youtubeapi.content;

import com.liskovsoft.youtubeapi.content.models.ContentTabsContainer;
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
public class ContentManagerTest {
    private static final String BODY_JSON = "{'context':{'client':{'clientName':'TVHTML5','clientVersion':'6.20180807','screenWidthPoints':1280," +
            "'screenHeightPoints':720,'screenPixelDensity':1,'theme':'CLASSIC','utcOffsetMinutes':120,'webpSupport':false," +
            "'animatedWebpSupport':false,'tvAppInfo':{'livingRoomAppMode':'LIVING_ROOM_APP_MODE_MAIN'," +
            "'appQuality':'TV_APP_QUALITY_LIMITED_ANIMATION'},'acceptRegion':'UA','deviceMake':'LG','deviceModel':'43LK5760PTA','platform':'TV'}," +
            "'request':{},'user':{'enableSafetyMode':false}},'browseId':'FEtopics'}";
    private ContentManager mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.youtube.com") // ignored in case of full url
                .addConverterFactory(JsonPathConverterFactory.create(ContentTabsContainer.class))
                .build();

        mService = retrofit.create(ContentManager.class);
    }

    @Test
    public void testThatContentTabsNotEmpty() throws IOException {
        Call<ContentTabsContainer> tabs = mService.getContentTabs(BODY_JSON);
        assertTrue(tabs.execute().body().getContentTabs().size() > 2);
    }

    @Test
    public void testThatContentHasNonNullProps() throws IOException {
        Call<ContentTabsContainer> tabs = mService.getContentTabs(BODY_JSON);
        ContentTabsContainer tabCollection = tabs.execute().body();
        String sampleTitle = tabCollection.getContentTabs().get(0).getTitle();
        String anotherTitle = tabCollection.getContentTabs().get(0).getSections().get(0).getTitle();
        assertNotNull(sampleTitle);
        assertNotNull(anotherTitle);
    }
}