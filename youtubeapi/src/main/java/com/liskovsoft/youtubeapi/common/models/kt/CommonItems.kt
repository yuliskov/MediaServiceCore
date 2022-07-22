package com.liskovsoft.youtubeapi.common.models.kt

data class NavigationEndpointItem(
    val browseEndpoint: BrowseEndpoint?,
    val watchEndpoint: WatchEndpointItem?,
    val watchPlaylistEndpoint: WatchEndpointItem?,
    val openPopupAction: PopupActionItem?
) {
    data class BrowseEndpoint(
        val browseId: String?
    )
    data class PopupActionItem(
        val popup: Popup?
    ) {
        data class Popup(
            val overlaySectionRenderer: OverlaySectionRenderer?
        ) {
            data class OverlaySectionRenderer(
                val overlay: Overlay?
            ) {
                data class Overlay(
                    val overlayTwoPanelRenderer: OverlayTwoPanelRenderer?
                ) {
                    data class OverlayTwoPanelRenderer(
                        val actionPanel: ActionPanel?
                    ) {
                        data class ActionPanel(
                            val overlayPanelRenderer: OverlayPanelRenderer?
                        ) {
                            data class OverlayPanelRenderer(
                                val content: Content?
                            ) {
                                data class Content(
                                    val overlayPanelItemListRenderer: OverlayPanelItemListRenderer?
                                ) {
                                    data class OverlayPanelItemListRenderer(
                                        val items: List<Item?>?
                                    ) {
                                        data class Item(
                                            val toggleButtonRenderer: ToggleButtonRenderer?
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class WatchEndpointItem(
    val videoId: String?,
    val playlistId: String?,
    val index: Int,
    val params: String?,
)

data class ToggleButtonRenderer(
    val isToggled: Boolean?
)

data class TextItem(
    val runs: List<Run?>?,
    val simpleText: String?
) {
    data class Run(
        val text: String?,
        val emoji: LiveChatEmoji?,
        val navigationEndpoint: NavigationEndpointItem?
    )

    override fun toString(): String {
        return getText() ?: super.toString()
    }
}

data class LiveChatEmoji(
    val emojiId: String?,
    val image: ThumbnailItem?,
    val variantIds: List<String?>?,
    val shortcuts: List<String?>?,
    val isCustomEmoji: Boolean?
)

data class ThumbnailItem(
    val thumbnails: List<Thumbnail?>?
) {
    data class Thumbnail(
        val url: String?,
        val width: String?,
        val height: String?
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
        val tileHeaderRenderer: TileHeaderRenderer?,
        val richTextTileHeaderRenderer: RichTextTileHeaderRenderer? // Video description (last row in the suggestions)
    ) {
        data class TileHeaderRenderer(
            val thumbnail: ThumbnailItem,
            val thumbnailOverlays: List<ThumbnailOverlayItem?>?,
            val movingThumbnail: ThumbnailItem?
        )

        /**
         * Video description (last row in the suggestions)
         */
        data class RichTextTileHeaderRenderer(
            val textContent: List<TextItem>
        )
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

data class RichThumbnailItem(
    val movingThumbnailRenderer: MovingThumbnailRenderer?
) {
    data class MovingThumbnailRenderer(
        val movingThumbnailDetails: ThumbnailItem?
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