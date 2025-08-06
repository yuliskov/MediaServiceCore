package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper
import com.liskovsoft.youtubeapi.common.models.gen.getParams
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken2
import com.liskovsoft.youtubeapi.common.models.gen.getTitle
import com.liskovsoft.youtubeapi.common.models.gen.isLive
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.MediaGroupOptions
import com.liskovsoft.youtubeapi.next.v2.gen.getItems
import com.liskovsoft.youtubeapi.next.v2.gen.getNextPageKey
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseApiUnsignedTest {
    /**
     * Authorization should be updated each hour
     */
    private lateinit var mService: BrowseApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.create(BrowseApi::class.java)
        RetrofitOkHttpHelper.authHeaders.clear()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Ignore("Needs setup")
    @Test
    fun testThatHomeNotEmpty() {
        val home = getHome()

        assertNotNull("Contains videos", home?.getItems()?.getOrNull(0))
    }

    @Ignore("Needs setup")
    @Test
    fun testThatHomeCanBeContinued() {
        val home = getHome()

        assertNotNull("Contains continuation token", home?.getContinuationToken())

        checkContinuationWeb(home?.getContinuationToken())
    }

    @Ignore("Needs setup")
    @Test
    fun testThatHomeContainsAllTokens() {
        val home = getHome()

        val item = home?.getItems()?.firstOrNull { it?.getFeedbackToken() != null }

        assertNotNull("Home contains feedback token 1", item?.getFeedbackToken())
        assertNotNull("Home contains feedback token 2", item?.getFeedbackToken2())
    }

    @Ignore("Needs setup")
    @Test
    fun testThatHomeContainsSections() {
        val home = getHome()

        val sections = home?.getSections()
        val tabs = home?.getTabs()
        val chips = home?.getChips()

        val section = sections?.firstOrNull()
        val tab = tabs?.firstOrNull()
        val chip = chips?.firstOrNull()

        assertTrue("Result contains either sections, tabs or chips", section != null || tab != null || chip != null)

        if (section != null) {
            assertNotNull("Section contains title", section.getTitle())
            assertNotNull("Section contains items", section.getItems()?.firstOrNull())
        }

        if (tab != null) {
            //assertNotNull("Section contains title", tab.getTitle())
            assertNotNull("Section contains items", tab.getItems()?.firstOrNull())
        }

        if (chip != null) {
            assertNotNull("Section contains title", chip.getTitle())
            assertNotNull("Section contains items", chip.getContinuationToken() != null)
        }
    }

    @Ignore("Needs setup")
    @Test
    fun testThatChipsCanBeContinued() {
        val home = getHome()

        val chips = home?.getChips()

        assertNotNull("Contains chips", chips)

        val chip = chips?.getOrNull(2) // first chip is empty, second one is Posts

        assertNotNull("Chip has title", chip?.getTitle())

        checkContinuationWeb(chip?.getContinuationToken(), false) // Chips usually don't support multiple continuation
    }

    @Ignore("Doesn't contains chips")
    @Test
    fun testThatAltHomeNotEmpty() {
        val home = getAltHome()

        assertNotNull("Contains videos", home?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatSportsNotEmpty() {
        val sports = getSports()

        assertNotNull("Contains sections", sports?.getShelves())
        assertNotNull("Contains title", sports?.getShelves()?.firstOrNull()?.getTitle())
        assertNotNull("Contains content", sports?.getShelves()?.firstOrNull()?.getItems())
    }

    @Test
    fun testThatSportsCanBeContinued() {
        val sports = getSports()

        // recommended
        val nextPageKey = sports?.getShelves()?.getOrNull(1)?.getNextPageKey()

        checkContinuationTV(nextPageKey, true)
    }

    @Ignore("Don't work anymore")
    @Test
    fun testThatGuideNotEmpty() {
        val guide = getGuide()

        assertTrue("Guide contains channels", guide?.getFirstSubs()?.isNotEmpty() == true)
        assertTrue("Guide collapse contains channels", guide?.getCollapsibleSubs()?.size ?: 0 > 20)
    }

    @Test
    fun testThatReelsNotEmpty() {
        val reel = getReel()

        testFirstReelResult(reel)
    }

    @Test
    fun testThatReelDetailsNotEmpty() {
        val reels = getReel()

        val continuation = getReelContinuation(reels?.getContinuationKey())

        testReelContinuation(continuation)

        val next = getReelContinuation(continuation?.getContinuationKey())

        testReelContinuation(next)
    }

    @Ignore("Don't work anymore")
    @Test
    fun testLikedMusicNotEmpty() {
        val likedMusic = getLikedMusic()

        assertNotNull("List not empty", likedMusic?.getItems())
        assertNotNull("List has title", likedMusic?.getTitle())
    }

    @Test
    fun testNewMusicNotEmpty() {
        val newMusic = getNewMusicAlbums()

        assertNotNull("List not empty", newMusic?.getItems())
        assertNotNull("List has title", newMusic?.getTitle())
    }

    @Test
    fun testThatChannelTabsNotEmpty() {
        val home = getChannelHome(TestHelpers.CHANNEL_ID_3)

        val firstTab = home?.getTabs()?.get(0)

        assertNotNull("Tab has param", firstTab?.getParams())
    }

    @Test
    fun testThatChannelVideosTabNotEmpty() {
        val videos = getChannelVideos("UC1vCu8GeDC7_UfY7PKqsAzg")

        assertTrue("Contains videos", videos?.getItems()?.size ?: 0 > 10)
        assertNotNull("Has continuation", videos?.getContinuationToken())
    }

    @Test
    fun testThatChannelLiveTabNotEmpty() {
        val videos = getChannelLive("UCjLdcql-zKjeviGljM1RMHA")

        assertTrue("Contains videos", videos?.getItems()?.filter { it?.isLive() == true }?.size ?: 0 > 1)
        assertNotNull("Has continuation", videos?.getContinuationToken())
    }

    @Test
    fun testThatChannelLiveTabEmpty() {
        val videos = getChannelLive(TestHelpers.CHANNEL_ID_3)

        assertTrue("Contains videos", videos?.getItems()?.filter { it?.isLive() == true }?.size ?: 0 == 0)
    }

    @Test
    fun testThatChannelHomeTabNotEmpty() {
        val result = getChannelHome(TestHelpers.CHANNEL_ID_2)

        val firstShelve = result?.getShelves()?.get(0)
        assertNotNull("Contains title", firstShelve?.getTitle())
        assertNotNull("Contains nested items", firstShelve?.getItems())
    }

    @Test
    fun testThatChannelReleasesTabNotEmpty() {
        val result = getChannelReleases(TestHelpers.CHANNEL_ID_3)

        val first = result?.getItems()?.getOrNull(0)

        assertNotNull("Contains title", result?.getTitle())
        assertNotNull("Contains items", first?.getTitle())
    }

    @Test
    fun testThatChannelVideosNotEmpty() {
        val channelId = "UCkjot4p29KLU0pwc0srHeGg" // Till Lindemann all videos

        val videos = getChannelVideos(channelId)

        assertTrue("Playlist not empty", (videos?.getItems()?.size ?: 0) > 0)
    }

    @Test
    fun testThatChannelVideosHasContinuation() {
        val channelId = "UCkjot4p29KLU0pwc0srHeGg" // Till Lindemann all videos

        val videos = getChannelVideos(channelId)

        assertNotNull("Playlist has continuation", videos?.getContinuationToken())

        checkContinuationWeb(videos?.getContinuationToken())
    }

    @Test
    fun testThatChannelSearchNotEmpty() {
        val channelId = TestHelpers.CHANNEL_ID_3

        val videos = getChannelSearch(channelId, "in the army now")

        assertTrue("Has content", (videos?.getItems()?.size ?: 0) > 5)
    }

    @Test
    fun testThatPlaylistNotEmpty() {
        // Starfield songs
        val channelId = "VLPL3irMzbdU-v1liRStfWBD9i9i3AvmLpY5"

        val videos = getChannelPlaylist(channelId)

        assertNotNull("Not empty", videos?.getItems())
        //assertNotNull("Has title", videos?.getTitle())
    }

    @Test
    fun testThatTrendingNotEmpty() {
        val trending = getTrending()

        assertNotNull("Contains videos", trending?.getItems()?.getOrNull(0))

        for (tab in trending!!.getTabs()!!) {
            if (tab!!.content != null) {
                assertTrue("Root tab contains videos", (tab.getItems()?.size ?: 0) > 10)
            } else {
                val tabContent = getTrendingTab(tab.endpoint?.getParams())

                assertTrue("Next tab contains videos", (tabContent?.getItems()?.size ?: 0) > 10)
            }
        }
    }

    @Test
    fun testChannelTopicContinuation() {
        // World of tanks recently uploaded
        val browse = mService.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, "UC1dGjtlDiiDM0gc_ghB1nTQ", "EgZyZWNlbnQ%3D"))

        val result = RetrofitHelper.get(browse)

        assertTrue("Topic has continuation token", result?.getContinuationToken() != null)

        val continuation =
            mService.getContinuationResult(BrowseApiHelper.getContinuationQuery(AppClient.WEB, result?.getContinuationToken()!!))

        val continuationResult = RetrofitHelper.get(continuation)

        assertTrue("Topic can be continued", continuationResult?.getItems() != null)
    }

    private fun testReelContinuation(continuation: ReelContinuationResult?) {
        val firstEntry = continuation?.getItems()?.getOrNull(0)
        val details = getReelDetails(firstEntry?.videoId, firstEntry?.params)

        assertNotNull("Contains continuation", continuation?.getContinuationKey())

        testReelWatchEndpoint(firstEntry)
        testReelResult(details)
    }

    private fun testFirstReelResult(details: ReelResult?) {
        // Not present
        assertNotNull("Contains video id", details?.getVideoId())
        assertNotNull("Contains thumbs", details?.getThumbnails())
        assertNotNull("Contains title", details?.getTitle())
        assertNotNull("Contains subtitle", details?.getSubtitle())
        assertNotNull("Contains continuation", details?.getContinuationKey())
        //assertNotNull("Contains feedback", details?.getFeedbackTokens()?.firstOrNull())
    }

    private fun testReelWatchEndpoint(firstEntry: ReelWatchEndpoint?) {
        assertNotNull("Contains video id", firstEntry?.getVideoId())
        assertNotNull("Contains thumbs", firstEntry?.getThumbnails())
    }

    private fun testReelResult(details: ReelResult?) {
        // Not present
        assertNotNull("Contains title", details?.getTitle())
        assertNotNull("Contains subtitle", details?.getSubtitle())
        //assertNotNull("Contains feedback", details?.getFeedbackTokens()?.firstOrNull())
    }

    private fun checkContinuationWeb(token: String?, checkNextToken: Boolean = false) {
        val continuationResult = mService.getContinuationResult(BrowseApiHelper.getContinuationQuery(AppClient.WEB, token!!))

        val continuation = RetrofitHelper.get(continuationResult)

        assertNotNull("Contains items", continuation?.getItems()?.getOrNull(0))

        if (checkNextToken) {
            assertNotNull("Contains next token", continuation?.getContinuationToken())
        }
    }

    private fun checkContinuationTV(token: String?, checkNextToken: Boolean = true) {
        val continuationResult = mService.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(AppClient.TV, token!!))

        val continuation = RetrofitHelper.get(continuationResult)

        assertNotNull("Contains items", continuation?.getItems()?.getOrNull(0))

        if (checkNextToken) {
            assertNotNull("Contains next token", continuation?.getNextPageKey())
        }
    }

    private fun getSubscriptions(): BrowseResult? {
        val subsResult = mService.getBrowseResult(BrowseApiHelper.getSubscriptionsQuery(AppClient.WEB))

        return RetrofitHelper.get(subsResult)
    }

    private fun getHome(): BrowseResult? {
        val homeResult = mService.getBrowseResult(BrowseApiHelper.getHomeQuery(AppClient.WEB))

        return RetrofitHelper.get(homeResult)
    }

    private fun getAltHome(): BrowseResult? {
        val homeResult = mService.getBrowseResultMobile(BrowseApiHelper.getHomeQuery(AppClient.MWEB))

        return RetrofitHelper.get(homeResult)
    }

    private fun getChannelSearch(channelId: String?, query: String?): BrowseResult? {
        val homeResult = mService.getBrowseResult(BrowseApiHelper.getChannelSearchQuery(AppClient.WEB, channelId!!, query!!))

        return RetrofitHelper.get(homeResult)
    }

    private fun getChannelVideos(channelId: String?): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId!!))

        return RetrofitHelper.get(result)
    }

    private fun getChannelLive(channelId: String?): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getChannelLiveQuery(AppClient.WEB, channelId!!))

        return RetrofitHelper.get(result)
    }

    private fun getChannelHome(channelId: String?): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getChannelHomeQuery(AppClient.WEB, channelId!!))

        return RetrofitHelper.get(result)
    }

    private fun getChannelReleases(channelId: String?): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getChannelReleasesQuery(AppClient.WEB, channelId!!))

        return RetrofitHelper.get(result)
    }

    private fun getChannelPlaylist(channelId: String?): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, channelId!!))

        return RetrofitHelper.get(result)
    }

    private fun getGuide(): GuideResult? {
        val guideResult = mService.getGuideResult(PostDataHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)
    }

    private fun getSports(): BrowseResultTV? {
        val kidsResult = mService.getBrowseResultTV(BrowseApiHelper.getSportsQuery(AppClient.TV))

        return RetrofitHelper.get(kidsResult)
    }

    private fun getReel(): ReelResult? {
        val reelsResult = mService.getReelResult(BrowseApiHelper.getReelQuery())

        return RetrofitHelper.get(reelsResult)
    }

    private fun getLikedMusic(): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getLikedMusicQuery(AppClient.WEB))

        return RetrofitHelper.get(result)
    }

    private fun getNewMusicAlbums(): BrowseResult? {
        val result = mService.getBrowseResult(BrowseApiHelper.getNewMusicAlbumsQuery())

        return RetrofitHelper.get(result)
    }

    private fun getReelDetails(videoId: String?, params: String?): ReelResult? {
        val details = mService.getReelResult(BrowseApiHelper.getReelDetailsQuery(AppClient.WEB, videoId!!, params!!))

        return RetrofitHelper.get(details)
    }

    private fun getReelContinuation(sequenceParams: String?): ReelContinuationResult? {
        val continuation = mService.getReelContinuationResult(BrowseApiHelper.getReelContinuationQuery(AppClient.WEB, sequenceParams!!))

        return RetrofitHelper.get(continuation)
    }

    private fun getReelContinuation2(nextPageKey: String?): ReelContinuationResult? {
        val continuation = mService.getReelContinuationResult(BrowseApiHelper.getReelContinuation2Query(AppClient.WEB, nextPageKey!!))

        return RetrofitHelper.get(continuation)
    }

    private fun getTrending(): BrowseResult? {
        val trendingResult = mService.getBrowseResult(BrowseApiHelper.getTrendingQuery(AppClient.WEB))

        return RetrofitHelper.get(trendingResult)
    }

    private fun getTrendingTab(params: String?): BrowseResult? {
        val trendingResult = mService.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, "FEtrending", params))

        return RetrofitHelper.get(trendingResult)
    }

    private fun createOptions(groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS): MediaGroupOptions {
        return MediaGroupOptions.create(
            groupType = groupType
        )
    }
}