package com.liskovsoft.youtubeapi.next.v2.gen.kt

import com.liskovsoft.youtubeapi.common.models.kt.ItemWrapper

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
            val autoplay: Autoplay?,
            val conversationBar: ConversationBar?
        ) {
            data class Pivot(
                val pivot: NestedPivot?,
                val sectionListRenderer: NestedPivot?
            ) {
                data class NestedPivot(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val shelfRenderer: ShelfItem?
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
                                    val videoMetadataRenderer: VideoMetadataItem?,
                                    val musicWatchMetadataRenderer: VideoMetadataItem?
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
                        val nextVideoRenderer: NextVideoRenderer?
                    ) {
                        data class NextVideoRenderer(
                                val maybeHistoryEndpointRenderer: NextVideoItem?,
                                val autoplayEndpointRenderer: NextVideoItem?,
                                val autoplayVideoWrapperRenderer: AutoplayVideoWrapperRenderer?
                        ) {
                            data class AutoplayVideoWrapperRenderer(
                                val primaryEndpointRenderer: PrimaryEndpointRenderer?
                            ) {
                                data class PrimaryEndpointRenderer(
                                    val autoplayEndpointRenderer: NextVideoItem?
                                )
                            }
                        }
                    }
                }
            }

            data class ConversationBar(
                val liveChatRenderer: LiveChatRenderer?
            ) {
                data class LiveChatRenderer(
                    val continuations: List<ContinuationItem?>?
                )
            }
        }
    }

    data class TransportControls(
        val transportControlsRenderer: ButtonStateItem?
    )
}