package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.auth.BrowserAuth;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

/**
 * Wraps result from the {@link BrowserAuth} and {@link SearchManager}
 */
public class SearchService {
    private SearchManager mSearchManager;

    public SearchResult getSearch(String searchText) {
        SearchManager manager = getSearchManager();

        Call<SearchResult> wrapper = manager.getSearchResult(SearchParams.getSearchQuery(searchText), SearchParams.getSearchKey());
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

        SearchManager manager = getSearchManager();
        Call<NextSearchResult> wrapper = manager.getNextSearchResult(SearchParams.getNextSearchQuery(nextSearchPageKey), SearchParams.getSearchKey());
        NextSearchResult searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + nextSearchPageKey);
        }

        return searchResult;
    }

    private SearchManager getSearchManager() {
        if (mSearchManager == null) {
            mSearchManager = RetrofitHelper.withJsonPath(SearchManager.class);
        }

        return mSearchManager;
    }
}
