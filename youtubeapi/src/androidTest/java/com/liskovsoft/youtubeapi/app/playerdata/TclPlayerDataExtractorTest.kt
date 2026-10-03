package com.liskovsoft.youtubeapi.app.playerdata

import android.Manifest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppServiceInt
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.helpers.AppConstants
import com.liskovsoft.youtubeapi.videoinfo.BaseVideoInfoApiTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

private const val TCL_TIMESTAMP_LENGTH = 8

class TclPlayerDataExtractorTest: BaseVideoInfoApiTest() {
    private val mAppServiceInt = AppServiceInt()

    @JvmField
    @Rule
    val internetPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET)

    @Before
    fun setUp() {
        initBase()
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
        val extractor = TclPlayerDataExtractor(playerUrl)
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

    @Ignore("Not implemented yet")
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
    fun testSabrUrlAccessible() {
        val videoInfo = getVideoInfo(AppClient.TV, TestHelpers.VIDEO_ID_CAPTIONS)

        assertNotNull("Contains adaptive formats error: ${videoInfo.playabilityStatus}", videoInfo.adaptiveFormats)

        decipherFormats(videoInfo)

        assertTrue("Sabr format exists", TestHelpers.urlExists(videoInfo.serverAbrStreamingUrl))
    }

    private fun testNSigPlayerUrl(url: String): String? {
        val extractor = TclPlayerDataExtractor(url)

        val nParam = "JJGumBcSHg0ByKxL"
        val result = extractor.extractNSig(nParam)
        assertNotNull("NSig not null for url $url", result)
        assertNotEquals("NSig not equal failed for url $url", nParam, result)
        assertTrue("NSig doesn't exceeds 17 chars for url $url with len=${result?.length ?: -1}", (result?.length ?: -1) <= 17)

        return result
    }

    private fun testSigPlayerUrl(url: String): String? {
        val extractor = TclPlayerDataExtractor(url)

        val sigParam = "NJAJEij0EwRgIhAI0KExTgjfPk-MPM9MAdzyyPRt=BM8-XO5tm5hlMCSVpAiEAv7eP3CURqZNSPow8BXXAoazVoXgeMP7gH9BdylHCwgw=gwzz"
        val result = extractor.extractSig(listOf(sigParam))
        assertNotNull("Sig not null for url $url", result?.firstOrNull())
        assertNotEquals("Sig not equal failed for url $url", sigParam, result?.firstOrNull())

        return result?.firstOrNull()
    }

    private fun testCPNPlayerUrl(url: String) {
        val extractor = TclPlayerDataExtractor(url)

        val cpn = extractor.createClientPlaybackNonce()
        assertNotNull("CPN not null for url $url", cpn)
    }

    private fun testTimestampPlayerUrl(url: String) {
        val extractor = TclPlayerDataExtractor(url)

        val timestamp = extractor.getSignatureTimestamp()
        assertNotNull("Timestamp not null for url $url", timestamp)

        assertEquals("Timestamp length is $TCL_TIMESTAMP_LENGTH", TCL_TIMESTAMP_LENGTH, timestamp?.length ?: 0)
    }

    private fun getTestingUrls(): List<String> = AppConstants.playerUrls.take(5) // do limit to avoid OOM
}