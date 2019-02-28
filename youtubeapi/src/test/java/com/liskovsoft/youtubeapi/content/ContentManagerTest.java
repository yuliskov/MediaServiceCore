package com.liskovsoft.youtubeapi.content;

import com.liskovsoft.youtubeapi.content.models.ContentTab;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.youtube.com") // ignored in case of full url
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(ContentManager.class);
    }

    @Test
    public void testThatContentTabsNotEmpty() {
        List<ContentTab> tabs = mService.getContentTabs(BODY_JSON);
        assertTrue(tabs.size() > 2);
    }
}