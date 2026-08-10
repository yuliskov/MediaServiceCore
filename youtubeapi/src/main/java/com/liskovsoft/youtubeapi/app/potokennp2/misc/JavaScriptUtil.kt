package com.liskovsoft.youtubeapi.app.potokennp2.misc

import com.grack.nanojson.JsonObject
import com.grack.nanojson.JsonParser
import com.grack.nanojson.JsonWriter
import com.liskovsoft.sharedutils.okhttp.OkHttpManager
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenException
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.toByteString
import java.util.regex.Pattern

/**
 * Parses the raw challenge data obtained from the Create endpoint and returns an object that can be
 * embedded in a JavaScript snippet.
 */
internal fun parseChallengeData(rawChallengeData: String): String {
    val scrambled = JsonParser.array().from(rawChallengeData)

    val challengeData = if (scrambled.size > 1 && scrambled.isString(1)) {
        val descrambled = descramble(scrambled.getString(1))
        JsonParser.array().from(descrambled)
    } else {
        // Fixes a regression, where if the challenge data array size was one, the second element
        // would be accessed, leading to a crash.
        // This was introduced when porting the challenge parsing from JS to
        // Kotlin.
        //scrambled.getArray(1)
        scrambled.getArray(0)
    }

    val messageId = challengeData.getString(0)
    val interpreterHash = challengeData.getString(3)
    val program = challengeData.getString(4)
    val globalName = challengeData.getString(5)
    val clientExperimentsStateBlob = challengeData.getString(7)

    val privateDoNotAccessOrElseSafeScriptWrappedValue = challengeData.getArray(1, null)?.find { it is String }
    val privateDoNotAccessOrElseTrustedResourceUrlWrappedValue = challengeData.getArray(2, null)?.find { it is String }

    return JsonWriter.string(
        JsonObject.builder()
            .value("messageId", messageId)
            .`object`("interpreterJavascript")
            .value("privateDoNotAccessOrElseSafeScriptWrappedValue", privateDoNotAccessOrElseSafeScriptWrappedValue)
            .value("privateDoNotAccessOrElseTrustedResourceUrlWrappedValue", privateDoNotAccessOrElseTrustedResourceUrlWrappedValue)
            .end()
            .value("interpreterHash", interpreterHash)
            .value("program", program)
            .value("globalName", globalName)
            .value("clientExperimentsStateBlob", clientExperimentsStateBlob)
            .done()
    )
}

/**
 * Parses the raw challenge data obtained from the Create endpoint and returns an object that can be
 * embedded in a JavaScript snippet.
 */
internal fun parseDescrambledChallengeData(rawChallengeData: String): String {
    val root = JsonParser.`object`().from(rawChallengeData)
    val bgChallenge = root.getObject("bgChallenge")

    val interpreterHash = bgChallenge.getString("interpreterHash")
    val program = bgChallenge.getString("program")
    val globalName = bgChallenge.getString("globalName")
    val clientExperimentsStateBlob = bgChallenge.getString("clientExperimentsStateBlob")

    val privateDoNotAccessOrElseTrustedResourceUrlWrappedValue = bgChallenge
        .getObject("interpreterUrl")
        .getString("privateDoNotAccessOrElseTrustedResourceUrlWrappedValue")
    val privateDoNotAccessOrElseSafeScriptWrappedValue =
        OkHttpManager.instance().doGetRequest("https:$privateDoNotAccessOrElseTrustedResourceUrlWrappedValue").body()?.string()
            ?: throw PoTokenException("Empty response body")

    return JsonWriter.string(
        JsonObject.builder()
            .`object`("interpreterJavascript")
            .value("privateDoNotAccessOrElseSafeScriptWrappedValue", privateDoNotAccessOrElseSafeScriptWrappedValue)
            .value("privateDoNotAccessOrElseTrustedResourceUrlWrappedValue", privateDoNotAccessOrElseTrustedResourceUrlWrappedValue)
            .end()
            .value("interpreterHash", interpreterHash)
            .value("program", program)
            .value("globalName", globalName)
            .value("clientExperimentsStateBlob", clientExperimentsStateBlob)
            .done()
    )
}

/**
 * ```text
 * --- PATCH(unstem 2026-08): homepage challenge + ytcfg (BgUtils#44) ------
 * parseLooseJSON vendored from LuanRT/BgUtils v4.0.3 (MIT). The ytAtN
 * payload is JS-object-literal-ish, not strict JSON.
 * ```
 */
internal fun parseLooseJSON(looseJson: String): Map<String, String> {
    val hexPattern = Pattern.compile("""\\x([0-9A-Fa-f]{2})""")
    val hexMatcher = hexPattern.matcher(looseJson)

    val sanitizedString = buildString {
        var lastEnd = 0

        while (hexMatcher.find()) {
            append(looseJson, lastEnd, hexMatcher.start())
            append(hexMatcher.group(1)!!.toInt(16).toChar())
            lastEnd = hexMatcher.end()
        }

        append(looseJson, lastEnd, looseJson.length)
    }

    val trailingCommaPattern = Pattern.compile(""",\s*([\]}])""")
    val trailingCommaMatcher = trailingCommaPattern.matcher(sanitizedString)
    var jsonStr = trailingCommaMatcher.replaceAll("$1")

    val singleQuotePattern = Pattern.compile("""'((?:[^'\\]|\\[\s\S])*)'""")
    val singleQuoteMatcher = singleQuotePattern.matcher(jsonStr)

    jsonStr = buildString {
        var lastEnd = 0

        while (singleQuoteMatcher.find()) {
            append(jsonStr, lastEnd, singleQuoteMatcher.start())

            val innerStr = singleQuoteMatcher.group(1)!!
                .replace("""\'""", "'")

            append(quoteJson(innerStr))

            lastEnd = singleQuoteMatcher.end()
        }

        append(jsonStr, lastEnd, jsonStr.length)
    }

    val unquotedKeyPattern = Pattern.compile("""([{,]\s*)([a-zA-Z0-9_$]+)\s*:""")
    val unquotedKeyMatcher = unquotedKeyPattern.matcher(jsonStr)
    jsonStr = unquotedKeyMatcher.replaceAll("""$1"$2":""")

    val parsedData = JsonParser.`object`().from(jsonStr)
    val result = LinkedHashMap<String, String>()

    for ((key, value) in parsedData) {
        result[key] = when (value) {
            null -> "null"
            else -> value.toString()
        }
    }

    return result
}

private fun quoteJson(value: String): String =
    buildString {
        append('"')
        for (char in value) {
            when (char) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> {
                    if (char.code < 0x20) {
                        // Securely escape raw control chars to match RFC spec
                        //append("\\u%04x".format(char.code))
                        append("\\u00")
                        // Fast hex conversion for values 0-31 without allocations
                        append("0123456789abcdef"[char.code ushr 4])
                        append("0123456789abcdef"[char.code and 0x0F])
                    } else {
                        append(char)
                    }
                }
            }
        }
        append('"')
    }

/**
 * Parses the raw integrity token data obtained from the GenerateIT endpoint to a JavaScript
 * `Uint8Array` that can be embedded directly in JavaScript code, and an [Int] representing the
 * duration of this token in seconds.
 */
internal fun parseIntegrityTokenData(rawIntegrityTokenData: String): Pair<String, Long> {
    val integrityTokenData = JsonParser.array().from(rawIntegrityTokenData)
    return base64ToU8(integrityTokenData.getString(0)) to integrityTokenData.getLong(1)
}

/**
 * Converts a string (usually the identifier used as input to `obtainPoToken`) to a JavaScript
 * `Uint8Array` that can be embedded directly in JavaScript code.
 */
internal fun stringToU8(identifier: String): String {
    return newUint8Array(identifier.toByteArray())
}

/**
 * Takes a poToken encoded as a sequence of bytes represented as integers separated by commas
 * (e.g. "97,98,99" would be "abc"), which is the output of `Uint8Array::toString()` in JavaScript,
 * and converts it to the specific base64 representation for poTokens.
 */
internal fun u8ToBase64(poToken: String): String {
    return poToken.split(",")
        .map { it.toUByte().toByte() }
        .toByteArray()
        .toByteString()
        .base64()
        .replace("+", "-")
        .replace("/", "_")
}

/**
 * Takes the scrambled challenge, decodes it from base64, adds 97 to each byte.
 */
private fun descramble(scrambledChallenge: String): String {
    return base64ToByteString(scrambledChallenge)
        .map { (it + 97).toByte() }
        .toByteArray()
        .decodeToString()
}

/**
 * Decodes a base64 string encoded in the specific base64 representation used by YouTube, and
 * returns a JavaScript `Uint8Array` that can be embedded directly in JavaScript code.
 */
private fun base64ToU8(base64: String): String {
    return newUint8Array(base64ToByteString(base64))
}

private fun newUint8Array(contents: ByteArray): String {
    return "new Uint8Array([" + contents.joinToString(separator = ",") { it.toUByte().toString() } + "])"
}

/**
 * Decodes a base64 string encoded in the specific base64 representation used by YouTube.
 */
private fun base64ToByteString(base64: String): ByteArray {
    val base64Mod = base64
        .replace('-', '+')
        .replace('_', '/')
        .replace('.', '=')

    return (base64Mod.decodeBase64() ?: throw PoTokenException("Cannot base64 decode"))
        .toByteArray()
}
