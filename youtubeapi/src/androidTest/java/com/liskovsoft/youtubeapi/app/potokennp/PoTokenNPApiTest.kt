package com.liskovsoft.youtubeapi.app.potokennp

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.googleapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PoTokenNPApiTest {
    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testWebPoTokenIsNotEmpty() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken(TestHelpersV2.VIDEO_ID_3)

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken("")

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }
}