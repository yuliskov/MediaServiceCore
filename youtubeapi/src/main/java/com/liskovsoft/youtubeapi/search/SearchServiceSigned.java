package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchTags;
import retrofit2.Call;

import java.util.List;

/**
 * Wraps result from the {@link SearchManagerSigned}
 */
public class SearchServiceSigned {
    private static SearchServiceSigned sInstance;
    private final SearchManagerSigned mSearchManagerSigned;

    private SearchServiceSigned() {
        mSearchManagerSigned = RetrofitHelper.withJsonPath(SearchManagerSigned.class);
    }

    public static SearchServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new SearchServiceSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public SearchResult getSearch(String searchText, String authorization) {
        Call<SearchResult> wrapper = mSearchManagerSigned.getSearchResult(SearchManagerParams.getSearchQuery(searchText), authorization);
        SearchResult searchResult = RetrofitHelper.get(wrapper);


        if (searchResult == null) {
            throw new IllegalStateException("Invalid search result for text " + searchText);
        }

        return searchResult;
    }

    /**
     * Method uses results from the {@link #getSearch(String, String)} call
     * @return video items
     */
    public SearchResultContinuation continueSearch(String nextSearchPageKey, String authorization) {
        if (nextSearchPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }
        
        Call<SearchResultContinuation> wrapper = mSearchManagerSigned.continueSearchResult(SearchManagerParams.getContinuationQuery(nextSearchPageKey), authorization);
        SearchResultContinuation searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + nextSearchPageKey);
        }

        return searchResult;
    }

    public List<String> getSearchTags(String searchText, String authorization) {
        Call<SearchTags> wrapper = mSearchManagerSigned.getSearchTags(searchText, authorization);
        SearchTags searchTags = RetrofitHelper.get(wrapper);

        if (searchTags != null && searchTags.getSearchTags() != null) {
            return searchTags.getSearchTags();
        }

        return null;
    }
}
