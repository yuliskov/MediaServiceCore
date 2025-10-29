package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.next.v2.gen.ContinuationItem
import com.liskovsoft.youtubeapi.next.v2.gen.ShelfRenderer

internal data class SectionWrapper(
    val itemSectionRenderer: ShelfListWrapper?,
    val richItemRenderer: RichItemRenderer?,
    val richSectionRenderer: RichSectionRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?,
    val gridVideoRenderer: VideoItem?, // Topic channel e.g. 'tanki - topic'
    val playlistVideoRenderer: VideoItem?, // ChannelPlaylist
    val gridPlaylistRenderer: PlaylistItem?, // ChannelPlaylist continuation
    val gridRenderer: GridRenderer?,
    val musicResponsiveHeaderRenderer: RadioItem?
)

internal data class TabRenderer(
    val title: String?,
    val content: Content?,
    val endpoint: NavigationEndpointItem?,
    val thumbnail: ThumbnailItem?,
    val presentationStyle: PresentationStyle?
) {
    data class Content(
        val sectionListRenderer: SectionListRenderer?,
        val richGridRenderer: RichGridRenderer?,
        val tvSurfaceContentRenderer: TvSurfaceContentRenderer?
    )
    data class PresentationStyle(
        val style: String?
    )
}

internal data class SectionListRenderer(
    val contents: List<SectionWrapper?>?
)

internal data class RichGridRenderer(
    val contents: List<SectionWrapper?>?,
    val header: Header?
) {
    data class Header(
        val feedFilterChipBarRenderer: FeedFilterChipBarRenderer?
    )
}

internal data class FeedFilterChipBarRenderer(
    val contents: List<Content?>?
) {
    data class Content(
        val chipCloudChipRenderer: ChipCloudChipRenderer?
    )
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

internal data class TvSurfaceContentRenderer(
    val content: Content?,
    val continuation: ContinuationItem?
) {
    data class Content(
        val sectionListRenderer: ShelfListWrapper?,
        val gridRenderer: GridRenderer?, // TV
        val twoColumnRenderer: TwoColumnRenderer? // TV
    )
}

internal data class TwoColumnRenderer(
    val leftColumn: LeftColumn?,
    val rightColumn: RightColumn?
) {
    data class LeftColumn(
        val entityMetadataRenderer: EntityMetadataRenderer?
    ) {
        data class EntityMetadataRenderer(
            val title: TextItem?
        )
    }
    data class RightColumn(
        val playlistVideoListRenderer: PlaylistVideoListRenderer?
    )
}

internal data class TvSecondaryNavRenderer(
    val sections: List<Section?>?
) {
    data class Section(
        val tvSecondaryNavSectionRenderer: TvSecondaryNavSectionRenderer?
    ) {
        data class TvSecondaryNavSectionRenderer(
            val tabs: List<Tab>
        )
    }
}

// Subscriptions, Sports
internal data class ShelfListWrapper(
    val contents: List<Shelf?>?,
    val continuations: List<ContinuationItem?>?
)

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

// Corresponds to the row
internal data class Shelf(
    val shelfRenderer: ShelfRenderer?,
    val playlistVideoListRenderer: PlaylistVideoListRenderer?,
    val gridRenderer: GridRenderer?,
    val videoRenderer: VideoItem?
)

internal data class PlaylistVideoListRenderer(
    val contents: List<ItemWrapper?>?,
    val continuations: List<ContinuationItem?>?
)

internal data class GridRenderer(
    val items: List<ItemWrapper?>?,
    val continuations: List<ContinuationItem?>?
)