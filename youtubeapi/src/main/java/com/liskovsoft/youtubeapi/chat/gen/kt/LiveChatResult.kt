package com.liskovsoft.youtubeapi.chat.gen.kt

import com.liskovsoft.youtubeapi.next.v2.gen.kt.ContinuationItem

data class LiveChatResult(val continuationContents: ContinuationContents?) {
    data class ContinuationContents(
            val liveChatContinuation: LiveChatContinuation?,
            val actions: List<LiveChatAction?>?
        ) {
        data class LiveChatContinuation(val continuations: List<ContinuationItem?>?)
    }
}
