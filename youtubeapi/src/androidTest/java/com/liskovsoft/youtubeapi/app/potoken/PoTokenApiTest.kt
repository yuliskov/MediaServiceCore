package com.liskovsoft.youtubeapi.app.potoken

import com.liskovsoft.sharedutils.TestHelpers
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.V8Runtime
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class PoTokenApiTest {
    private val requestKey = "O43z0dpjhgX20SCx4KAo"
    private lateinit var visitorData: String
    private val program = "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"

    @Before
    fun setUp() {
        visitorData = "CgtXYm83Ni15aTkyWSjxrbu4BjIKCgJVQRIEGgAgHA%3D%3D"
    }

    @Test
    fun testOpenPrivateJSFile() {
        evaluate(TestHelpers.readResource("potoken/privateScript.js"), program)
    }

    @Test
    fun testGetChallenge() {
        val result = getChallenge()

        assertNotNull("Private script not null", result?.interpreterJavascript?.privateDoNotAccessOrElseSafeScriptWrappedValue)

        evaluate(result?.interpreterJavascript?.privateDoNotAccessOrElseSafeScriptWrappedValue, result?.program)
    }

    @Ignore("Not fixed yet")
    @Test
    fun testGetPoToken() {
        val result = getChallenge()

        val poToken = PoToken()

        val poTokenResult = poToken.generate(
            PoToken.Arguments(
                result?.interpreterJavascript?.privateDoNotAccessOrElseSafeScriptWrappedValue,
                result?.program,
                result?.globalName,
                getConfig()
            )
        )

        assertNotNull("PoToken not null", poTokenResult?.poToken)
    }

    private fun getChallenge(): Challenge.Result? {
        val challenge = Challenge()

        val result = challenge.create(getConfig())
        return result
    }

    private fun getConfig() = BotGuardConfig(RetrofitHelper.create(PoTokenApi::class.java), requestKey = requestKey, identifier = visitorData)

    private fun evaluate(scriptContent: String?, program: String?) {
        val script = listOf(
            "window = {}; window.document = {};",
            scriptContent,
            "var vm = trayride; vm.a('${program}', () => {}, true, undefined, () => {}).toString();"
        )

        val result = V8Runtime.instance().evaluate(script.joinToString(""))

        assertNotNull(result)
    }
}