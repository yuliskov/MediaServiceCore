package com.liskovsoft.youtubeapi.next.v2.result.gen

data class TextItem(
        val runs: List<Run?>?,
        val simpleText: String?
) {
    data class Run(
            val text: String?
    )
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

class NextVideoItem

data class ShelfItem(
        val title: TextItem?,
        val content: Content?
) {


    data class Content(
            val horizontalListRenderer: HorizontalListRenderer?
    ) {
        data class HorizontalListRenderer(
                val items: List<ItemWrapper?>?
        )
    }
}

data class ItemWrapper(
        val tileRenderer: TileItem?,
        val gridVideoRenderer: VideoItem?,
        val pivotVideoRenderer: VideoItem?,
        val tvMusicVideoRenderer: MusicItem?,
        val gridRadioRenderer: RadioItem?,
        val pivotRadioRenderer: RadioItem?,
        val gridChannelRenderer: ChannelItem?,
        val pivotChannelRenderer: ChannelItem?,
        val gridPlaylistRenderer: PlaylistItem?,
        val pivotPlaylistRenderer: PlaylistItem?
)

data class TileItem(
        val metadata: Metadata?,
        val onSelectCommand: OnSelectCommand?
) {
    data class Metadata(
            val tileMetadataRenderer: TileMetadataRenderer?
    ) {
        data class TileMetadataRenderer(
                val title: TextItem?
        )
    }

    data class OnSelectCommand(
            val watchEndpoint: WatchEndpoint?,
            val watchPlaylistEndpoint: WatchPlaylistEndpoint?
    ) {
        data class WatchEndpoint(
                val videoId: String?,
                val playlistId: String?
        )

        data class WatchPlaylistEndpoint(
                val playlistId: String?
        )
    }
}

data class VideoItem(
        val thumbnail: ThumbnailItem?,
        val title: TextItem?,
        val shortBylineText: TextItem?,
        val longBylineText: TextItem?,
        val videoId: String?
)

data class MusicItem(
        val thumbnail: ThumbnailItem?,
        val primaryText: TextItem?,
        val secondaryText: TextItem?,
        val tertiaryText: TextItem?,
        val navigationEndpoint: NavigationEndpoint?
) {
    data class NavigationEndpoint(
            val watchEndpoint: WatchEndpoint?
    ) {
        data class WatchEndpoint(
                val videoId: String?,
                val playlistId: String?
        )
    }
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
        val viewCount: ViewCount?
) {
    data class Owner(
            val videoOwnerRenderer: VideoOwnerRenderer?
    ) {
        data class VideoOwnerRenderer(
                val thumbnail: ThumbnailItem?,
                val title: TextItem?
        )
    }

    data class ViewCount(
            val videoViewCountRenderer: VideoViewCountRenderer?
    ) {
        data class VideoViewCountRenderer(
                val viewCount: ViewCount?,
                val shortViewCount: TextItem?,
                val isLive: Boolean?
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
            val videoOwnerRenderer: VideoOwnerRenderer?
    ) {
        data class VideoOwnerRenderer(
                val navigationEndpoint: NavigationEndpoint?,
                val thumbnail: ThumbnailItem?
        ) {
            data class NavigationEndpoint(
                    val browseEndpoint: BrowseEndpoint?
            ) {
                data class BrowseEndpoint(
                        val browseId: String?
                )
            }
        }
    }
}