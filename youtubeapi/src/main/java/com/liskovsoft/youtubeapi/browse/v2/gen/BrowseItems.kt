package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

data class Section(
    val itemSectionRenderer: ItemSectionRenderer?,
    val richItemRenderer: RichItemRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?
) {
    data class ItemSectionRenderer(
        val contents: List<Shelf?>?
    ) {
        data class Shelf(
            val shelfRenderer: ShelfRenderer?
        ) {
            data class ShelfRenderer(
                val content: Content?
            ) {
                data class Content(
                    val gridRenderer: GridRenderer?,
                    val expandedShelfContentsRenderer: ExpandedShelfContentsRenderer?
                ) {
                    data class GridRenderer(
                        val items: List<ItemWrapper?>?
                    )

                    data class ExpandedShelfContentsRenderer(
                        val items: List<ItemWrapper?>?
                    )
                }
            }
        }
    }
    data class RichItemRenderer(
        val content: ItemWrapper?
    )
}

data class ContinuationItemRenderer(
    val continuationEndpoint: ContinuationEndpoint?
) {
    data class ContinuationEndpoint(
        val continuationCommand: ContinuationCommand?
    ) {
        data class ContinuationCommand(
            val token: String?
        )
    }
}