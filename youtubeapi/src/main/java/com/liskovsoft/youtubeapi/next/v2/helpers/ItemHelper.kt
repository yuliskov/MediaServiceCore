package com.liskovsoft.youtubeapi.next.v2.helpers

import com.liskovsoft.youtubeapi.next.v2.result.gen.TextItem

object ItemHelper {
    fun toString(textItem: TextItem?): String? {
        return textItem?.simpleText
    }
}