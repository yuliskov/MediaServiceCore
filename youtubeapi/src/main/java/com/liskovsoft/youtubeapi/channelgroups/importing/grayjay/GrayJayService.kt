package com.liskovsoft.youtubeapi.channelgroups.importing.grayjay

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup.Channel
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen.GrayJayGroup
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelImpl
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper

internal object GrayJayService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<ChannelGroup>? {
        val content = mFileService.getContent(url.toString())

        val grayJayContent = RetrofitHelper.get(content)?.content

        // replace:
        // "{ => {
        // }" => }
        // \" => "

        val grayJayContentFixed = grayJayContent
            ?.replace("\"{", "{")
            ?.replace("}\"", "}")
            ?.replace("\\\"", "\"")

        val gson = Gson()
        val listType = object : TypeToken<List<GrayJayGroup>>() {}.type

        val response: List<GrayJayGroup> = try {
            gson.fromJson(grayJayContentFixed, listType)
        } catch (e: JsonSyntaxException) {
            return null
        }

        val result = mutableListOf<ChannelGroup>()

        for (group in response) {
            val channels: MutableList<Channel> = mutableListOf()

            // channel url: https://www.youtube.com/channel/UCbWcXB0PoqOsAvAdfzWMf0w
            group.urls?.forEach { channels.add(ChannelImpl(channelId = YouTubeHelper.extractChannelId(Uri.parse(it)))) }

            result.add(ChannelGroupImpl(group.id.hashCode(), group.name, group.image?.url, channels))
        }

        return result
    }
}