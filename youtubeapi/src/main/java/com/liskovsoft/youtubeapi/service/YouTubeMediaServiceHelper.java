package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;

import java.util.List;

public final class YouTubeMediaServiceHelper {
    public static BrowseSection getSection(TabbedBrowseResult browseResult, int sectionIndex) {
        BrowseSection result = null;

        if (browseResult != null) {
            List<BrowseTab> browseTabs = browseResult.getBrowseTabs();

            if (browseTabs != null) {
                BrowseTab browseTab = browseTabs.get(0);

                if (browseTab != null) {
                    List<BrowseSection> sections = browseTab.getSections();

                    if (sections != null && sections.size() > sectionIndex) {
                        result = sections.get(sectionIndex);
                    }
                }
            }
        }

        return result;
    }

    public static String findHighResThumbnailUrl(com.liskovsoft.youtubeapi.common.models.items.VideoItem item) {
        List<Thumbnail> thumbnails = item.getThumbnails();

        return getHiResThumb(thumbnails);
    }

    public static String findHighResThumbnailUrl(com.liskovsoft.youtubeapi.common.models.items.MusicItem item) {
        List<Thumbnail> thumbnails = item.getThumbnails();

        return getHiResThumb(thumbnails);
    }

    public static String findHighResThumbnailUrl(com.liskovsoft.youtubeapi.common.models.items.ChannelItem item) {
        List<Thumbnail> thumbnails = item.getThumbnails();

        return getHiResThumb(thumbnails);
    }

    private static String getHiResThumb(List<Thumbnail> thumbnails) {
        if (thumbnails == null || thumbnails.size() == 0) {
            return null;
        }

        return thumbnails.get(thumbnails.size() - 1).getUrl();
    }

    public static String createDescription(com.liskovsoft.youtubeapi.common.models.items.VideoItem item) {
        return YouTubeHelper.itemsToDescription(
                item.getUserName(),
                item.getPublishedTime(),
                item.getShortViewCount()
        );
    }

    public static String createDescription(com.liskovsoft.youtubeapi.common.models.items.MusicItem item) {
        return YouTubeHelper.itemsToDescription(
                item.getUserName(),
                item.getViewsAndPublished()
        );
    }

    public static String createDescription(com.liskovsoft.youtubeapi.common.models.items.ChannelItem item) {
        return YouTubeHelper.itemsToDescription(
                item.getSubscriberCountText()
        );
    }

    public static String extractNextKey(MediaGroup mediaTab) {
        String result = null;

        if (mediaTab instanceof YouTubeMediaGroup) {
            result = ((YouTubeMediaGroup) mediaTab).mNextPageKey;
        }

        return result;
    }
}
