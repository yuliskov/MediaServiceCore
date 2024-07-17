package com.liskovsoft.youtubeapi.app.nsig

import com.liskovsoft.youtubeapi.common.js.V8Runtime
import java.util.regex.Pattern

internal object JSInterpret {
    private val MATCHING_PARENS = mapOf('(' to ')', '[' to ']', '{' to '}')

    //fun extractFunctionFromCode(argNames: List<String>, code: String): (String?) -> String? {
    //    var modifiedCode = code
    //    while (true) {
    //        val regex = Regex("function\\(([^)]*)\\)\\s*\\{")
    //        val matchResult = regex.find(modifiedCode) ?: break
    //        val (args) = matchResult.destructured
    //        val (start, bodyStart) = matchResult.range.first to matchResult.range.last + 1
    //        val (body, remaining) = separateAtParen(modifiedCode.substring(bodyStart - 1))
    //        val name = namedObject(extractFunctionFromCode(args.split(",").map { it.trim() }, body))
    //        modifiedCode = modifiedCode.substring(0, start) + name + remaining
    //    }
    //    return buildFunction(argNames, modifiedCode)
    //}

    fun extractFunctionFromCode(argNames: List<String>, code: String): (List<String>) -> String? {
        return { args: List<String> ->
            val fullCode =
                "(function (${argNames.joinToString(separator = ",")}) { $code })(${args.joinToString(separator = ",", prefix = "'", postfix = "'")})"
            val result = V8Runtime.instance().evaluate(fullCode)
            result?.toString()
        }
    }

    fun extractFunctionCode(jsCode: String, funcName: String): Pair<List<String>, String>? {
        val pattern = Pattern.compile(
            """(?xs)
                (?:
                    function\s+$funcName|
                    [{;,]\s*$funcName\s*=\s*function|
                    (?:var|const|let)\s+$funcName\s*=\s*function
                )\s*
                \(([^)]*)\)\s*
                (\{.+\})
            """.trimIndent()
        )
        val matcher = pattern.matcher(jsCode)
        if (!matcher.find()) {
            throw IllegalStateException("Could not find JS function \"$funcName\"")
        }
        val args = matcher.group(1)?.split(",")?.map { it.trim() } ?: emptyList()
        val codeBlock = matcher.group(2) ?: ""
        val (code, _) = separateAtParen(codeBlock)
        return Pair(args, code)
    }

    private fun separateAtParen(expr: String, delim: String? = null): Pair<String, String> {
        val delimiter = delim ?: expr.firstOrNull()?.let { MATCHING_PARENS[it].toString() }
        ?: throw IllegalStateException("No delimiter provided and expression is empty")

        val separated = separate(expr, delimiter, 1).toList()
        if (separated.size < 2) {
            throw IllegalStateException("No terminating paren $delimiter")
        }
        return separated[0].substring(1).trim() to separated[1].trim()
    }

    private fun separate(expr: String, delim: String = ",", maxSplit: Int? = null): List<String> {
        val opChars = setOf('+', '-', '*', '/', '%', '&', '|', '^', '=', '<', '>', '!', ',', ';', '{', '}', ':', '[')
        if (expr.isEmpty()) {
            return emptyList()
        }

        val counters = mutableMapOf<Char, Int>().apply { putAll(MATCHING_PARENS.values.map { it to 0 }) }

        var start = 0
        var splits = 0
        var pos = 0
        val delimLen = delim.length - 1
        var inQuote: Char? = null
        var escaping = false
        var afterOp = true
        var inRegexCharGroup = false

        val result = mutableListOf<String>()

        for ((idx, char) in expr.withIndex()) {
            if (inQuote == null && char in MATCHING_PARENS) {
                counters[MATCHING_PARENS[char]!!] = (counters[MATCHING_PARENS[char]!!] ?: 0) + 1
            } else if (inQuote == null && char in counters) {
                if (counters[char]!! > 0) {
                    counters[char] = (counters[char] ?: 0) - 1
                }
            } else if (!escaping) {
                if (char == '"' || char == '\'') {
                    if (inQuote != null && inQuote == char) {
                        inQuote = null
                    } else if (inQuote == null && (afterOp || char != '/')) {
                        inQuote = char
                    }
                } else if (inQuote == '/' && char in listOf('[', ']')) {
                    inRegexCharGroup = char == '['
                }
            }
            escaping = !escaping && inQuote != null && char == '\\'
            val inUnaryOp = (inQuote == null && !inRegexCharGroup && afterOp && char in setOf('-', '+'))
            afterOp = if (inQuote == null && char in opChars) true else (char.isWhitespace() && afterOp)  // ??? apfterOp

            if (char != delim[pos] || counters.values.any { it != 0 } || inQuote != null || inUnaryOp) {
                pos = 0
            } else if (pos != delimLen) {
                pos++
            } else {
                result.add(expr.substring(start, idx - delimLen))
                start = idx + 1
                pos = 0
                splits++
                if (maxSplit != null && splits >= maxSplit) {
                    break
                }
            }
        }
        result.add(expr.substring(start))
        return result
    }
}