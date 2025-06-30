package com.liskovsoft.youtubeapi.app.nsig

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.youtubeapi.app.AppServiceInt
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class PlayerDataExtractorTest {
    private val mAppServiceInt = AppServiceInt()

    @JvmField
    @Rule
    val internetPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET)

    private fun getPlayerUrl(): String {
        val playerUrl = mAppServiceInt.playerUrl

        assertNotNull("Player url not null", playerUrl)

        return playerUrl
    }

    @Test
    fun testExtractNSig() {
        val extractor = PlayerDataExtractor(getPlayerUrl())
        assertNotNull("NSig not null", extractor.extractNSig("5cNpZqIJ7ixNqU68Y7S"))
    }

    @Test
    fun testNSigPlayerVersions() {
        AppConstants.playerUrls.forEach { testNSigPlayerUrl(it) }
    }

    @Test
    fun testSigPlayerVersions() {
        AppConstants.playerUrls.forEach { testSigPlayerUrl(it) }
    }

    @Test
    fun testCPNPlayerVersions() {
        AppConstants.playerUrls.forEach { testCPNPlayerUrl(it) }
    }

    @Test
    fun testTimestampPlayerVersions() {
        AppConstants.playerUrls.forEach { testTimestampPlayerUrl(it) }
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
        val nSig = extractor.extractSig(sigParam)
        assertNotNull("Sig not null for url $url", nSig)
        assertNotEquals("Sig not equal failed for url $url", sigParam, nSig)
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
}