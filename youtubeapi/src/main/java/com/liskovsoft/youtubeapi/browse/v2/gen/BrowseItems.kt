package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.TextItem

data class Section(
    val itemSectionRenderer: ItemSectionRenderer?,
    val richItemRenderer: RichItemRenderer?,
    val richSectionRenderer: RichSectionRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?
)

// WhatToWatch only
data class RichSectionRenderer(
    val content: Content?
) {
    data class Content(
        val richShelfRenderer: RichShelfRenderer?
    ) {
        data class RichShelfRenderer(
            val title: TextItem?,
            val contents: List<Content?>?
        ) {
            data class Content(
                val richItemRenderer: RichItemRenderer?,
                val continuationItemRenderer: ContinuationItemRenderer?
            )
        }
    }
}

// Subscriptions only
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

// Common item (WhatToWatch, Subscriptions)
data class RichItemRenderer(
    val content: ItemWrapper?
)

// Common item (WhatToWatch, Subscriptions)
data class ContinuationItemRenderer(
    val continuationEndpoint: ContinuationEndpoint?
) {
    data class ContinuationEndpoint(
        val continuationCommand: ContinuationCommand?
    )
}

data class ContinuationCommand(
    val token: String?
)

data class ChipCloudChipRenderer(
    val text: TextItem?,
    val navigationEndpoint: NavigationEndpoint? // possible duplicate?
)

data class NavigationEndpoint(
    val continuationCommand: ContinuationCommand?
)