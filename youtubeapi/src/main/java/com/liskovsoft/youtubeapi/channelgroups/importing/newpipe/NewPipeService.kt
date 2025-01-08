package com.liskovsoft.youtubeapi.channelgroups.importing.newpipe

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup.Item
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.gen.NewPipeSubscriptionsGroup
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import java.io.File

internal object NewPipeService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<ItemGroup>? {
        val content = mFileService.getContent(url.toString())

        val grayJayContent = RetrofitHelper.get(content)?.content ?: return null

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
        response.subscriptions?.forEach { items.add(ItemImpl(channelId = YouTubeHelper.extractChannelId(Uri.parse(it.url)))) }

        result.add(ItemGroupImpl(title = NewPipeSubscriptionsGroup::subscriptions.name, items = items))

        return result
    }
}