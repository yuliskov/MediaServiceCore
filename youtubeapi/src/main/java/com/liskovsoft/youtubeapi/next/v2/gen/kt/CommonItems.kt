package com.liskovsoft.youtubeapi.next.v2.gen.kt

import com.liskovsoft.youtubeapi.common.models.kt.*

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
        val nextContinuationData: NextContinuationData?,
        val invalidationContinuationData: LiveChatContinuationData?, // live chats
        val timedContinuationData: LiveChatContinuationData? // live chats
) {
    data class ReloadContinuationData(
            val continuation: String?
    )

    data class NextContinuationData(
            val continuation: String?
    )

    data class LiveChatContinuationData(
            val timeoutMs: Int?,
            val continuation: String?
    )
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
}

data class PlaylistInfo(
    val title: String?,
    val currentIndex: Int?,
    val playlistId: String?,
    val totalVideos: Int?,
    val ownerName: TextItem?,
    val isEditable: Boolean?
)