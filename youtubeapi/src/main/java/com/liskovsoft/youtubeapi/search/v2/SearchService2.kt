package com.liskovsoft.youtubeapi.search.v2

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.locale.LocaleManager
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SearchContinuationMediaGroup
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SearchSectionMediaGroup
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.search.SearchApiHelper
import com.liskovsoft.youtubeapi.search.v2.gen.SearchResult
import com.liskovsoft.youtubeapi.search.v2.gen.SearchTags
import com.liskovsoft.youtubeapi.search.v2.gen.getSections
import retrofit2.Call

private val TAG = SearchService2::class.simpleName

internal open class SearchService2 {
    private val mSearchApi: SearchApi = RetrofitHelper.create(SearchApi::class.java)
    private val mSearchApi2: SearchApi2 = RetrofitHelper.create(SearchApi2::class.java)
    private val mAppService = AppService.instance()

    open fun getSearch(searchText: String?): List<MediaGroup>? {
        return getSearch(searchText, -1)
    }

    open fun getSearch(searchText: String?, options: Int): List<MediaGroup>? {
        val wrapper: Call<SearchResult?> =
            mSearchApi.getSearchResult(SearchApiHelper.getSearchQuery(searchText, options), mAppService.visitorData)
        val searchResult = RetrofitHelper.get(wrapper)


        if (searchResult == null) {
            Log.e(TAG, "Empty search result for text %s", searchText)
        }

        return searchResult?.getSections()?.mapNotNull { it?.let { SearchSectionMediaGroup(it) } }
    }

    /**
     * Method uses results from the [.getSearch] call
     * @return video items
     */
    fun continueSearch(nextSearchPageKey: String?): MediaGroup? {
        if (nextSearchPageKey == null) {
            Log.e(TAG, "Can't get next search page. Next search key is empty.")
            return null
        }

        val wrapper: Call<WatchNextResultContinuation?>? =
            mSearchApi.continueSearchResult(SearchApiHelper.getContinuationQuery(nextSearchPageKey))
        val searchResult = RetrofitHelper.get(wrapper)

        if (searchResult == null) {
            Log.e(TAG, "Empty next search page result for key %s", nextSearchPageKey)
        }

        return searchResult?.let { SearchContinuationMediaGroup(it) }
    }

    open fun getSearchTags(searchText: String?): List<String>? {
        var country: String? = null
        val language: String? = null

        val localeManager = LocaleManager.instance()
        country = localeManager.country

        // fix empty popular searches (country and language should match or use only country)
        //language = localeManager.getLanguage();
        return getSearchTags(searchText, null, country, language)
    }

    private fun getSearchTags(searchText: String?, suggestToken: String?, country: String?, language: String?): List<String>? {
        var searchText = searchText
        if (searchText == null) {
            searchText = ""
        }

        val wrapper: Call<SearchTags?>? =
            mSearchApi2.getSearchTags(
                searchText,
                suggestToken,
                country,
                language
            )
        val searchTags = RetrofitHelper.get<SearchTags?>(wrapper)

        if (searchTags != null && searchTags.searchTags != null) {
            return searchTags.searchTags
        }

        return null
    }

    open fun clearSearchHistory() {
        // NOP
    }

    open fun removeTag(tag: String?) {
        // NOP
    }
}