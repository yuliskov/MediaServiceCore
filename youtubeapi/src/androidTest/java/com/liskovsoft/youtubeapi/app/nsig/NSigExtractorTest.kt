package com.liskovsoft.youtubeapi.app.nsig

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import com.liskovsoft.youtubeapi.app.AppApiWrapper
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.app.models.AppInfo
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NSigExtractorTest {
    private lateinit var mExtractor: NSigExtractor
    private val mAppApiWrapper = AppApiWrapper()

    @JvmField
    @Rule
    val internetPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET)

    @Before
    fun setUp() {
        mExtractor = NSigExtractor(getPlayerUrl(DefaultHeaders.APP_USER_AGENT))
    }

    private fun getPlayerUrl(userAgent: String): String {
        val appInfo: AppInfo = mAppApiWrapper.getAppInfo(userAgent)

        assertNotNull("AppInfo not null", appInfo)

        val playerUrl = appInfo.playerUrl

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
        var extractor = NSigExtractor(url)

        assertNotNull("NSig not null", extractor.extractNSig("5cNpZqIJ7ixNqU68Y7S"))
    }
}