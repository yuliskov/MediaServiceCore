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
                            shelfRenderer: shelfItem
                        }
                    ]
                },
                sectionListRenderer: {
                    contents: [
                        {
                            shelfRenderer: shelfItem
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
            },
            // LIVE Chat Key
            conversationBar: {
                liveChatRenderer: {
                    continuations: [continuationItem]
                }
            }
        }
    },
    // ButtonStates
    // $.transportControls.transportControlsRenderer
    transportControls: {
        transportControlsRenderer: buttonStateItem
    }
};

JSON.stringify(watchNextResult);