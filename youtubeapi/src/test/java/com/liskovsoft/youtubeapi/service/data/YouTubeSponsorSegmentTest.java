package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.youtubeapi.block.data.SegmentList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class YouTubeSponsorSegmentTest {
    @Test
    public void missingAndNullBoundariesAreNotTreatedAsZero() throws IOException {
        assertTrue(parse("[{\"category\":\"sponsor\"},"
                + "{\"category\":\"sponsor\",\"segment\":[1]},"
                + "{\"category\":\"sponsor\",\"segment\":[null,10]},"
                + "{\"category\":\"sponsor\",\"segment\":[0,null]}]").isEmpty());
    }

    @Test
    public void negativeAndReversedBoundariesAreRejected() throws IOException {
        assertTrue(parse("[{\"category\":\"sponsor\",\"segment\":[-1,10]},"
                + "{\"category\":\"sponsor\",\"segment\":[0,-1]},"
                + "{\"category\":\"sponsor\",\"segment\":[10,1]}]").isEmpty());
    }

    @Test
    public void infiniteTimesAndMillisecondOverflowAreRejected() throws IOException {
        assertTrue(parse("[{\"category\":\"sponsor\",\"segment\":[0,1.0e40]},"
                + "{\"category\":\"sponsor\",\"segment\":[0,1.0e16]}]").isEmpty());
    }

    @Test
    public void validSegmentsSurviveInvalidNeighbours() throws IOException {
        List<SponsorSegment> result = parse("[{\"category\":\"sponsor\",\"segment\":[null,10]},"
                + "{\"category\":\"hook\",\"actionType\":\"skip\",\"segment\":[1.25,2.5]}]");

        assertEquals(1, result.size());
        assertEquals(SponsorSegment.CATEGORY_HOOK, result.get(0).getCategory());
        assertEquals(1250, result.get(0).getStartMs());
        assertEquals(2500, result.get(0).getEndMs());
    }

    @Test
    public void zeroStartAndLegacyMissingActionAreSupported() throws IOException {
        List<SponsorSegment> result = parse("[{\"category\":\"sponsor\",\"segment\":[0,10]}]");

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getStartMs());
        assertEquals(10000, result.get(0).getEndMs());
        assertNull(result.get(0).getAction());
    }

    @Test
    public void fractionalMillisecondConversionIsUnchanged() throws IOException {
        List<SponsorSegment> result = parse("[{\"category\":\"sponsor\",\"segment\":[10.123,20.123]}]");

        assertEquals(10123, result.get(0).getStartMs());
        assertEquals(20123, result.get(0).getEndMs());
    }

    @Test
    public void legacyHighlightPointIsPreserved() throws IOException {
        List<SponsorSegment> result = parse(
                "[{\"category\":\"poi_highlight\",\"actionType\":\"skip\",\"segment\":[12.5,12.5]}]");

        assertEquals(1, result.size());
        assertEquals(12500, result.get(0).getStartMs());
        assertEquals(12500, result.get(0).getEndMs());
    }

    @Test
    public void unsupportedExplicitActionsAreNotConvertedToSkips() throws IOException {
        for (String action : new String[] {"mute", "full", "chapter", "poi", "unknown", ""}) {
            assertTrue(parse("[{\"category\":\"sponsor\",\"actionType\":\"" + action
                    + "\",\"segment\":[1,10]}]").isEmpty());
        }
    }

    private List<SponsorSegment> parse(String json) throws IOException {
        SegmentList segments = (SegmentList) JsonPathConverterFactory.create()
                .responseBodyConverter(SegmentList.class, null, null)
                .convert(ResponseBody.create(MediaType.parse("application/json"), json));
        return YouTubeSponsorSegment.from(segments);
    }
}
