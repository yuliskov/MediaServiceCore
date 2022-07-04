package com.liskovsoft.youtubeapi.chat.helpers

import com.liskovsoft.youtubeapi.chat.gen.kt.LiveChatResult

fun LiveChatResult.getActions() = continuationContents?.liveChatContinuation?.actions
fun LiveChatResult.getContinuation() = continuationContents?.liveChatContinuation?.continuations?.getOrNull(0)
    ?.let { it.timedContinuationData ?: it.invalidationContinuationData }