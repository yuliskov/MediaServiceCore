package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

public class SearchManagerHelper {
    private SearchManager mSearchManager;

    public SearchResult startSearch(String searchText) {
        SearchManager manager = getSearchManager();

        Call<SearchResult> wrapper = manager.getSearchResult(SearchParams.getSearchKey(), SearchParams.getSearchQuery(searchText));
        SearchResult searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid search result for " + searchText);
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
