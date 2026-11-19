package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.data.SearchOptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SearchFilterHelperTest {
    @Test
    public void noOptionsMeansNoParams() {
        assertNull(SearchFilterHelper.toParams(-1));
        assertNull(SearchFilterHelper.toParams(0));
        assertNull(SearchFilterHelper.toParams(SearchOptions.SORT_BY_RELEVANCE));
    }

    /** Values of the former lookup table: single criteria must stay byte-identical. */
    @Test
    public void singleCriteriaMatchTheFormerTable() {
        assertEquals("EgQIARAB", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_LAST_HOUR));
        assertEquals("EgQIAhAB", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_TODAY));
        assertEquals("EgQIAxAB", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_THIS_WEEK));
        assertEquals("EgQIBBAB", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_THIS_MONTH));
        assertEquals("EgQIBRAB", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_THIS_YEAR));

        assertEquals("EgQQARgB", SearchFilterHelper.toParams(SearchOptions.DURATION_UNDER_4));
        assertEquals("EgQQARgD", SearchFilterHelper.toParams(SearchOptions.DURATION_BETWEEN_4_20));
        assertEquals("EgQQARgC", SearchFilterHelper.toParams(SearchOptions.DURATION_OVER_20));

        assertEquals("EgIQAQ%3D%3D", SearchFilterHelper.toParams(SearchOptions.TYPE_VIDEO));
        assertEquals("EgIQAg%3D%3D", SearchFilterHelper.toParams(SearchOptions.TYPE_CHANNEL));
        assertEquals("EgIQAw%3D%3D", SearchFilterHelper.toParams(SearchOptions.TYPE_PLAYLIST));
        assertEquals("EgIQBA%3D%3D", SearchFilterHelper.toParams(SearchOptions.TYPE_MOVIE));

        assertEquals("EgJAAQ%3D%3D", SearchFilterHelper.toParams(SearchOptions.FEATURE_LIVE));
        assertEquals("EgJwAQ%3D%3D", SearchFilterHelper.toParams(SearchOptions.FEATURE_4K));
        assertEquals("EgPIAQE%3D", SearchFilterHelper.toParams(SearchOptions.FEATURE_HDR));
        assertEquals("EgRAAXAB", SearchFilterHelper.toParams(SearchOptions.FEATURE_LIVE | SearchOptions.FEATURE_4K));
        assertEquals("EgVwAcgBAQ%3D%3D", SearchFilterHelper.toParams(SearchOptions.FEATURE_4K | SearchOptions.FEATURE_HDR));
        assertEquals("EgdAAXAByAEB",
                SearchFilterHelper.toParams(SearchOptions.FEATURE_LIVE | SearchOptions.FEATURE_4K | SearchOptions.FEATURE_HDR));

        assertEquals("CAI%3D", SearchFilterHelper.toParams(SearchOptions.SORT_BY_UPLOAD_DATE));
        assertEquals("CAMSAhAB", SearchFilterHelper.toParams(SearchOptions.SORT_BY_VIEW_COUNT));
        assertEquals("CAESAhAB", SearchFilterHelper.toParams(SearchOptions.SORT_BY_RATING));
    }

    @Test
    public void dateAndDurationMatchTheFormerTable() {
        assertEquals("EgYIAhABGAE%3D", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_TODAY | SearchOptions.DURATION_UNDER_4));
        assertEquals("EgYIAxABGAM%3D", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_THIS_WEEK | SearchOptions.DURATION_BETWEEN_4_20));
        assertEquals("EgYIBBABGAI%3D", SearchFilterHelper.toParams(SearchOptions.UPLOAD_DATE_THIS_MONTH | SearchOptions.DURATION_OVER_20));
    }

    /** The combination that used to fall back to unfiltered results (Telegram report). */
    @Test
    public void monthVideoSortedByUploadDate() {
        // sort=2, filters{date=4, type=1}
        assertEquals("CAISBAgEEAE%3D", SearchFilterHelper.toParams(
                SearchOptions.UPLOAD_DATE_THIS_MONTH | SearchOptions.TYPE_VIDEO | SearchOptions.SORT_BY_UPLOAD_DATE));
        // type=video is implied by the date filter, so leaving Type on Any gives the same params
        assertEquals("CAISBAgEEAE%3D", SearchFilterHelper.toParams(
                SearchOptions.UPLOAD_DATE_THIS_MONTH | SearchOptions.SORT_BY_UPLOAD_DATE));
    }

    @Test
    public void everyCombinationProducesParams() {
        int[] dates = {0, SearchOptions.UPLOAD_DATE_LAST_HOUR, SearchOptions.UPLOAD_DATE_TODAY, SearchOptions.UPLOAD_DATE_THIS_WEEK,
                SearchOptions.UPLOAD_DATE_THIS_MONTH, SearchOptions.UPLOAD_DATE_THIS_YEAR};
        int[] durations = {0, SearchOptions.DURATION_UNDER_4, SearchOptions.DURATION_BETWEEN_4_20, SearchOptions.DURATION_OVER_20};
        int[] types = {0, SearchOptions.TYPE_VIDEO, SearchOptions.TYPE_CHANNEL, SearchOptions.TYPE_PLAYLIST, SearchOptions.TYPE_MOVIE};
        int[] features = {0, SearchOptions.FEATURE_LIVE, SearchOptions.FEATURE_4K, SearchOptions.FEATURE_HDR,
                SearchOptions.FEATURE_LIVE | SearchOptions.FEATURE_4K | SearchOptions.FEATURE_HDR};
        int[] sorts = {SearchOptions.SORT_BY_RELEVANCE, SearchOptions.SORT_BY_UPLOAD_DATE, SearchOptions.SORT_BY_VIEW_COUNT,
                SearchOptions.SORT_BY_RATING};

        for (int d : dates) for (int du : durations) for (int t : types) for (int f : features) for (int s : sorts) {
            String params = SearchFilterHelper.toParams(d | du | t | f | s);
            if ((d | du | t | f | s) == SearchOptions.SORT_BY_RELEVANCE) {
                assertNull(params);
            } else {
                assertNotNull(params);
            }
        }
    }
}
