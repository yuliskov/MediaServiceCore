package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import retrofit2.Call;

/**
 * Wraps result from the {@link AuthManager} and {@link SearchManagerUnsigned}
 */
public class SearchServiceUnsigned {
    private static SearchServiceUnsigned sInstance;
    private final SearchManagerUnsigned mSearchManager;

    private SearchServiceUnsigned() {
        mSearchManager = RetrofitHelper.withJsonPath(SearchManagerUnsigned.class);
    }

    public static SearchServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new SearchServiceUnsigned();
        }

        return sInstance;
    }

    public SearchResult getSearch(String searchText) {
        Call<SearchResult> wrapper = mSearchManager.getSearchResult(SearchManagerParams.getSearchQuery(searchText));
        SearchResult searchResult = RetrofitHelper.get(wrapper);


        if (searchResult == null) {
            throw new IllegalStateException("Invalid search result for text " + searchText);
        }

        return searchResult;
    }

    /**
     * Method uses results from the {@link #getSearch(String)} call
     * @return video items
     */
    public SearchResultContinuation continueSearch(String nextSearchPageKey) {
        if (nextSearchPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }
        
        Call<SearchResultContinuation> wrapper = mSearchManager.continueSearchResult(SearchManagerParams.getNextSearchQuery(nextSearchPageKey));
        SearchResultContinuation searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + nextSearchPageKey);
        }

        return searchResult;
    }
}
