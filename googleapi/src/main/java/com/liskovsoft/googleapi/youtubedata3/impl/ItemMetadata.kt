package com.liskovsoft.googleapi.youtubedata3.impl

interface ItemMetadata {
    val title: String?
    val cardImageUrl: String?
    val channelId: String?
    val videoId: String?
    val playlistId: String?
    val channelTitle: String?
    val publishedAt: String?
    val durationIso: String?
    val itemCount: Int?
}
