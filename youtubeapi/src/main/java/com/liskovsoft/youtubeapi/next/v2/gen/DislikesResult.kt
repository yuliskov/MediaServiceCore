package com.liskovsoft.youtubeapi.next.v2.gen

internal data class DislikesResult(
    val id: String?,
    val dateCreated: String?,
    val likes: Int?,
    val dislikes: Int?,
    val rating: Float?,
    val viewCount: Long?, // should be long
    val deleted: Boolean?
)
