let buttonState = {
    subscribeButton: {
        toggleButtonRenderer: {
            isToggled: false
        }
    },
    likeButton: {
        toggleButtonRenderer: {
            isToggled: false
        }
    },
    dislikeButton: {
        toggleButtonRenderer: {
            isToggled: false
        }
    },
    channelButton: {
        videoOwnerRenderer: {
            navigationEndpoint: {
                browseEndpoint: {
                    browseId: ""
                }
            },
            thumbnail: thumbnailItem
        }
    }
};

let itemWrapper = {
    // TileItem
    tileRenderer: tileItem,
    // VideoItem
    gridVideoRenderer: videoItem,
    // SuggestedVideoItem
    pivotVideoRenderer: videoItem,
    // MusicItem
    tvMusicVideoRenderer: musicItem,
    // RadioItem
    gridRadioRenderer: radioItem,
    pivotRadioRenderer: radioItem,
    // ChannelItem
    gridChannelRenderer: channelItem,
    pivotChannelRenderer: channelItem,
    // PlaylistItem
    gridPlaylistRenderer: playlistItem,
    pivotPlaylistRenderer: playlistItem
};

let nextVideoItem = {

};

let watchNextResult = {
    contents: {
        singleColumnWatchNextResults: {
            // SuggestedSections
            // $.contents.singleColumnWatchNextResults.pivot.pivot.contents[*].shelfRenderer
            // $.title.runs[0].text, $.title.simpleText
            pivot: {
                pivot: {
                    contents: [
                        {
                            shelfRenderer: {
                                title: textItem,
                                // ItemWrappers
                                // $.content.horizontalListRenderer.items[*]
                                content: {
                                    horizontalListRenderer: {
                                        items: [
                                            itemWrapper
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                }
            },
            // VideoMetadata and VideoOwner
            // $.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].videoMetadataRenderer
            // $.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].musicWatchMetadataRenderer
            results: {
                results: {
                    contents: [
                        {
                            itemSectionRenderer: {
                                contents: [
                                    {
                                        videoMetadataRenderer: videoMetadataItem,
                                        musicWatchMetadataRenderer: videoMetadataItem
                                    }
                                ]
                            }
                        }
                    ]
                }
            },
            // NextVideo
            // VideoDetails
            // ReplayItem
            // $.contents.singleColumnWatchNextResults.autoplay.autoplay.sets[0].nextVideoRenderer.maybeHistoryEndpointRenderer
            // $.contents.singleColumnWatchNextResults.autoplay.autoplay.sets[0].nextVideoRenderer.autoplayEndpointRenderer
            // $.contents.singleColumnWatchNextResults.autoplay.autoplay.sets.replayVideoRenderer.pivotVideoRenderer ????
            // $.contents.singleColumnWatchNextResults.autoplay.autoplay.replayVideoRenderer
            autoplay: {
                autoplay: {
                    sets: [
                        {
                            nextVideoRenderer: {
                                maybeHistoryEndpointRenderer: nextVideoItem,
                                autoplayEndpointRenderer: nextVideoItem
                            }
                        }
                    ],
                    replayVideoRenderer: itemWrapper
                }
            }
        }
    },
    // ButtonStates
    // $.transportControls.transportControlsRenderer
    transportControls: {
        transportControlsRenderer: buttonState
    }
};

JSON.stringify(watchNextResult);