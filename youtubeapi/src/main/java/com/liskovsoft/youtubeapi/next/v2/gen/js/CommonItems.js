let textItem = {
    runs: [{
        text: "",
        navigationEndpoint: navigationEndpointItem
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

let navigationEndpointItem = {
    browseEndpoint: {
        browseId: ""
    },
    watchEndpoint: watchEndpointItem
};

let watchEndpointItem = {
    videoId: "",
    playlistId: "",
    index: 0,
    params: ""
};

let menuItem = {
    menuRenderer: {
        items: [{
            menuServiceItemRenderer: {
                serviceEndpoint: {
                    feedbackEndpoint: {
                        feedbackToken: ""
                    }
                }
            },
            menuNavigationItemRenderer: {
                navigationEndpoint: navigationEndpointItem
            }
        }]
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
        thumbnailOverlayItem
    ],
    badges: [{
        liveBadge: {
            label: textItem // BadgeText
        },
        upcomingEventBadge: {
            label: textItem // BadgeText
        },
        metadataBadgeRenderer: {
            label: "" // DescBadgeText
        }
    }],
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
            lines: [{
                lineRenderer: {
                    items: [{
                        lineItemRenderer: {
                            text: textItem,
                            badge: {
                                metadataBadgeRenderer: {
                                    style: "", // BadgeStyle
                                    label: "" // DescBadgeText
                                }
                            }
                        }
                    }]
                }
            }]
        }
    },
    header: {
        tileHeaderRenderer: {
            thumbnail: thumbnailItem,
            thumbnailOverlays: [
                thumbnailOverlayItem
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
    menu: menuItem,
    contentType: ""
};

let musicItem = {
    thumbnail: thumbnailItem, // Title
    primaryText: textItem, // Subtitle
    secondaryText: textItem, // Views and published
    tertiaryText: textItem,
    navigationEndpoint: navigationEndpointItem,
    lengthText: textItem
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
    title: textItem,
    subscribed: false,
    subscriptionButton: {
        subscribed: false
    },
    subscribeButton: {
        subscribeButtonRenderer: {
            subscribed: false,
            channelId: ""
        }
    },
    navigationEndpoint: navigationEndpointItem
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
    },
    likeStatus: "",
    likeButton: {
        likeButtonRenderer: {
            likeStatus: ""
        }
    },
    badges: [{
        upcomingEventBadge: {
            label: textItem
        }
    }],
    thumbnailOverlays: [
        thumbnailOverlayItem
    ]
};

let itemWrapper = {
    // TileItem
    tileRenderer: tileItem, // universal items (video, music etc). new api
    gridVideoRenderer: videoItem,
    pivotVideoRenderer: videoItem, // suggested item
    compactVideoRenderer: videoItem, // history/search result item
    tvMusicVideoRenderer: musicItem,
    gridRadioRenderer: radioItem,
    pivotRadioRenderer: radioItem,
    compactRadioRenderer: radioItem,
    gridChannelRenderer: channelItem,
    pivotChannelRenderer: channelItem,
    compactChannelRenderer: channelItem,
    gridPlaylistRenderer: playlistItem,
    pivotPlaylistRenderer: playlistItem,
    compactPlaylistRenderer: playlistItem
};

let chipItem = {
    chipCloudChipRenderer: {
        text: textItem,
        content: {
            horizontalListRenderer: {
                items: [itemWrapper],
                continuations: [continuationItem]
            },
            sectionListRenderer: {
                contents: [
                    {
                        shelfRenderer: shelfItem
                    }
                ]
            }
        }
    }
};

let continuationItem = {
    reloadContinuationData: {
        continuation: ""
    },
    nextContinuationData: {
        continuation: ""
    }
};

let shelfItem = {
    title: textItem, // ItemWrappers
    // $.content.horizontalListRenderer.items[*]
    content: {
        horizontalListRenderer: {
            items: [itemWrapper],
            continuations: [continuationItem]
        }
    },
    headerRenderer: {
        shelfHeaderRenderer: {
            title: textItem
        },
        chipCloudRenderer: {
            chips: [chipItem]
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

let thumbnailOverlayItem = {
    thumbnailOverlayTimeStatusRenderer: {
        text: textItem, // BadgeText
        style: "" // BadgeStyle
    },
    thumbnailOverlayResumePlaybackRenderer: {
        percentDurationWatched: 0
    }
};

let richThumbnailItem = {
    movingThumbnailRenderer: {
        movingThumbnailDetails: thumbnailItem
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
        videoOwnerRenderer: videoOwnerItem
    }
};
