package com.liskovsoft.youtubeapi.channelgroups.importing

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup

internal interface GroupImportService {
    fun importGroups(url: Uri): List<ChannelGroup>?
}
