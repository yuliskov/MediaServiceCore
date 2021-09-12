package com.liskovsoft.youtubeapi.next.v2.result

data class WatchNextResult(
    val contents: Contents?,
    val transportControls: TransportControls?
) {
    data class Contents(
        val singleColumnWatchNextResults: SingleColumnWatchNextResults?
    ) {
        data class SingleColumnWatchNextResults(
            val pivot: Pivot?,
            val results: Results?,
            val autoplay: Autoplay?
        ) {
            data class Pivot(
                val pivot: Pivot?
            ) {
                data class Pivot(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val shelfRenderer: ShelfRenderer?
                    ) {
                        data class ShelfRenderer(
                            val title: Title?,
                            val content: Content?
                        ) {
                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class Content(
                                val horizontalListRenderer: HorizontalListRenderer?
                            ) {
                                data class HorizontalListRenderer(
                                    val items: List<Item?>?
                                ) {
                                    data class Item(
                                        val tileRenderer: TileRenderer?,
                                        val gridVideoRenderer: GridVideoRenderer?,
                                        val pivotVideoRenderer: PivotVideoRenderer?,
                                        val tvMusicVideoRenderer: TvMusicVideoRenderer?,
                                        val gridRadioRenderer: GridRadioRenderer?,
                                        val pivotRadioRenderer: PivotRadioRenderer?,
                                        val gridChannelRenderer: GridChannelRenderer?,
                                        val pivotChannelRenderer: PivotChannelRenderer?,
                                        val gridPlaylistRenderer: GridPlaylistRenderer?,
                                        val pivotPlaylistRenderer: PivotPlaylistRenderer?
                                    ) {
                                        data class TileRenderer(
                                            val metadata: Metadata?
                                        ) {
                                            data class Metadata(
                                                val tileMetadataRenderer: TileMetadataRenderer?
                                            ) {
                                                data class TileMetadataRenderer(
                                                    val title: Title?
                                                ) {
                                                    data class Title(
                                                        val runs: List<Run?>?,
                                                        val simpleText: String?
                                                    ) {
                                                        data class Run(
                                                            val text: String?
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        data class GridVideoRenderer(
                                            val thumbnail: Thumbnail?,
                                            val title: Title?,
                                            val shortBylineText: ShortBylineText?,
                                            val longBylineText: LongBylineText?,
                                            val videoId: String?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class ShortBylineText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class LongBylineText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class PivotVideoRenderer(
                                            val thumbnail: Thumbnail?,
                                            val title: Title?,
                                            val shortBylineText: ShortBylineText?,
                                            val longBylineText: LongBylineText?,
                                            val videoId: String?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class ShortBylineText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class LongBylineText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class TvMusicVideoRenderer(
                                            val thumbnail: Thumbnail?,
                                            val primaryText: PrimaryText?,
                                            val secondaryText: SecondaryText?,
                                            val tertiaryText: TertiaryText?
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

                                            data class PrimaryText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class SecondaryText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class TertiaryText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class GridRadioRenderer(
                                            val thumbnail: Thumbnail?,
                                            val thumbnailRenderer: ThumbnailRenderer?,
                                            val title: Title?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class PivotRadioRenderer(
                                            val thumbnail: Thumbnail?,
                                            val thumbnailRenderer: ThumbnailRenderer?,
                                            val title: Title?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class GridChannelRenderer(
                                            val thumbnail: Thumbnail?,
                                            val title: Title?,
                                            val displayName: DisplayName?,
                                            val channelId: String?,
                                            val videoCountText: VideoCountText?,
                                            val subscriberCountText: SubscriberCountText?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class DisplayName(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class VideoCountText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class SubscriberCountText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class PivotChannelRenderer(
                                            val thumbnail: Thumbnail?,
                                            val title: Title?,
                                            val displayName: DisplayName?,
                                            val channelId: String?,
                                            val videoCountText: VideoCountText?,
                                            val subscriberCountText: SubscriberCountText?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class DisplayName(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class VideoCountText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class SubscriberCountText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class GridPlaylistRenderer(
                                            val thumbnail: Thumbnail?,
                                            val thumbnailRenderer: ThumbnailRenderer?,
                                            val title: Title?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }

                                        data class PivotPlaylistRenderer(
                                            val thumbnail: Thumbnail?,
                                            val thumbnailRenderer: ThumbnailRenderer?,
                                            val title: Title?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
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

            data class Results(
                val results: Results?
            ) {
                data class Results(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val itemSectionRenderer: ItemSectionRenderer?
                    ) {
                        data class ItemSectionRenderer(
                            val contents: List<Content?>?
                        ) {
                            data class Content(
                                val videoMetadataRenderer: VideoMetadataRenderer?,
                                val musicWatchMetadataRenderer: MusicWatchMetadataRenderer?
                            ) {
                                data class VideoMetadataRenderer(
                                    val owner: Owner?,
                                    val title: Title?,
                                    val byline: Byline?,
                                    val albumName: AlbumName?,
                                    val videoId: String?,
                                    val description: Description?,
                                    val publishedTimeText: PublishedTimeText?,
                                    val dateText: DateText?,
                                    val viewCountText: ViewCountText?,
                                    val shortViewCountText: ShortViewCountText?,
                                    val viewCount: ViewCount?
                                ) {
                                    data class Owner(
                                        val videoOwnerRenderer: VideoOwnerRenderer?
                                    ) {
                                        data class VideoOwnerRenderer(
                                            val thumbnail: Thumbnail?,
                                            val title: Title?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }
                                    }

                                    data class Title(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class Byline(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class AlbumName(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class Description(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class PublishedTimeText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class DateText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class ViewCountText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class ShortViewCountText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class ViewCount(
                                        val videoViewCountRenderer: VideoViewCountRenderer?
                                    ) {
                                        data class VideoViewCountRenderer(
                                            val viewCount: ViewCount?,
                                            val shortViewCount: ShortViewCount?,
                                            val isLive: Boolean?
                                        ) {
                                            data class ViewCount(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class ShortViewCount(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }
                                    }
                                }

                                data class MusicWatchMetadataRenderer(
                                    val owner: Owner?,
                                    val title: Title?,
                                    val byline: Byline?,
                                    val albumName: AlbumName?,
                                    val videoId: String?,
                                    val description: Description?,
                                    val publishedTimeText: PublishedTimeText?,
                                    val dateText: DateText?,
                                    val viewCountText: ViewCountText?,
                                    val shortViewCountText: ShortViewCountText?,
                                    val viewCount: ViewCount?
                                ) {
                                    data class Owner(
                                        val videoOwnerRenderer: VideoOwnerRenderer?
                                    ) {
                                        data class VideoOwnerRenderer(
                                            val thumbnail: Thumbnail?,
                                            val title: Title?
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

                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }
                                    }

                                    data class Title(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class Byline(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class AlbumName(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class Description(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class PublishedTimeText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class DateText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class ViewCountText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class ShortViewCountText(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class ViewCount(
                                        val videoViewCountRenderer: VideoViewCountRenderer?
                                    ) {
                                        data class VideoViewCountRenderer(
                                            val viewCount: ViewCount?,
                                            val shortViewCount: ShortViewCount?,
                                            val isLive: Boolean?
                                        ) {
                                            data class ViewCount(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class ShortViewCount(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
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

            data class Autoplay(
                val autoplay: Autoplay?
            ) {
                data class Autoplay(
                    val sets: List<Set?>?,
                    val replayVideoRenderer: ReplayVideoRenderer?
                ) {
                    data class Set(
                        val nextVideoRenderer: NextVideoRenderer?
                    ) {
                        data class NextVideoRenderer(
                            val maybeHistoryEndpointRenderer: MaybeHistoryEndpointRenderer?,
                            val autoplayEndpointRenderer: AutoplayEndpointRenderer?
                        ) {
                            class MaybeHistoryEndpointRenderer

                            class AutoplayEndpointRenderer
                        }
                    }

                    data class ReplayVideoRenderer(
                        val tileRenderer: TileRenderer?,
                        val gridVideoRenderer: GridVideoRenderer?,
                        val pivotVideoRenderer: PivotVideoRenderer?,
                        val tvMusicVideoRenderer: TvMusicVideoRenderer?,
                        val gridRadioRenderer: GridRadioRenderer?,
                        val pivotRadioRenderer: PivotRadioRenderer?,
                        val gridChannelRenderer: GridChannelRenderer?,
                        val pivotChannelRenderer: PivotChannelRenderer?,
                        val gridPlaylistRenderer: GridPlaylistRenderer?,
                        val pivotPlaylistRenderer: PivotPlaylistRenderer?
                    ) {
                        data class TileRenderer(
                            val metadata: Metadata?
                        ) {
                            data class Metadata(
                                val tileMetadataRenderer: TileMetadataRenderer?
                            ) {
                                data class TileMetadataRenderer(
                                    val title: Title?
                                ) {
                                    data class Title(
                                        val runs: List<Run?>?,
                                        val simpleText: String?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }
                                }
                            }
                        }

                        data class GridVideoRenderer(
                            val thumbnail: Thumbnail?,
                            val title: Title?,
                            val shortBylineText: ShortBylineText?,
                            val longBylineText: LongBylineText?,
                            val videoId: String?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class ShortBylineText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class LongBylineText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class PivotVideoRenderer(
                            val thumbnail: Thumbnail?,
                            val title: Title?,
                            val shortBylineText: ShortBylineText?,
                            val longBylineText: LongBylineText?,
                            val videoId: String?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class ShortBylineText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class LongBylineText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class TvMusicVideoRenderer(
                            val thumbnail: Thumbnail?,
                            val primaryText: PrimaryText?,
                            val secondaryText: SecondaryText?,
                            val tertiaryText: TertiaryText?
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

                            data class PrimaryText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class SecondaryText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class TertiaryText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class GridRadioRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: Title?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class PivotRadioRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: Title?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class GridChannelRenderer(
                            val thumbnail: Thumbnail?,
                            val title: Title?,
                            val displayName: DisplayName?,
                            val channelId: String?,
                            val videoCountText: VideoCountText?,
                            val subscriberCountText: SubscriberCountText?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class DisplayName(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class VideoCountText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class SubscriberCountText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class PivotChannelRenderer(
                            val thumbnail: Thumbnail?,
                            val title: Title?,
                            val displayName: DisplayName?,
                            val channelId: String?,
                            val videoCountText: VideoCountText?,
                            val subscriberCountText: SubscriberCountText?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class DisplayName(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class VideoCountText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class SubscriberCountText(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class GridPlaylistRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: Title?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class PivotPlaylistRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: Title?
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

                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class TransportControls(
        val transportControlsRenderer: TransportControlsRenderer?
    ) {
        data class TransportControlsRenderer(
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
                    val thumbnail: Thumbnail?
                ) {
                    data class NavigationEndpoint(
                        val browseEndpoint: BrowseEndpoint?
                    ) {
                        data class BrowseEndpoint(
                            val browseId: String?
                        )
                    }

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
    }
}