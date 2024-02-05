package com.liskovsoft.youtubeapi.dearrow.data

internal data class BrandingList(
    val titles: List<Title?>?,
    val thumbnails: List<Thumbnail?>?,
    val randomTime: Float?,
    val videoDuration: Float?) {
        data class Title(
            val title: String?,
            val original: Boolean?,
            val votes: Int?,
            val locked: Boolean?,
            val UUID: String?
        )
        data class Thumbnail(
            val timestamp: Float?,
            val original: Boolean?,
            val votes: Int?,
            val locked: Boolean?,
            val UUID: String?
        )
}
