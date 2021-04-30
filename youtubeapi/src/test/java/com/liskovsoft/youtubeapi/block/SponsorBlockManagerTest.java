package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.youtubeapi.block.data.Segment;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SponsorBlockManagerTest {
    private static final String VIDEO_ID = "eVwFiINVZU8";
    private SponsorBlockManager mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(SponsorBlockManager.class);
    }

    @Test
    public void testThatSegmentResultNotEmpty() {
        Call<SegmentList> wrapper = mService.getSegments(VIDEO_ID);

        SegmentList segmentList = RetrofitHelper.get(wrapper);

        List<Segment> segments = segmentList.getSegments();

        assertNotNull("Segment list not empty", segments);

        Segment firstSegment = segments.get(0);

        assertNotNull("Category not empty", firstSegment.getCategory());
        assertTrue("Start not empty", firstSegment.getStart() > 0);
        assertTrue("End not empty", firstSegment.getEnd() > 0);
        assertNotNull("UUID not empty", firstSegment.getUuid());
    }

    @Test
    public void testThatSegmentByCategoryResultNotEmpty() {
        Call<SegmentList> wrapper = mService.getSegments(
                VIDEO_ID, ServiceHelper.toJsonArrayString(
                        Segment.CATEGORY_SPONSOR, Segment.CATEGORY_INTRO, Segment.CATEGORY_OUTRO,
                        Segment.CATEGORY_INTERACTION, Segment.CATEGORY_SELF_PROMO, Segment.CATEGORY_MUSIC_OFF_TOPIC
                )
        );

        SegmentList segmentList = RetrofitHelper.get(wrapper);

        List<Segment> segments = segmentList.getSegments();

        assertNotNull("Segment list not empty", segments);

        Segment firstSegment = segments.get(0);

        assertNotNull("Category not empty", firstSegment.getCategory());
        assertTrue("Start not empty", firstSegment.getStart() > 0);
        assertTrue("End not empty", firstSegment.getEnd() > 0);
        assertNotNull("UUID not empty", firstSegment.getUuid());
    }
}