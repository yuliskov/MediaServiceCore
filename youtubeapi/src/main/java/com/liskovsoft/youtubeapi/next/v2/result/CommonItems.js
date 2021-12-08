let textItem = {
    runs: [{
        text: ""
    }],
    simpleText: ""
};

let thumbnailItem = {
    thumbnails: [{
        url: "",
        width: "",
        height: ""
    }]
};

let watchEndpointItem = {
    videoId: "",
    playlistId: "",
    index: 0,
    params: ""
};

let menuItem = {
    menuRenderer: {
        items: [
            {
                menuServiceItemRenderer: {
                    serviceEndpoint: {
                        feedbackEndpoint: {
                            feedbackToken: ""
                        }
                    }
                }
            }
        ]
    }
};

let videoItem = {
    thumbnail: thumbnailItem,
    title: textItem, // UserName
    shortBylineText: textItem, // UserName
    longBylineText: textItem,
    videoId: "",
    trackingParams: "",
    shortViewCountText: textItem,
    viewCountText: textItem,
    lengthText: textItem,
    publishedTimeText: textItem,
    thumbnailOverlays: [
        {
            thumbnailOverlayTimeStatusRenderer: {
                text: textItem, // BadgeText
                style: "" // BadgeStyle
            },
            thumbnailOverlayResumePlaybackRenderer: {
                percentDurationWatched: 0
            }
        }
    ],
    badges: [
        {   liveBadge: {
                label: textItem // BadgeText
            },
            upcomingEventBadge: {
                label: textItem // BadgeText
            },
            metadataBadgeRenderer: {
                label: "" // DescBadgeText
            }
        }
    ],
    upcomingEventData: {
        upcomingEventText: textItem,
        startTime: ""
    },
    richThumbnail: richThumbnailItem,
    menu: menuItem
};

let tileItem = {
    // Metadata
    // $.metadata.tileMetadataRenderer
    metadata: {
        tileMetadataRenderer: {
            title: textItem,
            lines: [
                {
                    lineRenderer: {
                        items: [
                            {
                                lineItemRenderer: {
                                    text: textItem,
                                    badge: {
                                        metadataBadgeRenderer: {
                                            style: "", // BadgeStyle
                                            label: "" // DescBadgeText
                                        }
                                    }
                                }
                            }
                        ]
                    }
                }
            ]
        }
    },
    header: {
        tileHeaderRenderer: {
            thumbnail: thumbnailItem,
            thumbnailOverlays: [
                {
                    thumbnailOverlayTimeStatusRenderer: {
                        text: textItem,
                        style: ""
                    },
                    thumbnailOverlayResumePlaybackRenderer: {
                        percentDurationWatched: 0
                    }
                }
            ],
            movingThumbnail: thumbnailItem
        }
    },
    onSelectCommand: {
        watchEndpoint: watchEndpointItem,
        watchPlaylistEndpoint: {
            playlistId: ""
        }
    },
    menu: menuItem
};

let musicItem = {
    thumbnail: thumbnailItem, // Title
    primaryText: textItem, // Subtitle
    secondaryText: textItem, // Views and published
    tertiaryText: textItem,
    navigationEndpoint: {
        watchEndpoint: watchEndpointItem
    }
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

let videoOwnerItem = {
    thumbnail: thumbnailItem,
    title: textItem
};

let videoMetadataItem = {
    owner: {
        videoOwnerRenderer: videoOwnerItem
    },
    title: textItem,
    byline: textItem,
    albumName: textItem,
    videoId: "",
    description: textItem,
    publishedTimeText: textItem,
    dateText: textItem,
    viewCountText: textItem,
    shortViewCountText: textItem,
    viewCount: {
        videoViewCountRenderer: {
            viewCount: textItem,
            shortViewCount: textItem,
            isLive: false
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

let shelfItem = {
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
};

let nextVideoItem = {
    item: {
        previewButtonRenderer: {
            thumbnail: thumbnailItem,
            title: textItem,
            byline: textItem
        }
    },
    endpoint: {
        watchEndpoint: watchEndpointItem
    }
};

let thumbnailOverlaysItem = {
    
};

let richThumbnailItem = {
    movingThumbnailRenderer: {
        movingThumbnailDetails: {
            thumbnails: [
                {
                    url: ""
                }
            ]
        }
    }
};

let buttonStateItem = {
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
