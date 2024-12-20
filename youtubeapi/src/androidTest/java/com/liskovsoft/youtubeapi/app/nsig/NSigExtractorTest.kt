package com.liskovsoft.youtubeapi.app.nsig

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.youtubeapi.app.AppServiceInt
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.app.models.AppInfo
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NSigExtractorTest {
    private lateinit var mExtractor: NSigExtractor
    private val mAppServiceInt = AppServiceInt()

    @JvmField
    @Rule
    val internetPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET)

    @Before
    fun setUp() {
        mExtractor = NSigExtractor(getPlayerUrl())
    }

    private fun getPlayerUrl(): String {
        val playerUrl = mAppServiceInt.playerUrl

        assertNotNull("Player url not null", playerUrl)

        return playerUrl
    }

    @Test
    fun testExtractNSig() {
        assertNotNull("NSig not null", mExtractor.extractNSig("5cNpZqIJ7ixNqU68Y7S"))
    }

    @Test
    fun testPlayerVersions() {
        AppConstants.playerUrls.forEach { testPlayerUrl(it) }
    }

    private fun testPlayerUrl(url: String) {
        val extractor = NSigExtractor(url)

        assertNotNull("NSig not null for url $url", extractor.extractNSig("5cNpZqIJ7ixNqU68Y7S"))
    }
}