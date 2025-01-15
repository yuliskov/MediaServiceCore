package com.liskovsoft.youtubeapi.channelgroups.importing

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup
import java.io.File

internal interface GroupImportService {
    fun importGroups(url: Uri): List<ItemGroup>?
    fun importGroups(file: File): List<ItemGroup>?
}
