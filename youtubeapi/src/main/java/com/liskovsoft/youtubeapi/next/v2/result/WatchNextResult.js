let textItem = {
    runs: [
        {
            text: ""
        }
    ],
    simpleText: ""
};

let thumbnailItem = {
    thumbnails: [
        {
            url: "",
            width: "",
            height: ""
        }
    ]
};

let videoItem = {
    thumbnail: thumbnailItem,
    title: textItem,
    // UserName
    shortBylineText: textItem,
    // UserName
    longBylineText: textItem,
    videoId: ""
};

let tileItem = {
    // Metadata
    // $.metadata.tileMetadataRenderer
    metadata: {
        tileMetadataRenderer: {
            title: textItem
        }
    }
};

let musicItem = {
    thumbnail: thumbnailItem,
    // Title
    primaryText: textItem,
    // Subtitle
    secondaryText: textItem,
    // Views and published
    tertiaryText: textItem
};

let radioItem = {
    thumbnail: thumbnailItem,
    thumbnailRenderer: {
        playlistVideoThumbnailRenderer: {
            thumbnail: thumbnailItem
        }
    },
    title: textItem
};

let channelItem = {
    thumbnail: thumbnailItem,
    title: textItem,
    displayName: textItem,
    channelId: "",
    videoCountText: textItem,
    subscriberCountText: textItem
};

let playlistItem = {
    thumbnail: thumbnailItem,
    thumbnailRenderer: {
        playlistVideoThumbnailRenderer: {
            thumbnail: thumbnailItem
        },
        playlistCustomThumbnailRenderer: {
            thumbnail: thumbnailItem
        }
    },
    title: textItem
};

let WatchNextResult = {
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
                                            {
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
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                }
            },
            // VideoMetadata
            // VideoOwner
            // $.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].videoMetadataRenderer
            // $.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].musicWatchMetadataRenderer
            results: {
                results: {
                    contents: [
                        {
                            itemSectionRenderer: {
                                contents: [
                                    {
                                        videoMetadataRenderer: {
                                             owner: {
                                                 videoOwnerRenderer: {}
                                             }
                                        },
                                        musicWatchMetadataRenderer: {
                                            
                                        }
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
                                maybeHistoryEndpointRenderer: {},
                                autoplayEndpointRenderer: {}
                            }
                        }
                    ],
                    replayVideoRenderer: {}
                }
            }
        }
    },
    // ButtonStates
    // $.transportControls.transportControlsRenderer
    transportControls: {
        transportControlsRenderer: {}
    }
};

JSON.stringify(WatchNextResult);