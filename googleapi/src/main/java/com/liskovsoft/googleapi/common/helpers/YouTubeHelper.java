package com.liskovsoft.googleapi.common.helpers;

import androidx.annotation.Nullable;

import com.liskovsoft.googleapi.common.models.items.Thumbnail;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;

import java.util.Collections;
import java.util.List;

public final class YouTubeHelper {
    /**
     * NOTE: Optimal thumbnail index is 3. Lower values cause black borders around images on Chromecast and Sony.
     */
    public static final int OPTIMAL_RES_THUMBNAIL_INDEX = 3;
    private static final int SHORTS_LEN_MS = 61_000;

    /**
     * Find optimal thumbnail for tv screen<br/>
     * For Kotlin counterpart see: {@link com.liskovsoft.googleapi.common.models.gen.CommonHelperKt#getOptimalResThumbnailUrl(ThumbnailItem)}
     */
    public static String findOptimalResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size > OPTIMAL_RES_THUMBNAIL_INDEX ? OPTIMAL_RES_THUMBNAIL_INDEX : size - 1).getUrl();
    }

    /**
     * For Kotlin counterpart see: com.liskovsoft.youtubeapi.common.models.gen.CommonHelperKt#getHighResThumbnailUrl(ThumbnailItem)
     */
    public static String findHighResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size - 1).getUrl();
    }

    /**
     * Additional video info such as user, published etc.
     */
    public static @Nullable String createInfo(Object... items) {
        return ServiceHelper.itemsToInfo(items);
    }

    /**
     * Avatar blocking fix
     */
    public static String avatarBlockFix(String url) {
        if (url != null) {
            url = url.replaceFirst("^https://yt3.ggpht.com", "https://yt4.ggpht.com");
        }

        return url;
    }

    public static boolean isEmpty(MediaItem item) {
        if (item == null) {
            return true;
        }

        return Helpers.allNulls(item.getVideoId(), item.getPlaylistId(), item.getReloadPageKey(), item.getParams(), item.getChannelId());
    }
}
