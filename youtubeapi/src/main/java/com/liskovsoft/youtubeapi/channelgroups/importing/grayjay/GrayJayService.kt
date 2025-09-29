package com.liskovsoft.youtubeapi.channelgroups.importing.grayjay

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup.Item
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen.GrayJayGroup
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.app.nsigsolver.common.YouTubeInfoExtractor
import java.io.File

internal object GrayJayService: GroupImportService {
    override fun importGroups(url: Uri): List<ItemGroup>? {
        val grayJayContent = YouTubeInfoExtractor.downloadWebpageSilent(url.toString()) ?: return null

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
            group.urls?.forEach { items.add(ItemImpl(channelId = YouTubeHelper.extractChannelId(Uri.parse(it)))) }

            result.add(ItemGroupImpl(group.id, group.name, group.image?.url, items))
        }

        return result
    }
}