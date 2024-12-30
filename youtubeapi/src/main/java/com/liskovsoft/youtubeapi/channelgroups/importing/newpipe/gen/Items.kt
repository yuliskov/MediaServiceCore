package com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.gen

internal data class NewPipeSubscriptionsGroup(
    val subscriptions: List<SubscriptionsItem>?
) {
    data class SubscriptionsItem(
        val service_id: Int?,
        val url: String?,
        val name: String?
    )
}