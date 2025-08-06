package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.browse.v2.mock.BrowseApiMock
import com.liskovsoft.youtubeapi.browse.v2.mock.BrowseApiMock2
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken2
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.KidsSectionMediaGroup
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.MediaGroupOptions
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.ShelfSectionMediaGroup
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getItems
import com.liskovsoft.youtubeapi.next.v2.gen.getNextPageKey
import com.liskovsoft.youtubeapi.next.v2.gen.getShelves
import com.liskovsoft.youtubeapi.next.v2.mock.MockUtils
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseApiSignedTest {
    /**
     * Authorization should be updated each hour
     */
    private lateinit var mService: BrowseApi
    private lateinit var mMockService: BrowseApi
    private lateinit var mMockService2: BrowseApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.create(BrowseApi::class.java)
        mMockService = MockUtils.mockWithGson(BrowseApiMock::class.java)
        mMockService2 = MockUtils.mockWithGson(BrowseApiMock2::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpers.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testThatSubsNotEmpty() {
        val subs = getSubscriptions()

        assertNotNull("Contains videos", subs?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatSubsContainShorts() {
        val subs = getSubscriptions()

        assertNotNull("Contains videos", subs?.getShortItems()?.getOrNull(0))
    }

    @Test
    fun testThatSubContainsFeedbackToken() {
        val subs = getSubscriptions()

        assertNotNull("Contains feedback token", subs?.getItems()?.getOrNull(0)?.getFeedbackToken())
    }

    @Test
    fun testThatSubsCanBeContinued() {
        val subs = getSubscriptions()

        assertNotNull("Contains continuation token", subs?.getContinuationToken())

        checkContinuationTV(subs?.getContinuationToken())
    }

    @Test
    fun testThatTVHomeMockNotEmpty() {
        val home = getHomeMock()

        assertNotNull("Home not empty", home?.getShelves()?.get(3)?.getItems()?.get(2))
    }

    @Test
    fun testThatTVHome2MockNotEmpty() {
        val home = getHome2Mock()

        assertNotNull("Home not empty", home?.getShelves()?.get(2)?.getItems()?.get(1))
    }

    @Test
    fun testThatTVHome2MockHasAllContent() {
        val home = getHome2Mock()

        val rowsOptions = createOptions(MediaGroup.TYPE_HOME)

        home?.getShelves()?.forEachIndexed { index, element ->
            val group = ShelfSectionMediaGroup(element!!, rowsOptions)
            assertTrue("Group $index not empty", !YouTubeHelper.isEmpty(group.mediaItems?.last()))
            if (index == 0) {
                assertTrue("Recommended contains more than two items", (group.mediaItems?.size ?: 0) > 5)
            }
        }
    }

    @Test
    fun testThatTVHomeContinuationMockNotEmpty() {
        val home = getHomeContinuationMock()

        assertNotNull("Home not empty", home?.getShelves()?.get(3)?.getItems()?.get(2))
    }

    @Test
    fun testThatTVHome2ContinuationMockNotEmpty() {
        val home = getHome2ContinuationMock()

        val rowsOptions = createOptions(MediaGroup.TYPE_HOME)

        home?.getShelves()?.forEachIndexed { index, element ->
            val group = ShelfSectionMediaGroup(element!!, rowsOptions)
            assertTrue("Group $index not empty", !YouTubeHelper.isEmpty(group.mediaItems?.last()))
            if (index == 2) {
                assertTrue("Top channels you watch contains more than two items", (group.mediaItems?.size ?: 0) > 5)
            }
        }
    }

    @Test
    fun testThatTVHome2ContinuationHasAllContent() {
        val home = getHome2ContinuationMock()

        assertNotNull("Home not empty", home?.getShelves()?.get(3)?.getItems()?.get(1))
    }

    @Test
    fun testThatHomeNotEmpty() {
        val home = getHome()

        assertNotNull("Contains videos", home?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatHomeCanBeContinued() {
        val home = getHome()

        assertNotNull("Contains continuation token", home?.getContinuationToken())

        checkContinuationTV(home?.getContinuationToken())
    }

    @Test
    fun testThatHomeContainsAllTokens() {
        val home = getHome()

        val item = home?.getItems()?.firstOrNull { it?.getFeedbackToken() != null }

        assertNotNull("Home contains feedback token 1", item?.getFeedbackToken())
        assertNotNull("Home contains feedback token 2", item?.getFeedbackToken2())
    }

    @Test
    fun testThatHomeContainsSections() {
        val home = getHome()

        val sections = home?.getShelves()

        val section = sections?.firstOrNull()

        assertNotNull("Section contains title", section?.getTitle())
        assertNotNull("Section contains items", section?.getItems()?.firstOrNull())
    }

    @Test
    fun testThatKidsHomeNotEmpty() {
        val kidsHome = getKidsHome()

        assertNotNull("Contains sections", kidsHome?.getSections())
    }

    @Test
    fun testThatKidsHomeCanBeContinued() {
        val kidsHome = getKidsHome()

        kidsHome?.getSections()?.forEach {
            if (it?.getItems() == null) {
                val home = getKidsHome(it?.getParams())
                assertNotNull("Section not empty", home?.getRootSection()?.getItems())
            }
        }
    }

    @Test
    fun testKidsMediaItemConversion() {
        val kidsHome = getKidsHome()

        val mediaGroup = KidsSectionMediaGroup(kidsHome?.getRootSection()!!, createOptions(MediaGroup.TYPE_KIDS_HOME))

        BrowseTestHelper.checkMediaItem(mediaGroup.mediaItems?.getOrNull(0)!!)
    }

    @Test
    fun testLikedMusicNotEmpty() {
        val likedMusic = getLikedMusic()

        assertNotNull("List not empty", likedMusic?.getItems())
    }

    @Test
    fun testMyPlaylists() {
        val client = AppClient.TV
        val browse = mService.getBrowseResultTV(browseQuery = BrowseApiHelper.getMyPlaylistQuery(client))

        val result = RetrofitHelper.get(browse)

        assertNotNull("Has playlist", result?.getItems())
    }

    @Ignore("Doesn't work with tv")
    @Test
    fun testThatGuideNotEmpty() {
        val guide = getGuide()

        assertNotNull("Guide contains suggest token", guide?.getSuggestToken())
        assertTrue("Guide contains channels", guide?.getFirstSubs()?.isNotEmpty() == true)
        assertTrue("Guide collapse contains channels", guide?.getCollapsibleSubs()?.size ?: 0 > 20)
    }

    private fun getGuide(): GuideResult? {
        val guideResult = mService.getGuideResult(PostDataHelper.createQueryTV(""))

        return RetrofitHelper.get(guideResult)
    }

    private fun checkContinuationTV(token: String?, checkNextToken: Boolean = true) {
        val continuationResult = mService.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(AppClient.TV, token!!))

        val continuation = RetrofitHelper.get(continuationResult)

        assertNotNull("Contains items", (continuation?.getItems() ?: continuation?.getShelves()?.get(0)?.getItems())?.getOrNull(0))

        if (checkNextToken) {
            assertNotNull("Contains next token", continuation?.getNextPageKey())
        }
    }

    private fun getSubscriptions(): BrowseResultTV? {
        val subsResult = mService.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(AppClient.TV))

        return RetrofitHelper.get(subsResult)
    }

    private fun getHome(): BrowseResultTV? {
        val homeResult = mService.getBrowseResultTV(BrowseApiHelper.getHomeQuery(AppClient.TV))

        return RetrofitHelper.get(homeResult)
    }

    private fun getHomeMock(): BrowseResultTV? {
        val homeResult = mMockService.getBrowseResultTV(BrowseApiHelper.getHomeQuery(AppClient.TV))

        return RetrofitHelper.get(homeResult)
    }

    private fun getHome2Mock(): BrowseResultTV? {
        val homeResult = mMockService2.getBrowseResultTV(BrowseApiHelper.getHomeQuery(AppClient.TV))

        return RetrofitHelper.get(homeResult)
    }

    private fun getHomeContinuationMock(): WatchNextResultContinuation? {
        val homeResult = mMockService.getContinuationResultTV(BrowseApiHelper.getHomeQuery(AppClient.TV))

        return RetrofitHelper.get(homeResult)
    }

    private fun getHome2ContinuationMock(): WatchNextResultContinuation? {
        val homeResult = mMockService2.getContinuationResultTV(BrowseApiHelper.getHomeQuery(AppClient.TV))

        return RetrofitHelper.get(homeResult)
    }

    private fun getKidsHome(): BrowseResultKids? {
        val kidsResult = mService.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery())

        return RetrofitHelper.get(kidsResult)
    }

    private fun getKidsHome(params: String?): BrowseResultKids? {
        val kidsResult = mService.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery(params!!))

        return RetrofitHelper.get(kidsResult)
    }

    private fun getLikedMusic(): WatchNextResultContinuation? {
        val result = mService.getContinuationResultTV(BrowseApiHelper.getLikedMusicContinuation(AppClient.TV))

        return RetrofitHelper.get(result)
    }

    private fun createOptions(groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS): MediaGroupOptions {
        return MediaGroupOptions.create(
            groupType = groupType
        )
    }
}