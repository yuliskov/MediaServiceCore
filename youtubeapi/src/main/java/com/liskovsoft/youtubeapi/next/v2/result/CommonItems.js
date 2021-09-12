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
    }
};

let musicItem = {
    thumbnail: thumbnailItem, // Title
    primaryText: textItem, // Subtitle
    secondaryText: textItem, // Views and published
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
