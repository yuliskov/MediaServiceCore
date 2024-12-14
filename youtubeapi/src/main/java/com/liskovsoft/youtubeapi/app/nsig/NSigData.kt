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

            return NSigData(Helpers.parseStr(split, 0), Helpers.parseStrList(split, 1), Helpers.parseStr(split, 2))
        }
    }
}

