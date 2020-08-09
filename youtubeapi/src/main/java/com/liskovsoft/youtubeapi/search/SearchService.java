package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import retrofit2.Call;

/**
 * Wraps result from the {@link AuthManager} and {@link SearchManager}
 */
public class SearchService {
    private static SearchService sInstance;
    private final SearchManager mSearchManager;

    private SearchService() {
        mSearchManager = RetrofitHelper.withJsonPath(SearchManager.class);
    }

    public static SearchService instance() {
        if (sInstance == null) {
            sInstance = new SearchService();
        }

        return sInstance;
    }

    public SearchResult getSearch(String searchText) {
        Call<SearchResult> wrapper = mSearchManager.getSearchResult(SearchParams.getSearchQuery(searchText), SearchParams.getSearchKey());
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
    public NextSearchResult continueSearch(String nextSearchPageKey) {
        if (nextSearchPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }
        
        Call<NextSearchResult> wrapper = mSearchManager.getNextSearchResult(SearchParams.getNextSearchQuery(nextSearchPageKey), SearchParams.getSearchKey());
        NextSearchResult searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + nextSearchPageKey);
        }

        return searchResult;
    }
}
