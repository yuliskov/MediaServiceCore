package com.liskovsoft.youtubeapi.app.playerdata

import android.Manifest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.common.helpers.AppConstants
import com.liskovsoft.youtubeapi.app.AppServiceInt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
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
    fun testThatTVAndWebHasSameResult() {
        val urlTV = AppConstants.playerUrls.first()
        val urlWeb = urlTV.replace("/tv-player-es6.vflset/tv-player-es6.js", "/player_es6.vflset/en_US/base.js")

        assertNotEquals("Using tv and web urls", urlTV, urlWeb)

        val firstNSig = testNSigPlayerUrl(urlTV)
        val secondNSig = testNSigPlayerUrl(urlWeb)

        assertEquals("NSig: tv and web produces the same result", firstNSig, secondNSig)

        val firstSig = testSigPlayerUrl(urlTV)
        val secondSig = testSigPlayerUrl(urlWeb)

        assertEquals("Sig: tv and web produces the same result", firstSig, secondSig)
    }

    private fun testNSigPlayerUrl(url: String): String? {
        val extractor = PlayerDataExtractor(url)

        val nParam = "5cNpZqIJ7ixNqU68Y7S"
        val result = extractor.extractNSig(nParam)
        assertNotNull("NSig not null for url $url", result)
        assertNotEquals("NSig not equal failed for url $url", nParam, result)

        return result
    }

    private fun testSigPlayerUrl(url: String): String? {
        val extractor = PlayerDataExtractor(url)

        val sigParam = "NJAJEij0EwRgIhAI0KExTgjfPk-MPM9MAdzyyPRt=BM8-XO5tm5hlMCSVpAiEAv7eP3CURqZNSPow8BXXAoazVoXgeMP7gH9BdylHCwgw=gwzz"
        val result = extractor.extractSig(listOf(sigParam))
        assertNotNull("Sig not null for url $url", result?.firstOrNull())
        assertNotEquals("Sig not equal failed for url $url", sigParam, result?.firstOrNull())

        return result?.firstOrNull()
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

        val timestampLength = if (url.contains("-tcl.")) 8 else 5
        assertEquals("Timestamp length is $timestampLength", timestampLength, timestamp?.length ?: 0)
    }

    private fun getTestingUrls(): List<String> = AppConstants.playerUrls.take(5) // do limit to avoid OOM
}