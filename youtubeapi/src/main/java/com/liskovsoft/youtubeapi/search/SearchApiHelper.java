package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

public class SearchApiHelper {
    private static final String FIRST_SEARCH = "\"query\":\"%s\"";
    private static final String FIRST_SEARCH_EXT = "\"query\":\"%s\",\"params\":\"%s\"";
    private static final String CONTINUATION_SEARCH = "\"continuation\":\"%s\"";

    public static String getSearchQuery(String searchText) {
        return getSearchQuery(searchText, -1);
    }

    public static String getSearchQuery(String searchText, int options) {
        String params = SearchFilterHelper.toParams(options);
        String search = params != null ?
                String.format(FIRST_SEARCH_EXT, escape(searchText), params) : String.format(FIRST_SEARCH, escape(searchText));
        return PostDataHelper.createQueryTV(search);
    }

    /**
     * Get data param for the next search
     * @param nextPageKey {@link SearchResult#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION_SEARCH, nextPageKey);
        return PostDataHelper.createQueryTV(continuation);
    }

    private static String escape(String text) {
        return text
                .replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\\"");
    }
}
