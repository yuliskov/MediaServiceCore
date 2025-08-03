package com.liskovsoft.youtubeapi.channelgroups.importing.pockettube

import android.net.Uri
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup.Item
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl
import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import java.io.File

internal object PocketTubeService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<ItemGroup>? {
        val content = mFileService.getContent(url.toString())

        val pocketTubeContent = RetrofitHelper.get(content)?.content ?: return null

        return parseGroups(pocketTubeContent)
    }

    override fun importGroups(file: File): List<ItemGroup>? {
        return parseGroups(file.readText())
    }

    private fun parseGroups(pocketTubeContent: String): List<ItemGroup>? {
        // Find group names
        val groupNames: List<String> = try {
            JsonPath.read(pocketTubeContent, "$.ysc_collection.*~")
        } catch (e: PathNotFoundException) {
            return null
        }

        val result = mutableListOf<ItemGroup>()

        for (groupName in groupNames) {
            // Get groups content
            val channelIds: List<String> = JsonPath.read(pocketTubeContent, "$['$groupName']")

            val items: MutableList<Item> = mutableListOf()

            // channel id: UCsjTlfV61bBwzLLmenR5zmg
            channelIds.forEach { items.add(ItemImpl(channelId = it)) }

            result.add(ItemGroupImpl(title = groupName, items = items))
        }

        return result
    }
}