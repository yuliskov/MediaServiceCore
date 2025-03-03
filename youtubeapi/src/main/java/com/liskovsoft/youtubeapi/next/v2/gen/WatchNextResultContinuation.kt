package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.browse.v2.gen.Shelf
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

internal data class WatchNextResultContinuation(
    val continuationContents: ContinuationContents?,
    val contents: Contents?
) {
    data class ContinuationContents(
        val horizontalListContinuation: Continuation?,
        val gridContinuation: Continuation?, // TV?
        val playlistVideoListContinuation: Continuation?, // TV
        val tvSurfaceContentContinuation: TvSurfaceContentContinuation?, // TV
        val sectionListContinuation: SectionListContinuation?, // Home continuation TV
    ) {
        data class Continuation(
            val items: List<ItemWrapper?>?,
            val contents: List<ItemWrapper?>?, // TV
            val continuations: List<ContinuationItem?>?
        )
        data class TvSurfaceContentContinuation(
            val content: Content?
        ) {
            data class Content(
                val gridRenderer: Continuation?,
                val sectionListRenderer: SectionListContinuation?
            )
        }
        data class SectionListContinuation(
            val contents: List<Shelf?>?,
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