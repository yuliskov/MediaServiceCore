package com.liskovsoft.youtubeapi.next.v2.result.gen

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
                                val title: TextItem?,
                                val content: Content?
                        ) {


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
                                val title: TextItem?,
                                val shortBylineText: TextItem?,
                                val longBylineText: TextItem?,
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






                        }

                        data class PivotVideoRenderer(
                                val thumbnail: Thumbnail?,
                                val title: TextItem?,
                                val shortBylineText: TextItem?,
                                val longBylineText: TextItem?,
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






                        }

                        data class TvMusicVideoRenderer(
                                val thumbnail: Thumbnail?,
                                val primaryText: TextItem?,
                                val secondaryText: TextItem?,
                                val tertiaryText: TextItem?
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

                        data class GridRadioRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: TextItem?
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


                        }

                        data class PivotRadioRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: TextItem?
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


                        }

                        data class GridChannelRenderer(
                                val thumbnail: Thumbnail?,
                                val title: TextItem?,
                                val displayName: TextItem?,
                                val channelId: String?,
                                val videoCountText: TextItem?,
                                val subscriberCountText: TextItem?
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

                        data class PivotChannelRenderer(
                                val thumbnail: Thumbnail?,
                                val title: TextItem?,
                                val displayName: TextItem?,
                                val channelId: String?,
                                val videoCountText: TextItem?,
                                val subscriberCountText: TextItem?
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

                        data class GridPlaylistRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: TextItem?
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


                        }

                        data class PivotPlaylistRenderer(
                            val thumbnail: Thumbnail?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val title: TextItem?
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