package com.liskovsoft.youtubeapi.channelgroups.importing.grayjay

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup.Item
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen.GrayJayGroup
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemImpl
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import java.io.File

internal object GrayJayService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<ItemGroup>? {
        val content = mFileService.getContent(url.toString())

        val grayJayContent = RetrofitHelper.get(content)?.content ?: return null

        return parseGroups(grayJayContent)
    }

    override fun importGroups(file: File): List<ItemGroup>? {
        return parseGroups(file.readText())
    }

    private fun parseGroups(grayJayContent: String): List<ItemGroup>? {
        // replace:
        // "{ => {
        // }" => }
        // \" => "

        val grayJayContentFixed = grayJayContent
            .replace("\"{", "{")
            .replace("}\"", "}")
            .replace("\\\"", "\"")

        val gson = Gson()
        val listType = object : TypeToken<List<GrayJayGroup>>() {}.type

        val response: List<GrayJayGroup> = try {
            gson.fromJson(grayJayContentFixed, listType)
        } catch (e: JsonSyntaxException) {
            return null
        }

        val result = mutableListOf<ItemGroup>()

        for (group in response) {
            val items: MutableList<Item> = mutableListOf()

            // channel url: https://www.youtube.com/channel/UCbWcXB0PoqOsAvAdfzWMf0w
            group.urls?.forEach { items.add(MediaItemImpl(channelId = YouTubeHelper.extractChannelId(Uri.parse(it)))) }

            result.add(MediaItemGroupImpl(group.id.hashCode(), group.name, group.image?.url, items))
        }

        return result
    }
}