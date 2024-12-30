package com.liskovsoft.youtubeapi.channelgroups.importing.newpipe

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.sharedutils.TestHelpers
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen.GrayJayGroup
import com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.gen.NewPipeSubscriptionsGroup
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class NewPipeApiTest {
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
    }

    @Test
    fun testResponse() {
        val newPipeContent = TestHelpers.readResource("channelgroups/newpipe_subscriptions.json")

        val gson = Gson()
        val myType = object : TypeToken<NewPipeSubscriptionsGroup>() {}.type

        val response: NewPipeSubscriptionsGroup = try {
            gson.fromJson(newPipeContent, myType)
        } catch (e: JsonSyntaxException) {
            return
        }

        assertTrue("Response not empty", response.subscriptions?.isNotEmpty() == true)
    }
}