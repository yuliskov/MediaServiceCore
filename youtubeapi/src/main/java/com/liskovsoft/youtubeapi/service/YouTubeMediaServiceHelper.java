package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;

import java.util.Collections;
import java.util.List;

public final class YouTubeMediaServiceHelper {
    /**
     * Optimal thumbnail for tv screen
     */
    public static final int LOW_RES_THUMBNAIL_INDEX = 2;
    private static final int SHORTS_LEN_MS = 60 * 1_000;

    /**
     * Find optimal thumbnail for tv screen
     */
    public static String findLowResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size > LOW_RES_THUMBNAIL_INDEX ? LOW_RES_THUMBNAIL_INDEX : size - 1).getUrl();
    }

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

    public static void filterIfNeeded(MediaGroup mediaGroup) {
        if (mediaGroup == null || mediaGroup.getMediaItems() == null) {
            return;
        }

        if (mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS) {
            // LIVE & UPCOMING videos always on top
            Collections.sort(mediaGroup.getMediaItems(), (o1, o2) ->
                    o1.isLive() == o2.isLive() && o1.isUpcoming() == o2.isUpcoming() ? 0 :
                            o1.isLive() || (o1.isUpcoming() && !o2.isLive()) ? -1 : 1
            );
        }

        if (GlobalPreferences.isInitialized()) {
            boolean isHideShortsEnabled = (GlobalPreferences.sInstance.isHideShortsFromSubscriptionsEnabled() && mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS) ||
                    (GlobalPreferences.sInstance.isHideShortsFromHomeEnabled() && mediaGroup.getType() == MediaGroup.TYPE_HOME) ||
                    (GlobalPreferences.sInstance.isHideShortsFromHistoryEnabled() && mediaGroup.getType() == MediaGroup.TYPE_HISTORY);
            boolean isHideUpcomingEnabled = GlobalPreferences.sInstance.isHideUpcomingEnabled() && mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS;

            if (isHideShortsEnabled || isHideUpcomingEnabled) {

                // Remove Shorts and/or Upcoming
                // NOTE: Predicate replacement function for devices with Android 6.0 and below.
                Helpers.removeIf(mediaGroup.getMediaItems(), mediaItem -> {
                    if (mediaItem == null) {
                        return false;
                    }

                    return (isHideShortsEnabled && isShort(mediaItem)) || (isHideUpcomingEnabled && mediaItem.isUpcoming());
                });
            }
        }
    }

    public static boolean isShort(MediaItem mediaItem) {
        if (mediaItem == null || mediaItem.getTitle() == null) {
            return false;
        }

        String title = mediaItem.getTitle().toLowerCase();

        int lengthMs = mediaItem.getDurationMs();
        boolean isShortLength = lengthMs > 0 && lengthMs < SHORTS_LEN_MS;
        return isShortLength || title.contains("#short") || title.contains("#shorts") || title.contains("#tiktok");
    }
}
