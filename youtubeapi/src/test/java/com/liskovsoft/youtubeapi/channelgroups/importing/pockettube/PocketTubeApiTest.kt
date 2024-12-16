package com.liskovsoft.youtubeapi.channelgroups.importing.pockettube

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class PocketTubeApiTest {
    private lateinit var mFileService: FileApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mFileService = RetrofitHelper.create(FileApi::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testResult() {
        val content = mFileService.getContent("https://github.com/yuliskov/SmartTube/releases/download/latest/pockettube.json")

        val pocketTubeContent = RetrofitHelper.get(content)?.content

        // Find group names
        val groupNames: List<String> = JsonPath.read(pocketTubeContent, "$.ysc_collection.*~") // get keys query

        assertTrue("Group names not empty", groupNames.isNotEmpty())

        for (groupName in groupNames) {
            // Get groups content
            val channelIds: List<String> = JsonPath.read(pocketTubeContent, "$.$groupName")

            assertTrue("Channel ids not empty", channelIds.isNotEmpty())
        }
    }

    @Test
    fun testWrongResult() {
        val content = mFileService.getContent("https://github.com/yuliskov/SmartTube/releases/download/latest/grayjay_subscription_groups")

        val pocketTubeContent = RetrofitHelper.get(content)?.content

        // Find group names
        val groupNames: List<String> = try {
            JsonPath.read(pocketTubeContent, "$.ysc_collection.*~")
        } catch (e: PathNotFoundException) {
            return
        }

        assertTrue("Group names not empty", groupNames.isNotEmpty())

        for (groupName in groupNames) {
            // Get groups content
            val channelIds: List<String> = JsonPath.read(pocketTubeContent, "$.$groupName")

            assertTrue("Channel ids not empty", channelIds.isNotEmpty())
        }
    }
}