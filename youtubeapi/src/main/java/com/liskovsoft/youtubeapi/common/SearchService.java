package com.liskovsoft.youtubeapi.common;

import com.liskovsoft.youtubeapi.auth.BrowserAuth;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.content_old.ContentManager;
import com.liskovsoft.youtubeapi.search.SearchManager;
import com.liskovsoft.youtubeapi.search.SearchParams;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

import java.util.List;

/**
 * Wraps result from the {@link BrowserAuth}, {@link ContentManager} and {@link SearchManager}
 */
public class SearchService {
    private SearchManager mSearchManager;
    private String mNextSearchPageKey;

    public List<VideoItem> getSearch(String searchText) {
        SearchManager manager = getSearchManager();

        Call<SearchResult> wrapper = manager.getSearchResult(SearchParams.getSearchQuery(searchText), SearchParams.getSearchKey());
        SearchResult searchResult = RetrofitHelper.get(wrapper);


        if (searchResult == null) {
            throw new IllegalStateException("Invalid search result for text " + searchText);
        }

        mNextSearchPageKey = searchResult.getNextPageKey();

        return searchResult.getVideoItems();
    }

    /**
     * Method uses results from the {@link #getSearch(String)} call
     * @return video items
     */
    public List<VideoItem> getNextSearch() {
        if (mNextSearchPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }

        SearchManager manager = getSearchManager();
        Call<NextSearchResult> wrapper = manager.getNextSearchResult(SearchParams.getNextSearchQuery(mNextSearchPageKey), SearchParams.getSearchKey());
        NextSearchResult searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + mNextSearchPageKey);
        }

        mNextSearchPageKey = searchResult.getNextPageKey();

        return searchResult.getVideoItems();
    }

    private SearchManager getSearchManager() {
        if (mSearchManager == null) {
            mSearchManager = RetrofitHelper.withJsonPath(SearchManager.class);
        }

        return mSearchManager;
    }
}
