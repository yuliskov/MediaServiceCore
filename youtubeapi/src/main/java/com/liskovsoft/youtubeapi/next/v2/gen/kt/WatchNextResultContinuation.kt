package com.liskovsoft.youtubeapi.next.v2.gen.kt

import com.liskovsoft.youtubeapi.common.models.kt.ItemWrapper

data class WatchNextResultContinuation(
    val continuationContents: ContinuationContents?,
    val contents: Contents?
) {
    data class ContinuationContents(
        val horizontalListContinuation: HorizontalListContinuation?
    ) {
        data class HorizontalListContinuation(
            val items: List<ItemWrapper?>?,
            val continuations: List<ContinuationItem?>?
        )
    }

    data class Contents(
        val singleColumnWatchNextResults: SingleColumnWatchNextResults?
    ) {
        data class SingleColumnWatchNextResults(
            val results: Results?
        ) {
            data class Results(
                val results: Results?
            ) {
                data class Results(
                    val trackingParams: String?
                )
            }
        }
    }
}