package com.liskovsoft.youtubeapi.next.v2.result.gen

data class TextItem(
        val runs: List<Run?>?,
        val simpleText: String?
) {
    data class Run(
            val text: String?
    )
}

data class Thumbnails(
        val thumbnails: List<Thumbnail?>?
) {
    data class Thumbnail(
            val url: String?,
            val width: String?,
            val height: String?
    )
}

data class TileRenderer(
        val metadata: Metadata?
) {
    data class Metadata(
            val tileMetadataRenderer: TileMetadataRenderer?
    ) {
        data class TileMetadataRenderer(
                val title: TextItem?
        ) {

        }
    }
}

data class GridVideoRenderer(
        val thumbnail: Thumbnails?,
        val title: TextItem?,
        val shortBylineText: TextItem?,
        val longBylineText: TextItem?,
        val videoId: String?
) {

    
    
    
}

data class PivotVideoRenderer(
        val thumbnail: Thumbnails?,
        val title: TextItem?,
        val shortBylineText: TextItem?,
        val longBylineText: TextItem?,
        val videoId: String?
) {

    
    
    
}

data class TvMusicVideoRenderer(
        val thumbnail: Thumbnails?,
        val primaryText: TextItem?,
        val secondaryText: TextItem?,
        val tertiaryText: TextItem?
) {

    
    
    
}

data class GridRadioRenderer(
        val thumbnail: Thumbnails?,
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

data class PivotRadioRenderer(
        val thumbnail: Thumbnails?,
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

data class GridChannelRenderer(
        val thumbnail: Thumbnails?,
        val title: TextItem?,
        val displayName: TextItem?,
        val channelId: String?,
        val videoCountText: TextItem?,
        val subscriberCountText: TextItem?
) {

    
    
    
    
}

data class PivotChannelRenderer(
        val thumbnail: Thumbnails?,
        val title: TextItem?,
        val displayName: TextItem?,
        val channelId: String?,
        val videoCountText: TextItem?,
        val subscriberCountText: TextItem?
) {

    
    
    
    
}

data class GridPlaylistRenderer(
        val thumbnail: Thumbnails?,
        val thumbnailRenderer: ThumbnailRenderer?,
        val title: TextItem?
) {


    data class ThumbnailRenderer(
            val playlistVideoThumbnailRenderer: PlaylistVideoThumbnailRenderer?,
            val playlistCustomThumbnailRenderer: PlaylistCustomThumbnailRenderer?
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

        data class PlaylistCustomThumbnailRenderer(
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

data class PivotPlaylistRenderer(
        val thumbnail: Thumbnails?,
        val thumbnailRenderer: ThumbnailRenderer?,
        val title: TextItem?
) {


    data class ThumbnailRenderer(
            val playlistVideoThumbnailRenderer: PlaylistVideoThumbnailRenderer?,
            val playlistCustomThumbnailRenderer: PlaylistCustomThumbnailRenderer?
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

        data class PlaylistCustomThumbnailRenderer(
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

data class VideoMetadataRenderer(
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
                val thumbnail: Thumbnails?,
                val title: TextItem?
        ) {



        }
    }
    
    
    
    
    
    
    
    

    data class ViewCount(
            val videoViewCountRenderer: VideoViewCountRenderer?
    ) {
        data class VideoViewCountRenderer(
                val viewCount: ViewCount?,
                val shortViewCount: TextItem?,
                val isLive: Boolean?
        ) {



        }
    }
}

data class MusicWatchMetadataRenderer(
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
                val thumbnail: Thumbnails?,
                val title: TextItem?
        ) {



        }
    }
    
    
    
    
    
    
    
    

    data class ViewCount(
            val videoViewCountRenderer: VideoViewCountRenderer?
    ) {
        data class VideoViewCountRenderer(
                val viewCount: ViewCount?,
                val shortViewCount: TextItem?,
                val isLive: Boolean?
        ) {



        }
    }
}