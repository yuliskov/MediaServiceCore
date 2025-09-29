package com.liskovsoft.youtubeapi.channelgroups.importing.newpipe

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.googleapi.youtubedata3.YouTubeDataServiceInt
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup.Item
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.gen.NewPipeSubscriptionsGroup
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.app.nsigsolver.common.YouTubeInfoExtractor
import java.io.File

internal object NewPipeService: GroupImportService {
    override fun importGroups(url: Uri): List<ItemGroup>? {
        val grayJayContent = YouTubeInfoExtractor.downloadWebpageSilent(url.toString()) ?: return null

        return parseGroups(grayJayContent)
    }

    override fun importGroups(file: File): List<ItemGroup>? {
        return parseGroups(file.readText())
    }

    private fun parseGroups(newPipeContent: String): List<ItemGroup>? {
        val gson = Gson()
        val myType = object : TypeToken<NewPipeSubscriptionsGroup>() {}.type

        val response: NewPipeSubscriptionsGroup = try {
            gson.fromJson(newPipeContent, myType)
        } catch (e: JsonSyntaxException) {
            return null
        }

        val result = mutableListOf<ItemGroup>()

        val items: MutableList<Item> = mutableListOf()

        // channel url: https://www.youtube.com/channel/UCbWcXB0PoqOsAvAdfzWMf0w
        response.subscriptions?.forEach { items.add(ItemImpl(channelId = YouTubeHelper.extractChannelId(Uri.parse(it.url)), title = it.name)) }

        // Get channels thumbs and titles
        val metadata = YouTubeDataServiceInt.getChannelMetadata(*items.mapNotNull { it.channelId }.toTypedArray())
        val newItems = metadata?.map { ItemImpl(it.channelId, it.title, it.cardImageUrl) }

        result.add(ItemGroupImpl(title = NewPipeSubscriptionsGroup::subscriptions.name, items = newItems?.toMutableList() ?: items))

        return result
    }
}