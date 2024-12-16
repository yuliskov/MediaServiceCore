package com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen

internal data class GrayJayGroup(
    val id: String,
    val name: String,
    val image: GrayJayImage?,
    val urls: List<String>?,
    val creationTime: Int?
) {
    data class GrayJayImage(
        val url: String?
    )
}