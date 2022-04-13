package com.liskovsoft.youtubeapi.next.v2.gen.kt

import com.liskovsoft.youtubeapi.next.v2.helpers.getText

data class NavigationEndpointItem(
        val browseEndpoint: BrowseEndpoint?,
        val watchEndpoint: WatchEndpointItem?,
        val watchPlaylistEndpoint: WatchEndpointItem?
) {
    data class BrowseEndpoint(
            val browseId: String?
    )
}

data class WatchEndpointItem(
        val videoId: String?,
        val playlistId: String?,
        val index: Int,
        val params: String?,
)

data class TextItem(
        val runs: List<Run?>?,
        val simpleText: String?
) {
    data class Run(
            val text: String?,
            val navigationEndpoint: NavigationEndpointItem?
    )

    override fun toString(): String {
        return getText() ?: super.toString()
    }
}

data class ThumbnailItem(
        val thumbnails: List<Thumbnail?>?
) {
    data class Thumbnail(
            val url: String?,
            val width: String?,
            val height: String?
    )
}

data class NextVideoItem(
        val item: Item?,
        val endpoint: Endpoint?
) {
    data class Item(val previewButtonRenderer: PreviewButtonRenderer?) {
        data class PreviewButtonRenderer(val thumbnail: ThumbnailItem?, val title: TextItem?, val byline: TextItem?)
    }

    data class Endpoint(val watchEndpoint: WatchEndpointItem?)
}

data class ShelfItem(
        val title: TextItem?,
        val content: Content?,
        val headerRenderer: HeaderRenderer?
) {
    data class Content(
            val horizontalListRenderer: HorizontalListRenderer?
    ) {
        data class HorizontalListRenderer(
                val items: List<ItemWrapper?>?,
                val continuations: List<ContinuationItem?>?
        )
    }

    data class HeaderRenderer(
            val shelfHeaderRenderer: ShelfHeaderRenderer?,
            val chipCloudRenderer: ChipCloudRenderer?
    ) {
        data class ShelfHeaderRenderer(
                val title: TextItem?
        )

        data class ChipCloudRenderer(
                val chips: List<ChipItem?>?
        )
    }
}

data class ChipItem(
        val chipCloudChipRenderer: ChipCloudChipRenderer?
) {
    data class ChipCloudChipRenderer(
            val text: TextItem?,
            val content: Content?
    ) {
        data class Content(
                val horizontalListRenderer: HorizontalListRenderer?,
                val sectionListRenderer: SectionListRenderer?
        ) {
            data class HorizontalListRenderer(
                    val items: List<ItemWrapper?>?,
                    val continuations: List<ContinuationItem?>?
            )

            data class SectionListRenderer(
                    val contents: List<Content?>?
            ) {
                data class Content(
                        val shelfRenderer: ShelfItem?
                )
            }
        }
    }
}

data class ContinuationItem(
        val reloadContinuationData: ReloadContinuationData?,
        val nextContinuationData: NextContinuationData?
) {
    data class ReloadContinuationData(
            val continuation: String?
    )

    data class NextContinuationData(
            val continuation: String?
    )
}

data class RichThumbnailItem(
        val movingThumbnailRenderer: MovingThumbnailRenderer?
) {
    data class MovingThumbnailRenderer(
            val movingThumbnailDetails: ThumbnailItem?
    )
}

data class ItemWrapper(
        val tileRenderer: TileItem?,
        val gridVideoRenderer: VideoItem?,
        val pivotVideoRenderer: VideoItem?,
        val compactVideoRenderer: VideoItem?,
        val tvMusicVideoRenderer: MusicItem?,
        val gridRadioRenderer: RadioItem?,
        val pivotRadioRenderer: RadioItem?,
        val compactRadioRenderer: RadioItem?,
        val gridChannelRenderer: ChannelItem?,
        val pivotChannelRenderer: ChannelItem?,
        val compactChannelRenderer: ChannelItem?,
        val gridPlaylistRenderer: PlaylistItem?,
        val pivotPlaylistRenderer: PlaylistItem?,
        val compactPlaylistRenderer: PlaylistItem?
)

data class TileItem(
        val metadata: Metadata?,
        val header: Header?,
        val onSelectCommand: NavigationEndpointItem?,
        val menu: MenuItem?,
        val contentType: String?
) {
    data class Metadata(
            val tileMetadataRenderer: TileMetadataRenderer?
    ) {
        data class TileMetadataRenderer(
                val title: TextItem?,
                val lines: List<Line?>?
        ) {
            data class Line(
                    val lineRenderer: LineRenderer?
            ) {
                data class LineRenderer(
                        val items: List<Item?>?
                ) {
                    data class Item(
                            val lineItemRenderer: LineItemRenderer?
                    ) {
                        data class LineItemRenderer(
                                val text: TextItem?,
                                val badge: Badge?
                        ) {
                            data class Badge(
                                    val metadataBadgeRenderer: MetadataBadgeRenderer?
                            ) {
                                data class MetadataBadgeRenderer(
                                        val style: String?,
                                        val label: String?
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class Header(
            val tileHeaderRenderer: TileHeaderRenderer?
    ) {
        data class TileHeaderRenderer(
                val thumbnail: ThumbnailItem,
                val thumbnailOverlays: List<ThumbnailOverlayItem?>?,
                val movingThumbnail: ThumbnailItem?
        )
    }
}

data class MenuItem(
        val menuRenderer: MenuRenderer?
) {
    data class MenuRenderer(
            val items: List<Item?>?
    ) {
        data class Item(
                val menuServiceItemRenderer: MenuServiceItemRenderer?,
                val menuNavigationItemRenderer: MenuNavigationItemRenderer?
        ) {
            data class MenuServiceItemRenderer(
                    val serviceEndpoint: ServiceEndpoint?
            ) {
                data class ServiceEndpoint(
                        val feedbackEndpoint: FeedbackEndpoint?
                ) {
                    data class FeedbackEndpoint(
                            val feedbackToken: String?
                    )
                }
            }

            data class MenuNavigationItemRenderer(
                    val navigationEndpoint: NavigationEndpointItem?
            )
        }
    }
}

data class VideoItem(
        val thumbnail: ThumbnailItem?,
        val title: TextItem?,
        val shortBylineText: TextItem?,
        val longBylineText: TextItem?,
        val shortViewCountText: TextItem?,
        val viewCountText: TextItem?,
        val publishedTimeText: TextItem?,
        val videoId: String?,
        val menu: MenuItem?,
        val badges: List<BadgeItem?>?,
        val upcomingEventData: UpcomingEvent?,
        val richThumbnail: RichThumbnailItem?,
        val thumbnailOverlays: List<ThumbnailOverlayItem?>?,
        val navigationEndpoint: NavigationEndpointItem?,
        val lengthText: TextItem?
) {
    data class BadgeItem(
            val liveBadge: LiveBadge?,
            val upcomingEventBadge: UpcomingEventBadge?,
            val metadataBadgeRenderer: MetadataBadgeRenderer?
    ) {
        data class LiveBadge(
                val label: TextItem?
        )

        data class UpcomingEventBadge(
                val label: TextItem?
        )

        data class MetadataBadgeRenderer(
                val label: String?
        )
    }

    data class UpcomingEvent(
            val upcomingEventText: TextItem?,
            val startTime: String?
    )
}

data class MusicItem(
        val thumbnail: ThumbnailItem?,
        val primaryText: TextItem?,
        val secondaryText: TextItem?,
        val tertiaryText: TextItem?,
        val navigationEndpoint: NavigationEndpoint?,
        val lengthText: TextItem?,
        val menu: MenuItem?
) {
    data class NavigationEndpoint(
            val watchEndpoint: WatchEndpointItem?
    )
}

data class RadioItem(
        val thumbnail: ThumbnailItem?,
        val thumbnailRenderer: ThumbnailRenderer?,
        val title: TextItem?
) {
    data class ThumbnailRenderer(
            val playlistVideoThumbnailRenderer: PlaylistVideoThumbnailRenderer?
    ) {
        data class PlaylistVideoThumbnailRenderer(
                val thumbnail: Thumbnail?
        ) {
            data class Thumbnail(
                    val thumbnails: List<Thumbnail?>?
            ) {
                data class Thumbnail(
                        val url: String?,
                        val width: String?,
                        val height: String?
                )
            }
        }
    }

}

data class ChannelItem(
        val thumbnail: ThumbnailItem?,
        val title: TextItem?,
        val displayName: TextItem?,
        val channelId: String?,
        val videoCountText: TextItem?,
        val subscriberCountText: TextItem?
)

data class PlaylistItem(
        val thumbnail: ThumbnailItem?,
        val thumbnailRenderer: ThumbnailRenderer?,
        val title: TextItem?
) {
    data class ThumbnailRenderer(
            val playlistVideoThumbnailRenderer: PlaylistVideoThumbnailRenderer?,
            val playlistCustomThumbnailRenderer: PlaylistCustomThumbnailRenderer?
    ) {
        data class PlaylistVideoThumbnailRenderer(
                val thumbnail: ThumbnailItem?
        )

        data class PlaylistCustomThumbnailRenderer(
                val thumbnail: ThumbnailItem?
        )
    }

}

data class VideoOwnerItem(
        val thumbnail: ThumbnailItem?,
        val title: TextItem?,
        val subscribed: Boolean?,
        val subscriptionButton: SubscriptionButton?,
        val subscribeButton: SubscribeButton?,
        val navigationEndpoint: NavigationEndpointItem?
) {
    data class SubscriptionButton(
            val subscribed: Boolean?
    )

    data class SubscribeButton(
            val subscribeButtonRenderer: SubscribeButtonRenderer?
    ) {
        data class SubscribeButtonRenderer(
                val subscribed: Boolean?,
                val channelId: String?
        )
    }
}

data class VideoMetadataItem(
        val owner: Owner?,
        val title: TextItem?,
        val byline: TextItem?,
        val albumName: TextItem?,
        val videoId: String?,
        val description: TextItem?,
        val publishedTimeText: TextItem?,
        val dateText: TextItem?,
        val viewCountText: TextItem?,
        val shortViewCountText: TextItem?,
        val viewCount: ViewCount?,
        val likeStatus: String?,
        val likeButton: LikeButton?,
        val badges: List<Badge?>?,
        val thumbnailOverlays: List<ThumbnailOverlayItem?>?
) {
    data class Owner(
            val videoOwnerRenderer: VideoOwnerItem?
    )

    data class ViewCount(
            val videoViewCountRenderer: VideoViewCountRenderer?
    ) {
        data class VideoViewCountRenderer(
                val viewCount: TextItem?,
                val shortViewCount: TextItem?,
                val isLive: Boolean?
        )
    }

    data class LikeButton(
            val likeButtonRenderer: LikeButtonRenderer?
    ) {
        data class LikeButtonRenderer(
                val likeStatus: String?
        )
    }

    data class Badge(
            val upcomingEventBadge: UpcomingEventBadge?
    ) {
        data class UpcomingEventBadge(
                val label: TextItem?
        )
    }
}

data class ButtonStateItem(
        val subscribeButton: SubscribeButton?,
        val likeButton: LikeButton?,
        val dislikeButton: DislikeButton?,
        val channelButton: ChannelButton?
) {
    data class SubscribeButton(
            val toggleButtonRenderer: ToggleButtonRenderer?
    ) {
        data class ToggleButtonRenderer(
                val isToggled: Boolean?
        )
    }

    data class LikeButton(
            val toggleButtonRenderer: ToggleButtonRenderer?
    ) {
        data class ToggleButtonRenderer(
                val isToggled: Boolean?
        )
    }

    data class DislikeButton(
            val toggleButtonRenderer: ToggleButtonRenderer?
    ) {
        data class ToggleButtonRenderer(
                val isToggled: Boolean?
        )
    }

    data class ChannelButton(
            val videoOwnerRenderer: VideoOwnerItem?
    )
}

data class ThumbnailOverlayItem(
        val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?,
        val thumbnailOverlayResumePlaybackRenderer: ThumbnailOverlayResumePlaybackRenderer?
) {
    data class ThumbnailOverlayTimeStatusRenderer(
            val text: TextItem?,
            val style: String?
    )

    data class ThumbnailOverlayResumePlaybackRenderer(
            val percentDurationWatched: Int?
    )
}