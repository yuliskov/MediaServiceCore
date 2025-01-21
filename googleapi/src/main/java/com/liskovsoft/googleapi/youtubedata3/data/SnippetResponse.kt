package com.liskovsoft.googleapi.youtubedata3.data

internal data class SnippetResponse(
    val items: List<SnippetWrapper?>?
)

internal data class SnippetWrapper(
    val kind: String?,
    val id: String?,
    val snippet: Snippet?,
    val contentDetails: ContentDetails?
) {
    data class Snippet(
        val title: String?,
        val description: String?,
        val channelId: String?,
        val customUrl: String?, // canonical channelId
        val channelTitle: String?,
        val publishedAt: String?,
        val categoryId: String?, // type of the content?
        val thumbnails: ThumbnailsHolder?,
        val localized: Localized?
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

        data class Localized(
            val title: String?,
            val description: String?
        )
    }
    data class ContentDetails(
        val duration: String?, // PT2H4M4S
        val itemCount: Int? // for playlists
    )
}
