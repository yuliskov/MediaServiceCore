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

let videoItem = {
    thumbnail: thumbnailItem,
    title: textItem, // UserName
    shortBylineText: textItem, // UserName
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
    },
    onSelectCommand: {
        watchEndpoint: {
            videoId: "",
            playlistId: ""
        },
        watchPlaylistEndpoint: {
            playlistId: ""
        }
    }
};

let musicItem = {
    thumbnail: thumbnailItem, // Title
    primaryText: textItem, // Subtitle
    secondaryText: textItem, // Views and published
    tertiaryText: textItem,
    navigationEndpoint: {
        watchEndpoint: {
            videoId: "",
            playlistId: ""
        }
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
