package com.liskovsoft.youtubeapi.search.v2

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper.authHeaders
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.browse.v1.BrowseService
import com.liskovsoft.youtubeapi.search.SearchApiHelper
import com.liskovsoft.youtubeapi.search.v2.gen.getNextPageKey
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

private const val SEARCH_TEXT = "thrones season 8 trailer"
private const val SEARCH_TEXT_SPECIAL_CHAR = "What's Trending"

@RunWith(RobolectricTestRunner::class)
internal class SearchService2Test : SearchServiceTestBase() {
    private lateinit var mSearchApi: SearchApi
    private lateinit var mSearchApi2: SearchApi2
    private lateinit var mBrowseService: BrowseService

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")

        ShadowLog.stream = System.out // catch Log class output

        mSearchApi = RetrofitHelper.create(SearchApi::class.java)
        mSearchApi2 = RetrofitHelper.create(SearchApi2::class.java)
        mBrowseService = BrowseService.instance() // TODO: replace with v2 version somehow

        authHeaders.put("Authorization", TestHelpers.getAuthorization())
    }

    @Test
    fun testThatSearchResultIsValid() {
        var wrapper = mSearchApi.getSearchResult(
            SearchApiHelper.getSearchQuery(SEARCH_TEXT), TestHelpers.getAuthorization())
        var searchResult = RetrofitHelper.get(wrapper)

        checkSearchResult(searchResult)

        wrapper = mSearchApi.getSearchResult(
            SearchApiHelper.getSearchQuery(SEARCH_TEXT_SPECIAL_CHAR), TestHelpers.getAuthorization())
        searchResult = RetrofitHelper.get(wrapper)

        checkSearchResult(searchResult)
    }

    @Test
    fun testThatContinuationResultIsValid() {
        val wrapper = mSearchApi.getSearchResult(
            SearchApiHelper.getSearchQuery(SEARCH_TEXT), TestHelpers.getAuthorization())
        val result = RetrofitHelper.get(wrapper)
        checkSearchResult(result)

        val nextPageKey = result.getNextPageKey()
        val wrapper2 = mSearchApi.continueSearchResult(
            SearchApiHelper.getContinuationQuery(nextPageKey))
        val result2 = RetrofitHelper.get(wrapper2)
        checkSearchResultContinuation(result2)
    }

    @Test
    fun testThatSearchTagsNotEmpty() {
        val wrapper = mSearchApi2.getSearchTags(
            "bc",
            mBrowseService.getSuggestToken()
        )
        val searchTags = RetrofitHelper.get(wrapper)

        Assert.assertNotNull("Search tags not empty", searchTags)
        Assert.assertTrue("Contains multiple tags", searchTags!!.searchTags.size > 3)
    }

    @Test
    fun testThatSearchTagsHistoryNotEmpty() {
        val wrapper = mSearchApi2.getSearchTags(
            "",
            mBrowseService.getSuggestToken()
        )
        val searchTags = RetrofitHelper.get(wrapper)

        Assert.assertNotNull("Search tags not empty", searchTags)
        Assert.assertTrue("Contains multiple tags", searchTags!!.searchTags.size > 3)
    }

    @Test
    fun testThatServiceResultNotEmpty() {
        val result = SearchService2Wrapper.getSearch("tv rain")

        Assert.assertTrue("Contains multiple sections", (result?.size ?: 0) > 0)
        Assert.assertNotNull("Contains next page key", result?.firstOrNull()?.nextPageKey)

        val continuation = SearchService2Wrapper.continueSearch(result?.firstOrNull()?.nextPageKey)

        Assert.assertNotNull("Can be continued", (continuation?.mediaItems?.size ?: 0) > 3)
    }
}