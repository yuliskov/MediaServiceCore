package com.liskovsoft.youtubeapi.app.nsig

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.youtubeapi.app.AppServiceInt
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor
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
    fun testPlayerVersions() {
        AppConstants.playerUrls.forEach { testPlayerUrl(it) }
    }

    private fun testPlayerUrl(url: String) {
        val extractor = PlayerDataExtractor(url)

        assertNotNull("NSig not null for url $url", extractor.extractNSig("5cNpZqIJ7ixNqU68Y7S"))
    }
}