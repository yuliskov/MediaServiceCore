package com.liskovsoft.youtubeapi.app.nsig

import android.Manifest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.common.helpers.AppConstants
import com.liskovsoft.youtubeapi.app.AppServiceInt
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class PlayerDataExtractorTest {
    private val mAppServiceInt = AppServiceInt()

    @JvmField
    @Rule
    val internetPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET)

    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
    }

    private fun getPlayerUrl(): String {
        val playerUrl = mAppServiceInt.playerUrl

        assertNotNull("Player url not null", playerUrl)

        return playerUrl
    }

    @Test
    fun testExtractNSig() {
        val playerUrl = getPlayerUrl()
        testPlayerExtractorValid(playerUrl)
        testPlayerExtractorValid(getTestingUrls().first())
    }

    private fun testPlayerExtractorValid(playerUrl: String) {
        val extractor = PlayerDataExtractor(playerUrl)
        assertNotNull("NSig not null for $playerUrl", extractor.extractNSig("5cNpZqIJ7ixNqU68Y7S"))
        assertTrue("PlayerExtractor validated", extractor.validate())
    }

    @Test
    fun testThatDecipherFunctionIsValid() {
        val playerUrl = mAppServiceInt.playerUrl

        mAppServiceInt.getPlayerDataExtractor(playerUrl).validate()
    }
    
    @Test
    fun testNSigPlayerVersions() {
        getTestingUrls().forEach { testNSigPlayerUrl(it) }
    }
    
    @Test
    fun testSigPlayerVersions() {
        getTestingUrls().forEach { testSigPlayerUrl(it) }
    }

    @Test
    fun testCPNPlayerVersions() {
        getTestingUrls().forEach { testCPNPlayerUrl(it) }
    }

    @Test
    fun testTimestampPlayerVersions() {
        getTestingUrls().forEach { testTimestampPlayerUrl(it) }
    }

    @Test
    fun testSingleNSigPlayerVersion() {
        testNSigPlayerUrl("https://www.youtube.com/s/player/17ad44a3/tv-player-es6.vflset/tv-player-es6.js")
        //testNSigPlayerUrl("https://www.youtube.com/s/player/a61444a1/tv-player-es6.vflset/tv-player-es6.js")
        testNSigPlayerUrl("https://www.youtube.com/s/player/a61444a1/tv-player-ias.vflset/tv-player-ias.js")

        testNSigPlayerUrl("https://www.youtube.com/s/player/3d3ba064/player_ias_tce.vflset/en_US/base.js")
        testNSigPlayerUrl("https://www.youtube.com/s/player/2b83d2e0/tv-player-ias.vflset/tv-player-ias.js")
    }

    @Test
    fun testSingleSigPlayerVersion() {
        testSigPlayerUrl("https://www.youtube.com/s/player/17ad44a3/tv-player-es6.vflset/tv-player-es6.js")
        testSigPlayerUrl("https://www.youtube.com/s/player/2b83d2e0/tv-player-ias.vflset/tv-player-ias.js")
        //testSigPlayerUrl("https://www.youtube.com/s/player/2b83d2e0/tv-player-es6.vflset/tv-player-es6.js")
    }

    @Test
    fun testSingleCPNPlayerVersion() {
        //testNSigPlayerUrl("https://www.youtube.com/s/player/a61444a1/tv-player-es6.vflset/tv-player-es6.js")
        testCPNPlayerUrl("https://www.youtube.com/s/player/a61444a1/tv-player-ias.vflset/tv-player-ias.js")

        testCPNPlayerUrl("https://www.youtube.com/s/player/3d3ba064/player_ias_tce.vflset/en_US/base.js")
        testCPNPlayerUrl("https://www.youtube.com/s/player/2b83d2e0/tv-player-ias.vflset/tv-player-ias.js")
    }

    @Test
    fun testSingleTimestampPlayerVersion() {
        //testNSigPlayerUrl("https://www.youtube.com/s/player/a61444a1/tv-player-es6.vflset/tv-player-es6.js")
        testTimestampPlayerUrl("https://www.youtube.com/s/player/a61444a1/tv-player-ias.vflset/tv-player-ias.js")

        testTimestampPlayerUrl("https://www.youtube.com/s/player/3d3ba064/player_ias_tce.vflset/en_US/base.js")
        testTimestampPlayerUrl("https://www.youtube.com/s/player/2b83d2e0/tv-player-ias.vflset/tv-player-ias.js")
    }

    private fun testNSigPlayerUrl(url: String) {
        val extractor = PlayerDataExtractor(url)

        val nParam = "5cNpZqIJ7ixNqU68Y7S"
        val nSig = extractor.extractNSig(nParam)
        assertNotNull("NSig not null for url $url", nSig)
        assertNotEquals("NSig not equal failed for url $url", nParam, nSig)
    }

    private fun testSigPlayerUrl(url: String) {
        val extractor = PlayerDataExtractor(url)

        val sigParam = "5cNpZqIJ7ixNqU68Y7S"
        val nSig = extractor.extractSig(listOf(sigParam))
        assertNotNull("Sig not null for url $url", nSig?.firstOrNull())
        assertNotEquals("Sig not equal failed for url $url", sigParam, nSig?.firstOrNull())
    }

    private fun testCPNPlayerUrl(url: String) {
        val extractor = PlayerDataExtractor(url)

        val cpn = extractor.createClientPlaybackNonce()
        assertNotNull("CPN not null for url $url", cpn)
    }

    private fun testTimestampPlayerUrl(url: String) {
        val extractor = PlayerDataExtractor(url)

        val timestamp = extractor.getSignatureTimestamp()
        assertNotNull("Timestamp not null for url $url", timestamp)
    }

    private fun getTestingUrls(): List<String> = AppConstants.playerUrls.take(5) // do limit to avoid OOM
}