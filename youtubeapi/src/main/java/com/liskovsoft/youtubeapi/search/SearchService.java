package com.liskovsoft.youtubeapi.search;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.browse.v1.BrowseService;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchTags;
import retrofit2.Call;

import java.util.List;

/**
 * Wraps result from the {@link SearchApi}
 */
public class SearchService {
    private static final String TAG = SearchService.class.getSimpleName();
    private final SearchApi mSearchApi;

    public SearchService() {
        mSearchApi = RetrofitHelper.create(SearchApi.class);
    }

    public SearchResult getSearch(String searchText) {
        return getSearch(searchText, -1);
    }

    public SearchResult getSearch(String searchText, int options) {
        Call<SearchResult> wrapper = mSearchApi.getSearchResult(SearchApiHelper.getSearchQuery(searchText, options), getAppService().getVisitorData());
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
            return null;
        }

        Call<SearchResultContinuation> wrapper = mSearchApi.continueSearchResult(SearchApiHelper.getContinuationQuery(nextSearchPageKey));
        SearchResultContinuation searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            Log.e(TAG, "Empty next search page result for key %s", nextSearchPageKey);
        }

        return searchResult;
    }

    public List<String> getSearchTags(String searchText) {
        String country = null;
        String language = null;

        LocaleManager localeManager = LocaleManager.instance();
        country = localeManager.getCountry();
        // fix empty popular searches (country and language should match or use only country)
        //language = localeManager.getLanguage();

        return getSearchTags(searchText, null, country, language);
    }

    private List<String> getSearchTags(String searchText, String suggestToken, String country, String language) {
        if (searchText == null) {
            searchText = "";
        }

        Call<SearchTags> wrapper =
                mSearchApi.getSearchTags(
                        searchText,
                        suggestToken,
                        country,
                        language
                );
        SearchTags searchTags = RetrofitHelper.get(wrapper);

        if (searchTags != null && searchTags.getSearchTags() != null) {
            return searchTags.getSearchTags();
        }

        return null;
    }

    public void clearSearchHistory() {
        // NOP
    }

    @NonNull
    private static AppService getAppService() {
        return AppService.instance();
    }

    @NonNull
    private static BrowseService getBrowseService() {
        return BrowseService.instance();
    }
}
