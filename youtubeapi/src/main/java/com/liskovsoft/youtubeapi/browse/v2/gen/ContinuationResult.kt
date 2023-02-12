package com.liskovsoft.youtubeapi.browse.v2.gen

data class ContinuationResult(
    val onResponseReceivedActions: List<OnResponseReceivedAction?>?
) {
    data class OnResponseReceivedAction(
        val appendContinuationItemsAction: AppendContinuationItemsAction?
    ) {
        data class AppendContinuationItemsAction(
            val continuationItems: List<Section?>?
        )
    }
}
