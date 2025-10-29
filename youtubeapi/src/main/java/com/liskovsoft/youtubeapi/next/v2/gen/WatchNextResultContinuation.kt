package com.liskovsoft.youtubeapi.next.v2.gen

internal data class WatchNextResultContinuation(
    val continuationContents: ContinuationContents?,
    val contents: Contents?
) {
    data class ContinuationContents(
        val horizontalListContinuation: GridContinuationWrapper?,
        val gridContinuation: GridContinuationWrapper?, // TV?
        val playlistVideoListContinuation: GridContinuationWrapper?, // TV
        val tvSurfaceContentContinuation: TvSurfaceContentContinuation?, // TV
        val sectionListContinuation: SectionListContinuation?, // Home continuation TV
    )

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