package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;

import java.util.List;

public final class YouTubeMediaServiceHelper {
    public static String findHighResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null || thumbnails.size() == 0) {
            return null;
        }

        return thumbnails.get(thumbnails.size() - 1).getUrl();
    }

    /**
     * Additional video info such as user, published etc.
     */
    public static String createInfo(Object... items) {
        return ServiceHelper.itemsToInfo(items);
    }

    public static String extractNextKey(MediaGroup mediaTab) {
        String result = null;

        if (mediaTab instanceof YouTubeMediaGroup) {
            result = ((YouTubeMediaGroup) mediaTab).mNextPageKey;
        } else if (mediaTab instanceof MediaGroupImpl) {
            result = ((MediaGroupImpl) mediaTab).getNextPageKey();
        }

        return result;
    }
}
