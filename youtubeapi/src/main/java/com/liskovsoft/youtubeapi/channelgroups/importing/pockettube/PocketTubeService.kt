package com.liskovsoft.youtubeapi.channelgroups.importing.pockettube

import android.net.Uri
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup.MediaItem
import com.liskovsoft.youtubeapi.channelgroups.importing.GroupImportService
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemImpl
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import java.io.File

internal object PocketTubeService: GroupImportService {
    private val mFileService = RetrofitHelper.create(FileApi::class.java)

    override fun importGroups(url: Uri): List<MediaItemGroup>? {
        val content = mFileService.getContent(url.toString())

        val pocketTubeContent = RetrofitHelper.get(content)?.content ?: return null

        return parseGroups(pocketTubeContent)
    }

    override fun importGroups(file: File): List<MediaItemGroup>? {
        return parseGroups(file.readText())
    }

    private fun parseGroups(pocketTubeContent: String): List<MediaItemGroup>? {
        // Find group names
        val groupNames: List<String> = try {
            JsonPath.read(pocketTubeContent, "$.ysc_collection.*~")
        } catch (e: PathNotFoundException) {
            return null
        }

        val result = mutableListOf<MediaItemGroup>()

        for (groupName in groupNames) {
            // Get groups content
            val channelIds: List<String> = JsonPath.read(pocketTubeContent, "$.$groupName")

            val mediaItems: MutableList<MediaItem> = mutableListOf()

            // channel id: UCsjTlfV61bBwzLLmenR5zmg
            channelIds.forEach { mediaItems.add(MediaItemImpl(channelId = it)) }

            result.add(MediaItemGroupImpl(title = groupName, mediaItems = mediaItems))
        }

        return result
    }
}