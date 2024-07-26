package com.liskovsoft.youtubeapi.common.api

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp

internal class FileContent {
    @RegExp("[\\w\\W]*")
    private val mContent: String? = null

    val content: String?
        get() = mContent
}
