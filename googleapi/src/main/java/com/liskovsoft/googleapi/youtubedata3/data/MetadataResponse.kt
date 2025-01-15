package com.liskovsoft.googleapi.youtubedata3.data

internal data class MetadataResponse(
    val items: List<ItemMetadata?>?
)

internal data class ItemMetadata(
    val kind: String?,
    val id: String?,
    val snippet: Snippet?
) {
    data class Snippet(
        val title: String?,
        val description: String?,
        val channelId: String?,
        val customUrl: String?, // canonical channelId
        val channelTitle: String?,
        val publishedAt: String?,
        val categoryId: String?, // type of the content?
        val thumbnails: ThumbnailsHolder?
    ) {
        data class ThumbnailsHolder(
            val default: ThumbnailItem?,
            val medium: ThumbnailItem?,
            val high: ThumbnailItem?,
            val standard: ThumbnailItem?,
            val maxres: ThumbnailItem?,
        ) {
            data class ThumbnailItem(
                val url: String?,
                val width: Int?,
                val height: Int?
            )
        }
    }
}
