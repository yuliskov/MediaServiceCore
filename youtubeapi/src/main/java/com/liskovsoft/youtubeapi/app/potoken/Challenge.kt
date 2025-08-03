package com.liskovsoft.youtubeapi.app.potoken

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal class Challenge {
    data class Result(val messageId: String?,
                      val interpreterJavascript: InterpreterJavascript?,
                      val interpreterHash: String?,
                      val program: String?,
                      val globalName: String?,
                      val clientExperimentsStateBlob: String?) {
        data class InterpreterJavascript(val privateDoNotAccessOrElseSafeScriptWrappedValue: String?)
    }
    data class Data(val messageId: String?,
                    val wrappedScript: List<String?>?,
                    val unknown1: String?,
                    val interpreterHash: String?,
                    val program: String?,
                    val globalName: String?,
                    val unknown2: String?,
                    val clientExperimentsStateBlob: String?)

    fun create(bgConfig: BotGuardConfig, interpreterHash: String? = null): Result? {
        val requestKey = bgConfig.requestKey
        val payload = listOfNotNull(requestKey, interpreterHash)
        val wrapper = bgConfig.api.createChallenge(Gson().toJson(payload))

        val result = RetrofitHelper.get(wrapper) ?: return null

        return parseChallengeData(result)
    }

    private fun parseChallengeData(rawData: List<String?>): Result {
        var challengeData: Data? = null

        if (rawData.getOrNull(1)?.isNotEmpty() == true) {
            val descrambled = descramble(rawData[1]!!)
            val jsonElements  = Gson().fromJson(descrambled, JsonArray::class.java)

            challengeData = Data(
                jsonElements[0].asString,
                jsonElements[1].asJsonArray?.map { if (it?.isJsonNull == true) null else it?.asString },
                null,
                jsonElements[3].asString,
                jsonElements[4].asString,
                jsonElements[5].asString,
                null,
                jsonElements[7].asString,
            )
        } else if (rawData.getOrNull(0)?.isNotEmpty() == true) {
            TODO("Not yet implemented")
            // challengeData = rawData[0];
        }

        val privateDoNotAccessOrElseSafeScriptWrappedValue = challengeData?.wrappedScript?.firstNotNullOfOrNull { it }

        return Result(
            challengeData?.messageId,
            Result.InterpreterJavascript(privateDoNotAccessOrElseSafeScriptWrappedValue),
            challengeData?.interpreterHash,
            challengeData?.program,
            challengeData?.globalName,
            challengeData?.clientExperimentsStateBlob
        )
    }

    /**
     * Descrambles the given challenge data.
     */
    private fun descramble(scrambledChallenge: String): String? {
        val buffer = Base64.decode(scrambledChallenge, Base64.DEFAULT)

        if (buffer.isEmpty())
            return null

        return buffer.map { (it + 97).toByte() }.toByteArray().toString(Charsets.UTF_8)
    }
}