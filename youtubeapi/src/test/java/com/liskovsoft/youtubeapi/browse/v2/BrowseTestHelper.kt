package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import org.junit.Assert.assertNotNull

object BrowseTestHelper {
    fun checkMediaItem(mediaItem: MediaItem) {
        assertNotNull(mediaItem.title)
        assertNotNull(mediaItem.secondTitle)
        assertNotNull(mediaItem.videoId)
    }

    fun checkGuideMediaItem(mediaItem: MediaItem) {
        assertNotNull(mediaItem.title)
        assertNotNull(mediaItem.channelId ?: mediaItem.reloadPageKey)
        assertNotNull(mediaItem.cardImageUrl)
        assertNotNull(mediaItem.backgroundImageUrl)
    }
}