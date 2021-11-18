package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.data.SearchOptions;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

public class SearchManagerParams {
    private static final String FIRST_SEARCH = "\"query\":\"%s\"";
    private static final String FIRST_SEARCH_EXT = "\"query\":\"%s\",\"params\":\"%s\"";
    private static final String CONTINUATION_SEARCH = "\"continuation\":\"%s\"";

    public static String getSearchQuery(String searchText) {
        return getSearchQuery(searchText, -1);
    }

    public static String getSearchQuery(String searchText, int options) {
        String search = options > 0 ?
                String.format(FIRST_SEARCH_EXT, escape(searchText), toParams(options)) : String.format(FIRST_SEARCH, escape(searchText));
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

    private static String toParams(int options) {
        String result = null;

        if (Helpers.check(options, SearchOptions.UPLOAD_DATE_LAST_HOUR)) {
            result = "EgQIARAB";
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_TODAY)) {
            result = "EgQIAhAB";
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_WEEK)) {
            result = "EgQIAxAB";
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_MONTH)) {
            result = "EgQIBBAB";
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_YEAR)) {
            result = "EgQIBRAB";
        }

        return result;
    }
}
