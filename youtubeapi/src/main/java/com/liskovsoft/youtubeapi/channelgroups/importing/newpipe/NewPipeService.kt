package com.liskovsoft.youtubeapi.channelgroups.importing.newpipe

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup.Channel
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.gen.NewPipeSubscriptionsGroup
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelImpl
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import java.io.File

internal object NewPipeService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<ChannelGroup>? {
        val content = mFileService.getContent(url.toString())

        val grayJayContent = RetrofitHelper.get(content)?.content ?: return null

        return parseGroups(grayJayContent)
    }

    override fun importGroups(file: File): List<ChannelGroup>? {
        return parseGroups(file.readText())
    }

    private fun parseGroups(newPipeContent: String): List<ChannelGroup>? {
        val gson = Gson()
        val myType = object : TypeToken<NewPipeSubscriptionsGroup>() {}.type

        val response: NewPipeSubscriptionsGroup = try {
            gson.fromJson(newPipeContent, myType)
        } catch (e: JsonSyntaxException) {
            return null
        }

        val result = mutableListOf<ChannelGroup>()

        val channels: MutableList<Channel> = mutableListOf()

        // channel url: https://www.youtube.com/channel/UCbWcXB0PoqOsAvAdfzWMf0w
        response.subscriptions?.forEach { channels.add(ChannelImpl(channelId = YouTubeHelper.extractChannelId(Uri.parse(it.url)))) }

        result.add(ChannelGroupImpl(title = NewPipeSubscriptionsGroup::subscriptions.name, channels = channels))

        return result
    }
}