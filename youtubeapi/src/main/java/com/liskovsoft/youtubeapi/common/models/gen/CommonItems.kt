package com.liskovsoft.youtubeapi.common.models.gen

import com.liskovsoft.youtubeapi.browse.v2.gen.ContinuationItemRenderer
import com.liskovsoft.youtubeapi.browse.v2.gen.ReelWatchEndpoint
import com.liskovsoft.youtubeapi.next.v2.gen.ContinuationItem
import com.liskovsoft.youtubeapi.next.v2.gen.EngagementPanel
import com.liskovsoft.youtubeapi.next.v2.gen.VideoOwnerItem

internal data class NavigationEndpointItem(
    val browseEndpoint: BrowseEndpoint?,
    val watchEndpoint: WatchEndpointItem?,
    val reelWatchEndpoint: ReelWatchEndpoint?,
    val watchPlaylistEndpoint: WatchEndpointItem?,
    val openPopupAction: PopupActionItem?,
    val showEngagementPanelEndpoint: ShowEngagementPanelEndpoint?,
    val searchEndpoint: SearchEndpoint?
) {
    data class BrowseEndpoint(
        val browseId: String?,
        val params: String?
    )
    data class SearchEndpoint(
        val query: String?
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
                                val content: Content?,
                                val header: Header?
                            ) {
                                data class Content(
                                    val overlayPanelItemListRenderer: OverlayPanelItemListRenderer?,
                                    val itemSectionRenderer: ItemSectionRenderer?
                                ) {
                                    data class OverlayPanelItemListRenderer(
                                        val items: List<Item?>?
                                    ) {
                                        data class Item(
                                            val toggleButtonRenderer: ToggleButtonRenderer?,
                                            val subscribeButtonRenderer: SubscribeButtonRenderer?,
                                            val compactLinkRenderer: CompactLinkRenderer?
                                        )
                                    }

                                    data class ItemSectionRenderer(
                                        val continuations: List<ContinuationItem?>?
                                    )
                                }
                                data class Header(
                                    val overlayPanelHeaderRenderer: OverlayPanelHeaderRenderer?
                                ) {
                                    data class OverlayPanelHeaderRenderer(
                                        val title: TextItem?,
                                        val subtitle: TextItem?
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

internal data class WatchEndpointItem(
    val videoId: String?,
    val playlistId: String?,
    val index: Int,
    val params: String?,
    val startTimeSeconds: Int?
)

internal data class ChannelsEndpoint(
    val channelIds: List<String?>?,
    val params: String?
)

internal data class CommentEndpoint(
    val action: String?
)

internal data class DefaultServiceEndpoint(
    val authDeterminedCommand: AuthDeterminedCommand?
) {
    data class AuthDeterminedCommand(
        val authenticatedCommand: ToggledServiceEndpoint?
    )
}

internal data class ToggledServiceEndpoint(
    val subscribeEndpoint: ChannelsEndpoint?,
    val unsubscribeEndpoint: ChannelsEndpoint?,
    val performCommentActionEndpoint: CommentEndpoint?
)

internal data class ShowEngagementPanelEndpoint(
    val engagementPanel: EngagementPanel?
)

internal data class ButtonContentWrapper(
    val videoOwnerRenderer: VideoOwnerItem?,
    val toggleButtonRenderer: ToggleButtonRenderer?,
    val buttonRenderer: ButtonRenderer?,
    val musicPlayButtonRenderer: MusicPlayButtonRenderer?
)

internal data class ButtonRenderer(
    val isDisabled: Boolean?,
    val text: TextItem?,
    val icon: IconItem?,
    val navigationEndpoint: NavigationEndpointItem?
)

internal data class ToggleButtonRenderer(
    val isToggled: Boolean?,
    val isDisabled: Boolean?,
    val defaultServiceEndpoint: ToggledServiceEndpoint?,
    val toggledServiceEndpoint: ToggledServiceEndpoint?
)

internal data class SubscribeButtonRenderer(
    val subscribed: Boolean?,
    val channelId: String?,
    val subscriberCountText: TextItem?,
    val shortSubscriberCountText: TextItem?,
    val longSubscriberCountText: TextItem?,
    val serviceEndpoints: List<DefaultServiceEndpoint?>?,
    val notificationPreferenceButton: NotificationPreferenceButton?,
    val onSubscribeEndpoints: List<ToggledServiceEndpoint?>?
)

internal data class CompactLinkRenderer(
    val serviceEndpoint: ServiceEndpoint?
)

internal data class MusicPlayButtonRenderer(
    val playNavigationEndpoint: NavigationEndpointItem?
)

internal data class TextItem(
    val runs: List<Run?>?,
    val simpleText: String?,
    val content: String?,
    val accessibility: AccessibilityItem?
) {
    data class Run(
        val text: String?,
        val emoji: LiveChatEmoji?,
        val navigationEndpoint: NavigationEndpointItem?
    )

    override fun toString(): String {
        // Use empty string instead of super.toString() to fix output like "com.package.name.TextItem@0"
        return getText() ?: ""
    }
}

internal data class IconItem(
    val iconType: String?
)

internal data class LiveChatEmoji(
    val emojiId: String?,
    val image: ThumbnailItem?,
    val variantIds: List<String?>?,
    val shortcuts: List<String?>?,
    val isCustomEmoji: Boolean?
)

internal data class ThumbnailItem(
    val thumbnails: List<Thumbnail?>?,
    val sources: List<Thumbnail?>?
) {
    data class Thumbnail(
        val url: String?,
        val width: String?,
        val height: String?
    )
}

internal data class AccessibilityItem(
    val accessibilityData: AccessibilityData?
) {
    data class AccessibilityData(
        val label: String?
    )
}

internal data class ItemWrapper(
    val tileRenderer: TileItem? = null,
    val gridVideoRenderer: VideoItem? = null,
    val videoRenderer: VideoItem? = null,
    val pivotVideoRenderer: VideoItem? = null,
    val reelItemRenderer: VideoItem? = null,
    val compactVideoRenderer: VideoItem? = null,
    val tvMusicVideoRenderer: MusicItem? = null,
    val gridRadioRenderer: RadioItem? = null,
    val pivotRadioRenderer: RadioItem? = null,
    val compactRadioRenderer: RadioItem? = null,
    val gridChannelRenderer: ChannelItem? = null,
    val pivotChannelRenderer: ChannelItem? = null,
    val compactChannelRenderer: ChannelItem? = null,
    val gridPlaylistRenderer: PlaylistItem? = null,
    val pivotPlaylistRenderer: PlaylistItem? = null,
    val compactPlaylistRenderer: PlaylistItem? = null,
    val playlistRenderer: PlaylistItem? = null,
    val playlistVideoRenderer: VideoItem? = null, // ChannelPlaylist
    val musicTwoRowItemRenderer: RadioItem? = null, // YouTube Music
    val continuationItemRenderer: ContinuationItemRenderer? = null, // ChannelPlaylist
    val shortsLockupViewModel: ShortsItem? = null, // Shorts v2
    val lockupViewModel: LockupItem? = null, // Home video items v2
)

internal data class TileItem(
    val style: String?,
    val metadata: Metadata?,
    val header: Header?,
    val onSelectCommand: NavigationEndpointItem?,
    val menu: MenuWrapper?,
    val contentId: String?,
    val contentType: String?,
    val onLongPressCommand: OnLongPressCommand?
) {
    data class Metadata(
        val tileMetadataRenderer: TileMetadataRenderer?
    )

    data class Header(
        val tileHeaderRenderer: TileHeaderRenderer?,
        val richTextTileHeaderRenderer: RichTextTileHeaderRenderer?, // Video description (last row in the suggestions)
        val trackTileHeaderRenderer: TrackTileHeaderRenderer? // Special case: music albums
    ) {
        data class TileHeaderRenderer(
            val thumbnail: ThumbnailItem?,
            val thumbnailOverlays: List<ThumbnailOverlayItem?>?,
            val movingThumbnail: ThumbnailItem?, // v1
            val onFocusThumbnail: ThumbnailItem? // v2
        )

        /**
         * Video description (last row in the suggestions)
         */
        data class RichTextTileHeaderRenderer(
            val textContent: List<TextItem?>?
        )

        data class TrackTileHeaderRenderer(
            val title: TextItem?,
            val thumbnail: ThumbnailItem?,
            val duration: TextItem?
        )
    }

    data class OnLongPressCommand(
        val showMenuCommand: ShowMenuCommand?
    ) {
        data class ShowMenuCommand(
            val menu: MenuWrapper?
        )
    }
}

internal data class TileMetadataRenderer(
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

internal data class VideoItem(
    val thumbnail: ThumbnailItem?,
    val title: TextItem?,
    val headline: TextItem?,
    val shortBylineText: TextItem?,
    val longBylineText: TextItem?,
    val shortViewCountText: TextItem?,
    val viewCountText: TextItem?,
    val videoInfo: TextItem?,
    val publishedTimeText: TextItem?,
    val videoId: String?,
    val menu: MenuWrapper?,
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
            val label: String?,
            val style: String?
        )
    }

    data class UpcomingEvent(
        val upcomingEventText: TextItem?,
        val startTime: String?
    )
}

internal data class MusicItem(
    val thumbnail: ThumbnailItem?,
    val primaryText: TextItem?,
    val secondaryText: TextItem?,
    val tertiaryText: TextItem?,
    val navigationEndpoint: NavigationEndpointItem?,
    val lengthText: TextItem?,
    val menu: MenuWrapper?
)

internal data class RadioItem(
    val thumbnail: ThumbnailItem?,
    val thumbnailRenderer: ThumbnailRenderer?,
    val title: TextItem?,
    val subtitle: TextItem?,
    val navigationEndpoint: NavigationEndpointItem?,
    val menu: MenuWrapper?
)

internal data class ChannelItem(
    val thumbnail: ThumbnailItem?,
    val title: TextItem?,
    val displayName: TextItem?,
    val channelId: String?,
    val videoCountText: TextItem?,
    val subscriberCountText: TextItem?
)

// Fully replace with VideoItem?
internal data class PlaylistItem(
    val thumbnail: ThumbnailItem?,
    val thumbnails: List<ThumbnailItem?>?,
    val thumbnailRenderer: ThumbnailRenderer?,
    val title: TextItem?,
    val videoCountText: TextItem?,
    val videoCountShortText: TextItem?,
    val playlistId: String?
)

internal data class ShortsItem(
    val accessibilityText: String?,
    val thumbnail: ThumbnailItem?,
    val onTap: OnTap?,
    val overlayMetadata: OverlayMetadata?
) {
    data class OverlayMetadata(
        val primaryText: TextItem?,
        val secondaryText: TextItem?
    )
}

internal data class OnTap(
    val innertubeCommand: InnertubeCommand?
)

internal data class InnertubeCommand(
    val reelWatchEndpoint: ReelWatchEndpoint?,
    val watchEndpoint: WatchEndpointItem?,
    val showSheetCommand: ShowSheetCommand?,
    val feedbackEndpoint: FeedbackEndpoint?
) {
    data class ShowSheetCommand(
        val panelLoadingStrategy: PanelLoadingStrategy?
    ) {
        data class PanelLoadingStrategy(
            val inlineContent: InlineContent?
        ) {
            data class InlineContent(
                val sheetViewModel: SheetViewModel?
            ) {
                data class SheetViewModel(
                    val content: Content?
                ) {
                    data class Content(
                        val listViewModel: ListViewModel?
                    ) {
                        data class ListViewModel(
                            val listItems: List<ListItem?>?
                        ) {
                            data class ListItem(
                                val listItemViewModel: ListItemViewModel?
                            ) {
                                data class ListItemViewModel(
                                    val rendererContext: RendererContext?
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

internal data class LockupItem(
    val contentImage: ContentImage?, // thumbnail
    val metadata: MetadataItem?, // title, subtitle, channelId
    val rendererContext: RendererContext? // videoId
) {
    data class ContentImage(
        val thumbnailViewModel: ThumbnailViewModel?,
        val collectionThumbnailViewModel: CollectionThumbnailViewModel?
    ) {
        data class ThumbnailViewModel(
            val image: ThumbnailItem?,
            val overlays: List<Overlay?>?
        ) {
            data class Overlay(
                val thumbnailOverlayBadgeViewModel: ThumbnailOverlayBadgeViewModel?,
                val thumbnailBottomOverlayViewModel: ThumbnailBottomOverlayViewModel?
            ) {
                data class ThumbnailOverlayBadgeViewModel(
                    val thumbnailBadges: List<ThumbnailBadge?>?
                ) {
                    data class ThumbnailBadge(
                        val thumbnailBadgeViewModel: ThumbnailBadgeViewModel?
                    ) {
                        data class ThumbnailBadgeViewModel(
                            val text: String?,
                            val badgeStyle: String?
                        )
                    }
                }
                data class ThumbnailBottomOverlayViewModel(
                    val progressBar: ProgressBar?
                ) {
                    data class ProgressBar(
                        val thumbnailOverlayProgressBarViewModel: ThumbnailOverlayProgressBarViewModel?
                    ) {
                        data class ThumbnailOverlayProgressBarViewModel(
                            val startPercent: Int?
                        )
                    }
                }
            }
        }
        data class CollectionThumbnailViewModel(
            val primaryThumbnail: PrimaryThumbnail?
        ) {
            data class PrimaryThumbnail(
                val thumbnailViewModel: ThumbnailViewModel?
            )
        }
    }
    data class MetadataItem(
        val lockupMetadataViewModel: LockupMetadataViewModel?
    ) {
        data class LockupMetadataViewModel(
            val title: TextItem?, // title
            val metadata: NestedMetadataItem?, // subtitle, channelId
            val menuButton: MenuButton?
        ) {
            data class NestedMetadataItem(
                val contentMetadataViewModel: ContentMetadataViewModel?
            ) {
                data class ContentMetadataViewModel(
                    val metadataRows: List<MetadataRow?>?
                ) {
                    data class MetadataRow(
                        val metadataParts: List<MetadataPart?>?
                    ) {
                        data class MetadataPart(
                            val text: TextItem?
                        )
                    }
                }
            }
            data class MenuButton(
                val buttonViewModel: ButtonViewModel?
            ) {
                data class ButtonViewModel(
                    val onTap: OnTap?
                )
            }
        }
    }
}

internal data class RendererContext(
    val commandContext: CommandContext?
) {
    data class CommandContext(
        val onTap: OnTap?
    )
}

internal data class ThumbnailRenderer(
    val playlistVideoThumbnailRenderer: ThumbnailRenderer?,
    val playlistCustomThumbnailRenderer: ThumbnailRenderer?,
    val musicThumbnailRenderer: ThumbnailRenderer?
) {
    data class ThumbnailRenderer(
        val thumbnail: ThumbnailItem?
    )
}

internal data class MenuWrapper(
    val menuRenderer: MenuRenderer?
) {
    data class MenuRenderer(
        val items: List<MenuItem?>?
    )
}

internal data class MenuItem(
    val menuServiceItemRenderer: MenuServiceItemRenderer?,
    val menuNavigationItemRenderer: MenuNavigationItemRenderer?
) {
    data class MenuServiceItemRenderer(
        val text: TextItem?,
        val icon: IconItem?,
        val serviceEndpoint: ServiceEndpoint?,
        val command: NavigationEndpointItem?
    )
    data class MenuNavigationItemRenderer(
        val navigationEndpoint: NavigationEndpointItem?
    )
}

internal data class ServiceEndpoint(
    val feedbackEndpoint: FeedbackEndpoint?,
    val recordNotificationInteractionsEndpoint: RecordNotificationInteractionsEndpoint?,
    val commandExecutorCommand: CommandExecutorCommand?
) {
    data class RecordNotificationInteractionsEndpoint(
        val serializedInteractionsRequest: String?
    )
    data class CommandExecutorCommand(
        // WARN: don't merge ExecutorCommand with InnertubeCommand: Android 4 StackOverflowError
        // The node InnertubeCommand recursively reference itself
        val commands: List<ExecutorCommand>?
    )
}

internal data class ExecutorCommand(
    val feedbackEndpoint: FeedbackEndpoint?
)

internal data class FeedbackEndpoint(
    val feedbackToken: String?
)

internal data class RichThumbnailItem(
    val movingThumbnailRenderer: MovingThumbnailRenderer?
) {
    data class MovingThumbnailRenderer(
        val movingThumbnailDetails: ThumbnailItem?
    )
}

internal data class ThumbnailOverlayItem(
    val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?,
    val thumbnailOverlayResumePlaybackRenderer: ThumbnailOverlayResumePlaybackRenderer?,
    val tileMetadataRenderer: TileMetadataRenderer?,
) {
    data class ThumbnailOverlayTimeStatusRenderer(
        val text: TextItem?,
        val style: String?
    )

    data class ThumbnailOverlayResumePlaybackRenderer(
        val percentDurationWatched: Int?
    )
}

internal data class NotificationPreferenceButton(
    val subscriptionNotificationToggleButtonRenderer: SubscriptionNotificationToggleButtonRenderer?
) {
    data class SubscriptionNotificationToggleButtonRenderer(
        val states: List<NotificationStateItem?>?,
        val currentStateId: Int?
    )
}

internal data class NotificationStateItem(
    val stateId: Int?,
    val nextStateId: Int?,
    val inlineMenuButton: InlineMenuButton?
) {
    data class InlineMenuButton(
        val buttonRenderer: NotificationButtonRenderer?
    ) {
        data class NotificationButtonRenderer(
            val text: TextItem?,
            val serviceEndpoint: NotificationServiceEndpoint?
        ) {
            data class NotificationServiceEndpoint(
                val modifyChannelNotificationPreferenceEndpoint: ModifyChannelNotificationPreferenceEndpoint?
            ) {
                data class ModifyChannelNotificationPreferenceEndpoint(
                    val params: String?
                )
            }
        }
    }
}

internal data class ResponseContext(
    val serviceTrackingParams: List<TrackingParam?>?
)

internal data class TrackingParam(
    val service: String?,
    val params: List<Param?>?
) {
    data class Param(
        val key: String?,
        val value: String?
    )
}