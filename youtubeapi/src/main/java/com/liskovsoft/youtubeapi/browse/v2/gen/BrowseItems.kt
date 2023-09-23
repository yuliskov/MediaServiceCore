package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.*

internal data class Section(
    val itemSectionRenderer: ItemSectionRenderer?,
    val richItemRenderer: RichItemRenderer?,
    val richSectionRenderer: RichSectionRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?,
    val playlistVideoRenderer: VideoItem? // ChannelPlaylist
)

internal data class TabRenderer(
    val title: String?,
    val content: Content?,
    val endpoint: NavigationEndpointItem?
) {
    data class Content(
        val sectionListRenderer: SectionListRenderer?,
        val richGridRenderer: RichGridRenderer?
    ) {
        data class SectionListRenderer(
            val contents: List<Section?>?
        )
        data class RichGridRenderer(
            val contents: List<Section?>?,
            val header: Header?
        ) {
            data class Header(
                val feedFilterChipBarRenderer: FeedFilterChipBarRenderer?
            ) {
                data class FeedFilterChipBarRenderer(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val chipCloudChipRenderer: ChipCloudChipRenderer?
                    )
                }
            }
        }
    }
}

// WhatToWatch only
internal data class RichSectionRenderer(
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
internal data class ItemSectionRenderer(
    val contents: List<Shelf?>?
) {
    data class Shelf(
        val shelfRenderer: ShelfRenderer?,
        val playlistVideoListRenderer: PlaylistVideoListRenderer?
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
        data class PlaylistVideoListRenderer(
            val contents: List<ItemWrapper?>?
        )
    }
}

// Common item (WhatToWatch, Subscriptions)
internal data class RichItemRenderer(
    val content: ItemWrapper?
)

// Common item (WhatToWatch, Subscriptions)
internal data class ContinuationItemRenderer(
    val continuationEndpoint: NavigationEndpoint?
)

internal data class ContinuationCommand(
    val token: String?
)

internal data class ChipCloudChipRenderer(
    val text: TextItem?,
    val navigationEndpoint: NavigationEndpoint? // possible duplicate?
)

internal data class NavigationEndpoint(
    val continuationCommand: ContinuationCommand?
)

/////

internal data class GuideItem(
    val thumbnail: ThumbnailItem?,
    val formattedTitle: TextItem?,
    val navigationEndpoint: NavigationEndpointItem?,
    val badges: Badges?,
    val presentationStyle: String?
) {
    data class Badges(
        val liveBroadcasting: Boolean?
    )
}

// Kids only
internal data class AnchoredSectionRenderer(
    val title: String?,
    val navigationEndpoint: NavigationEndpointItem?,
    val content: ContentItem?
) {
    data class ContentItem(
        val sectionListRenderer: SectionListRenderer?
    ) {
        data class SectionListRenderer(
            val contents: List<ContentItem?>?
        ) {
            data class ContentItem(
                val itemSectionRenderer: ItemSectionRenderer?
            ) {
                data class ItemSectionRenderer(
                    val contents: List<ItemWrapper?>?
                )
            }
        }
    }
}

// Reel only. Basic data. No title or description.
internal data class ReelWatchEndpoint(
    val videoId: String?,
    val playerParams: String?,
    val params: String?,
    val thumbnail: ThumbnailItem?
)

// Reel only. Extended data.
internal data class ReelPlayerHeaderRenderer(
    val reelTitleText: TextItem?,
    val channelTitleText: TextItem?,
    val timestampText: TextItem?,
    val channelNavigationEndpoint: NavigationEndpointItem?,
    val channelThumbnail: ThumbnailItem?,
    val reelTitleOnClickCommand: NavigationEndpointItem?
)