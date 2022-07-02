package com.liskovsoft.youtubeapi.chat.gen.kt

import com.liskovsoft.youtubeapi.next.v2.gen.kt.ContinuationItem

data class LiveChatResult(
    val continuationContents: ContinuationContents?
) {
    data class ContinuationContents(
        val liveChatContinuation: LiveChatContinuation?
    ) {
        data class LiveChatContinuation(
            val continuations: List<ContinuationItem?>?,
            val actions: List<LiveChatAction?>?
        )
    }
}
