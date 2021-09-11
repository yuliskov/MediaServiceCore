package com.liskovsoft.youtubeapi.next.v2.result

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

// "$.contents.singleColumnWatchNextResults.pivot.pivot.contents[*].shelfRenderer"
// "$.title.runs[0].text", "$.title.simpleText"

data class WatchNextResult(val contents: Contents?) {
    data class Contents(val singleColumnWatchNextResults: SingleColumnWatchNextResults?) {
        data class SingleColumnWatchNextResults(val pivot: Pivot?) {
            data class Pivot(val pivot: Pivot?, val contents: List<Content>?) {
                data class Content(val shelfRenderer: ShelfRenderer?) {
                    data class ShelfRenderer(val title: Title?) {
                        data class Title(val runs: List<TextItem?>?, val simpleText: String?) {
                            data class TextItem(val text: String?)

                            fun getText() = if (runs != null) ServiceHelper.combineText(runs) else simpleText
                        }
                    }
                }
            }
        }
    }
}
