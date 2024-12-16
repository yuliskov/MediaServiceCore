package com.liskovsoft.youtubeapi.channelgroups.importing.pockettube

import android.net.Uri
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup.Channel
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelImpl
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

internal object PocketTubeService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<ChannelGroup>? {
        val content = mFileService.getContent(url.toString())

        val pocketTubeContent = RetrofitHelper.get(content)?.content

        // Find group names
        val groupNames: List<String> = try {
            JsonPath.read(pocketTubeContent, "$.ysc_collection.*~")
        } catch (e: PathNotFoundException) {
            return null
        }

        val result = mutableListOf<ChannelGroup>()

        for (groupName in groupNames) {
            // Get groups content
            val channelIds: List<String> = JsonPath.read(pocketTubeContent, "$.$groupName")

            val channels: MutableList<Channel> = mutableListOf()

            // channel id: UCsjTlfV61bBwzLLmenR5zmg
            channelIds.forEach { channels.add(ChannelImpl(channelId = it)) }

            result.add(ChannelGroupImpl(title = groupName, channels = channels))
        }

        return result
    }
}