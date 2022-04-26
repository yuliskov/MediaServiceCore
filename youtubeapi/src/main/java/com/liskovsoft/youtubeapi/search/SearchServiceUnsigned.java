package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchTags;
import retrofit2.Call;

import java.util.List;

/**
 * Wraps result from the {@link SearchManagerUnsigned}
 */
public class SearchServiceUnsigned {
    private static final String TAG = SearchServiceUnsigned.class.getSimpleName();
    private static SearchServiceUnsigned sInstance;
    private final SearchManagerUnsigned mSearchManagerUnsigned;
    private final AppService mAppService;

    private SearchServiceUnsigned() {
        mSearchManagerUnsigned = RetrofitHelper.withJsonPath(SearchManagerUnsigned.class);
        mAppService = AppService.instance();
    }

    public static SearchServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new SearchServiceUnsigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public SearchResult getSearch(String searchText) {
        return getSearch(searchText, -1);
    }

    public SearchResult getSearch(String searchText, int options) {
        Call<SearchResult> wrapper =
                mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(searchText, options), mAppService.getVisitorId());
        SearchResult searchResult = RetrofitHelper.get(wrapper);


        if (searchResult == null) {
            Log.e(TAG, "Empty search result for text %s", searchText);
        }

        return searchResult;
    }

    /**
     * Method uses results from the {@link #getSearch(String)} call
     * @return video items
     */
    public SearchResultContinuation continueSearch(String nextSearchPageKey) {
        if (nextSearchPageKey == null) {
            Log.e(TAG, "Can't get next search page. Next search key is empty.");
        }
        
        Call<SearchResultContinuation> wrapper = mSearchManagerUnsigned.continueSearchResult(SearchManagerParams.getContinuationQuery(nextSearchPageKey));
        SearchResultContinuation searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            Log.e(TAG, "Empty next search page result for key %s", nextSearchPageKey);
        }

        return searchResult;
    }

    public List<String> getSearchTags(String searchText) {
        if (searchText == null) {
            searchText = "";
        }

        LocaleManager localeManager = LocaleManager.instance();

        Call<SearchTags> wrapper = mSearchManagerUnsigned.getSearchTags(
                searchText,
                localeManager.getCountry(),
                localeManager.getLanguage(),
                mAppService.getVisitorId()
        );
        SearchTags searchTags = RetrofitHelper.get(wrapper);

        if (searchTags != null && searchTags.getSearchTags() != null) {
            return searchTags.getSearchTags();
        }

        return null;
    }
}
