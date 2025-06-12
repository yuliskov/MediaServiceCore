package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

internal data class WatchNextResult(
    val contents: Contents?,
    val transportControls: TransportControls?,
    val playerOverlays: PlayerOverlays?,
    val engagementPanels: List<EngagementPanel?>?,
    val frameworkUpdates: FrameworkUpdates?
) {
    data class Contents(
        val singleColumnWatchNextResults: SingleColumnWatchNextResults?
    ) {
        data class SingleColumnWatchNextResults(
            val pivot: Pivot?,
            val results: Results?,
            val autoplay: Autoplay?,
            val conversationBar: ConversationBar?,
            val playlist: Playlist?
        ) {
            data class Pivot(
                val pivot: NestedPivot?,
                val sectionListRenderer: NestedPivot?
            ) {
                data class NestedPivot(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val shelfRenderer: ShelfRenderer?
                    )
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
                                val musicWatchMetadataRenderer: VideoMetadataRenderer?
                            )
                        }
                    }
                }
            }

            data class Autoplay(
                val autoplay: Autoplay?
            ) {
                data class Autoplay(
                    val sets: List<Set?>?,
                    val replayVideoRenderer: ItemWrapper?
                ) {
                    data class Set(
                        val autoplayVideoRenderer: NextVideoRenderer?,
                        val nextVideoRenderer: NextVideoRenderer?
                    )
                }
            }

            data class ConversationBar(
                val liveChatRenderer: LiveChatRenderer?
            ) {
                data class LiveChatRenderer(
                    val continuations: List<ContinuationItem?>?
                )
            }

            data class Playlist(
                val playlist: PlaylistInfo?
            )
        }
    }

    data class TransportControls(
        val transportControlsRenderer: ButtonStateItem?
    )

    data class PlayerOverlays(
        val playerOverlayRenderer: PlayerOverlayRenderer?
    ) {
        data class PlayerOverlayRenderer(
            val decoratedPlayerBarRenderer: DecoratedPlayerBar?
        ) {
            data class DecoratedPlayerBar(
                val decoratedPlayerBarRenderer: DecoratedPlayerBarRenderer?
            ) {
                data class DecoratedPlayerBarRenderer(
                    val playerBar: PlayerBar?
                ) {
                    data class PlayerBar(
                        val multiMarkersPlayerBarRenderer: MultiMarkersPlayerBarRenderer?
                    ) {
                        data class MultiMarkersPlayerBarRenderer(
                            val markersMap: List<MapItem?>?
                        ) {
                            data class MapItem(
                                val key: String?,
                                val value: Value?
                            ) {
                                data class Value(
                                    val chapters: List<ChapterItemWrapper?>?
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class FrameworkUpdates(
        val entityBatchUpdate: EntityBatchUpdate?
    ) {
        data class EntityBatchUpdate(
            val mutations: List<Mutation?>?
        ) {
            data class Mutation(
                val payload: Payload?
            ) {
                data class Payload(
                    val macroMarkersListEntity: MacroMarkersListEntity?
                ) {
                    data class MacroMarkersListEntity(
                        val markersList: MarkersList?
                    ) {
                        data class MarkersList(
                            val markers: List<ChapterItemWrapper?>?,
                            val markerType: String?
                        )
                    }
                }
            }
        }
    }
}