package com.liskovsoft.youtubeapi.browse.v2.gen

data class ContinuationResult(
    val onResponseReceivedActions: List<OnResponseReceivedAction?>?
) {
    data class OnResponseReceivedAction(
        val appendContinuationItemsAction: AppendContinuationItemsAction?,
        val reloadContinuationItemsCommand: ReloadContinuationItemsCommand?
    ) {
        data class AppendContinuationItemsAction(
            val continuationItems: List<Section?>?
        )

        data class ReloadContinuationItemsCommand(
            val continuationItems: List<Section?>?
        )
    }
}
