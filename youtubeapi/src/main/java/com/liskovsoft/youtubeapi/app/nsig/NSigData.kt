package com.liskovsoft.youtubeapi.app.nsig

import com.liskovsoft.sharedutils.helpers.Helpers

private const val DELIM: String = "%nsd%"

internal data class NSigData(
    val nFuncPlayerUrl: String,
    val nFuncParams: List<String>,
    val nFuncCode: String
) {
    override fun toString(): String {
        return Helpers.merge(DELIM, nFuncPlayerUrl, nFuncParams, nFuncCode)
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): NSigData? {
            if (spec == null)
                return null

            val split = Helpers.split(DELIM, spec)

            val nFuncPlayerUrl = Helpers.parseStr(split, 0)
            val nFuncParams = Helpers.parseStrList(split, 1)
            val nFuncCode = Helpers.parseStr(split, 2)

            if (Helpers.anyNull(nFuncPlayerUrl, nFuncParams, nFuncCode))
                return null

            return NSigData(nFuncPlayerUrl, nFuncParams, nFuncCode)
        }
    }
}
