package com.liskovsoft.youtubeapi.app.potoken

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import java.nio.charset.Charset
import kotlin.random.Random

internal object PoTokenService {
    private const val REQUEST_KEY = "O43z0dpjhgX20SCx4KAo"
    private val appService = AppService.instance()
    
    data class Data(val integrityToken: String?,
                    val estimatedTtlSecs: Int?,
                    val mintRefreshThreshold: Int?,
                    val webSafeFallbackToken: String?)

    data class BotGuardResult(val integrityTokenData: Data?, val postProcessFunction: String?)

    @JvmStatic
    fun getChallenge(): Challenge.Result? {
        val challenge = Challenge()

        val result = challenge.create(getConfig())
        return result
    }

    @JvmStatic
    fun generateIntegrityToken(requestKey: String, botguardResponse: String): BotGuardResult? {
        val wrapper = getConfig().api.generateIntegrityToken(Gson().toJson(arrayOf(requestKey, botguardResponse)))

        val response = RetrofitHelper.get(wrapper) ?: return null

        val integrityToken = response.getOrNull(0)
        val estimatedTtlSecs = response.getOrNull(1)
        val mintRefreshThreshold = response.getOrNull(2)
        val websafeFallbackToken = response.getOrNull(3)

        return BotGuardResult(
            Data(asString(integrityToken), asInt(estimatedTtlSecs), asInt(mintRefreshThreshold), asString(websafeFallbackToken)), null
        )
    }

    private fun getConfig() = BotGuardConfig(RetrofitHelper.create(PoTokenApi::class.java), requestKey = REQUEST_KEY, identifier = appService.visitorData)

    private fun asInt(intElem: JsonElement?) = if (intElem?.isJsonNull == true) null else intElem?.asInt

    private fun asString(strElem: JsonElement?) = if (strElem?.isJsonNull == true) null else strElem?.asString

    @JvmStatic
    fun generateColdStartToken(identifier: String, clientState: Int? = null): String {
        val encodedIdentifier = identifier.toByteArray(Charset.forName("UTF-8"))

        if (encodedIdentifier.size > 118) {
            throw BGError(
                "BAD_INPUT",
                "Content binding is too long.",
                mapOf("identifierLength" to encodedIdentifier.size)
            )
        }

        val timestamp = (System.currentTimeMillis() / 1000).toInt()

        val randomKeys = byteArrayOf(
            Random.nextInt(256).toByte(),
            Random.nextInt(256).toByte()
        )

        val header = ByteArray(2 + 1 + 1 + 4)
        var offset = 0

        randomKeys.copyInto(header, offset)
        offset += 2

        header[offset++] = 0
        header[offset++] = (clientState ?: 1).toByte()

        header[offset++] = ((timestamp ushr 24) and 0xFF).toByte()
        header[offset++] = ((timestamp ushr 16) and 0xFF).toByte()
        header[offset++] = ((timestamp ushr 8) and 0xFF).toByte()
        header[offset]   = (timestamp and 0xFF).toByte()

        val packet = ByteArray(2 + header.size + encodedIdentifier.size)

        packet[0] = 34
        packet[1] = (header.size + encodedIdentifier.size).toByte()

        header.copyInto(packet, 2)
        encodedIdentifier.copyInto(packet, 2 + header.size)

        val payloadOffset = 2
        val keyLength = randomKeys.size

        for (i in keyLength until packet.size - payloadOffset) {
            val idx = payloadOffset + i
            packet[idx] =
                (packet[idx].toInt() xor packet[payloadOffset + (i % keyLength)].toInt()).toByte()
        }

        return u8ToBase64(packet)
    }
}