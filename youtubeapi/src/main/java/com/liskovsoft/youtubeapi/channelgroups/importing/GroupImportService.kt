package com.liskovsoft.youtubeapi.channelgroups.importing

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup
import java.io.File

internal interface GroupImportService {
    fun importGroups(url: Uri): List<MediaItemGroup>?
    fun importGroups(file: File): List<MediaItemGroup>?
}
