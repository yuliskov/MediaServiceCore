package com.liskovsoft.youtubeapi.content;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.content.models.ContentTab;
import com.liskovsoft.youtubeapi.content.models.ContentTabSection;
import com.liskovsoft.youtubeapi.content.models.RootContent;
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
                .addConverterFactory(JsonPathConverterFactory.create())
                .build();

        mService = retrofit.create(ContentManager.class);
    }

    @Test
    public void testThatContentTabsNotEmpty() throws IOException {
        Call<RootContent> tabs = mService.getRootContent(BODY_JSON);
        assertTrue(tabs.execute().body().getContentTabs().size() > 2);
    }

    @Test
    public void testThatContentHasNonNullProps() throws IOException {
        Call<RootContent> tabs = mService.getRootContent(BODY_JSON);
        RootContent tabCollection = tabs.execute().body();
        ContentTab contentTab = tabCollection.getContentTabs().get(0);
        ContentTabSection contentTabSection = contentTab.getSections().get(0);
        VideoItem videoItem = contentTabSection.getVideoItems().get(0);

        assertNotNull(contentTab.getTitle());
        assertNotNull(videoItem.getVideoId());
        assertNotNull(contentTabSection.getTitle());
    }
}