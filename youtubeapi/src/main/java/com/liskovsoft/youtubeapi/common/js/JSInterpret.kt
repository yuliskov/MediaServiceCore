package com.liskovsoft.youtubeapi.common.js

import java.util.regex.Pattern

internal object JSInterpret {
    private val MATCHING_PARENS = mapOf('(' to ')', '{' to '}', '[' to ']')

    fun extractFunctionFromCode(argNames: List<String>, code: String): (List<String>) -> String? {
        return { args: List<String> ->
            val fullCode =
                "(function (${argNames.joinToString(separator = ",")}) { $code })(${args.joinToString(separator = ",", prefix = "'", postfix = "'")})"
            val result = V8Runtime.instance().evaluate(fullCode)
            result?.toString()
        }
    }

    /**
     * yt_dlp.jsinterp.JSInterpreter.extract_function_code
     *
     * yt-dlp\yt_dlp\jsinterp.py
     */
    fun extractFunctionCode(jsCode: String, funcName: String): Pair<List<String>, String> {
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

    /**
     * yt_dlp.jsinterp.JSInterpreter._separate_at_paren
     */
    private fun separateAtParen(expr: String, delim: Char? = null): Pair<String, String> {
        val delimiter = delim ?: expr.firstOrNull()?.let { MATCHING_PARENS[it] }
        ?: throw IllegalStateException("No delimiter provided and expression is empty")

        val separated = separate(expr, delimiter, 1).toList()
        //val separated = separateOld(expr, delimiter.toString(), 1).toList()
        if (separated.size < 2) {
            if (separated.size == 1) {
                // No matched paren. Probably, we should delete the last paren.
                return separated[0].substring(0, separated[0].length - 1).trim() to ""
            } else {
                throw IllegalStateException("No terminating paren $delimiter in expression $expr")
            }
        }
        return separated[0].substring(1).trim() to separated[1].trim()
    }

    /**
     * yt_dlp.jsinterp.JSInterpreter._separate
     */
    private fun separate(expr: String, delim: Char = ',', maxSplit: Int? = null): List<String> {
        val opChars = "+-*/%&|^=<>!,;{}:["
        if (expr.isEmpty()) return emptyList()

        val counters = MATCHING_PARENS.values.associateWith { 0 }.toMutableMap()
        val quotes = "\"'`/"
        var start = 0
        var splits = 0
        var pos = 0
        val delimLen = delim.toString().length - 1
        var inQuote: Char? = null
        var escaping = false
        var afterOp = true
        var inRegexCharGroup = false
        val result = mutableListOf<String>()

        for ((idx, char) in expr.withIndex()) {
            if (inQuote == null && MATCHING_PARENS.containsKey(char)) {
                counters[MATCHING_PARENS[char]!!] = counters[MATCHING_PARENS[char]!!]!! + 1
            } else if (inQuote == null && counters.containsKey(char)) {
                if (counters[char]!! > 0) {
                    counters[char] = counters[char]!! - 1
                }
            } else if (!escaping) {
                if (char in quotes && (inQuote == null || inQuote == char)) {
                    if (inQuote != null || afterOp || char != '/') {
                        inQuote = if (inQuote != null && !inRegexCharGroup) null else char
                    } else if (inQuote == '/' && (char == '[' || char == ']')) {
                        inRegexCharGroup = char == '['
                    }
                }
                escaping = !escaping && inQuote != null && char == '\\'
                val inUnaryOp = inQuote == null && !inRegexCharGroup && afterOp !in listOf(true, false) && char in "-+"
                afterOp = if (inQuote == null && char in opChars) char != null else if (char.isWhitespace()) afterOp else false

                if (char != delim || counters.values.any { it > 0 } || inQuote != null || inUnaryOp) {
                    pos = 0
                    continue
                } else if (pos != delimLen) {
                    pos += 1
                    continue
                }
                result.add(expr.substring(start, idx - delimLen))
                start = idx + 1
                pos = 0
                splits += 1
                if (maxSplit != null && splits >= maxSplit) {
                    break
                }
            }
        }
        result.add(expr.substring(start))
        return result
    }

    /**
     * yt_dlp.jsinterp.JSInterpreter._separate
     */
    private fun separateOld(expr: String, delim: String = ",", maxSplit: Int? = null): List<String> {
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

    fun searchJson(startPattern: Pattern, content: String, endPattern: Pattern = Pattern.compile(";"),
                   containsPattern: Pattern = Pattern.compile("""\{(?s:.+?)\}""")): String? {
        val jsonRegex = Pattern.compile("""(?:$startPattern)\s*($containsPattern)\s*(?:$endPattern)""")
        val matcher = jsonRegex.matcher(content)

        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1)
        }

        return null
    }
}