package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.*

internal data class NextVideoItem(
    val item: Item?,
    val endpoint: Endpoint?
) {
    data class Item(val previewButtonRenderer: PreviewButtonRenderer?) {
        data class PreviewButtonRenderer(val thumbnail: ThumbnailItem?, val title: TextItem?, val byline: TextItem?)
    }

    data class Endpoint(val watchEndpoint: WatchEndpointItem?)
}

internal data class ShelfItem(
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
                val title: TextItem?,
                val avatarLockup: AvatarLockup?
        ) {
            data class AvatarLockup(
                val avatarLockupRenderer: AvatarLockupRenderer?
            ) {
                data class AvatarLockupRenderer(
                    val title: TextItem?
                )
            }
        }

        data class ChipCloudRenderer(
                val chips: List<ChipItem?>?
        )
    }
}

internal data class ChipItem(
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

internal data class ContinuationItem(
    val reloadContinuationData: ReloadContinuationData?,
    val nextContinuationData: NextContinuationData?,
    val invalidationContinuationData: LiveChatContinuationData?, // live chats
    val timedContinuationData: LiveChatContinuationData? // live chats
) {
    data class ReloadContinuationData(
            val continuation: String?
    )

    data class NextContinuationData(
            val continuation: String?,
            val label: TextItem?
    )

    data class LiveChatContinuationData(
            val timeoutMs: Int?,
            val continuation: String?
    )
}

internal data class VideoOwnerItem(
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
    )
}

internal data class VideoMetadataItem(
    val owner: Owner?,
    val title: TextItem?,
    val byline: TextItem?,
    val albumName: TextItem?,
    val videoId: String?,
    val description: TextItem?,
    val publishedTimeText: TextItem?,
    val publishedTime: TextItem?,
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
                val likeStatus: String?,
                val likeCountText: TextItem?
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

internal data class ButtonStateItem(
    val subscribeButton: SubscribeButton?,
    val likeButton: LikeButton?,
    val dislikeButton: DislikeButton?,
    val channelButton: ChannelButton?,
    val buttons: List<GenericButton?>?
) {
    data class SubscribeButton(
            val toggleButtonRenderer: ToggleButtonRenderer?
    )

    data class LikeButton(
            val toggleButtonRenderer: ToggleButtonRenderer?
    )

    data class DislikeButton(
            val toggleButtonRenderer: ToggleButtonRenderer?
    )

    data class ChannelButton(
            val videoOwnerRenderer: VideoOwnerItem?
    )

    data class GenericButton(
            val type: String?,
            val button: ButtonContent?
    ) {
        data class ButtonContent(
            val videoOwnerRenderer: VideoOwnerItem?,
            val toggleButtonRenderer: ToggleButtonRenderer?,
            val buttonRenderer: ButtonRenderer?
        )
    }
}

internal data class PlaylistInfo(
    val title: String?,
    val currentIndex: Int?,
    val playlistId: String?,
    val totalVideos: Int?,
    val ownerName: TextItem?,
    val isEditable: Boolean?
)

internal data class ChapterItem(
   val chapterRenderer: ChapterRenderer?
) {
    data class ChapterRenderer(
        val title: TextItem?,
        val timeRangeStartMillis: Long?,
        val thumbnail: ThumbnailItem?
    )
}

//////////

internal data class EngagementPanel(
    val engagementPanelSectionListRenderer: EngagementPanelSectionListRenderer?
) {
    data class EngagementPanelSectionListRenderer(
        val panelIdentifier: String?,
        val header: Header?,
        val content: Content?
    ) {
        data class Header(
            val engagementPanelTitleHeaderRenderer: EngagementPanelTitleHeaderRenderer?
        ) {
            data class EngagementPanelTitleHeaderRenderer(
                val menu: Menu?
            )
        }
        data class Content(
            val structuredDescriptionContentRenderer: StructuredDescriptionContentRenderer?
        ) {
            data class StructuredDescriptionContentRenderer(
                 val items: List<Item?>?
            ) {
                data class Item(
                    val videoDescriptionHeaderRenderer: VideoDescriptionHeaderRenderer?
                )
            }
        }
    }
}

internal data class VideoDescriptionHeaderRenderer(
    val title: TextItem?,
    val channel: TextItem?,
    val views: TextItem?,
    val publishDate: TextItem,
    val channelNavigationEndpoint: NavigationEndpointItem?
)

internal data class Menu(
    val sortFilterSubMenuRenderer: SortFilterSubMenuRenderer?
) {
    data class SortFilterSubMenuRenderer(
        val subMenuItems: List<SubMenuItem>
    )
}

internal data class SubMenuItem(
    val continuation: ContinuationItem?
)

//////////