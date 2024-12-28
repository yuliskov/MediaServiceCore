package com.liskovsoft.youtubeapi.channelgroups.importing

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup
import java.io.File

internal interface GroupImportService {
    fun importGroups(url: Uri): List<ChannelGroup>?
    fun importGroups(file: File): List<ChannelGroup>?
}
