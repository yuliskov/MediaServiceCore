package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

public class SearchManagerParams {
    private static final String FIRST_SEARCH = "\"query\":\"%s\"";
    private static final String CONTINUATION_SEARCH = "\"continuation\":\"%s\"";

    public static String getSearchQuery(String searchText) {
        String search = String.format(FIRST_SEARCH, escape(searchText));
        return ServiceHelper.createQuery(search);
    }

    /**
     * Get data param for the next search
     * @param nextPageKey {@link SearchResult#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION_SEARCH, nextPageKey);
        return ServiceHelper.createQuery(continuation);
    }

    private static String escape(String text) {
        return text
                .replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\\"");
    }
}
