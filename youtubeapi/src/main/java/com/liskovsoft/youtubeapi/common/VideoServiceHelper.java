package com.liskovsoft.youtubeapi.common;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;
import com.liskovsoft.youtubeapi.service.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.YouTubeMediaSection;
import com.liskovsoft.youtubeapi.support.utils.YouTubeHelper;

import java.util.ArrayList;
import java.util.List;

public final class VideoServiceHelper {
    public static List<MediaItem> convertVideoItems(List<com.liskovsoft.youtubeapi.common.models.videos.VideoItem> items) {
        ArrayList<MediaItem> result = new ArrayList<>();

        if (items == null) {
            return result;
        }

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : items) {
            result.add(YouTubeMediaItem.from(item));
        }

        return result;
    }

    public static List<MediaTab> convertBrowseTabs(List<BrowseTab> browseTabs) {
        List<MediaTab> result = new ArrayList<>();

        if (browseTabs != null && browseTabs.size() > 0) {
            BrowseTab browseTab = browseTabs.get(0);
            List<BrowseSection> sections = browseTab.getSections();
            for (BrowseSection section : sections) {
                result.add(YouTubeMediaSection.from(section));
            }
        }

        return result;
    }

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

    public static String obtainHighResThumbnailUrl(com.liskovsoft.youtubeapi.common.models.videos.VideoItem item) {
        List<Thumbnail> thumbnails = item.getThumbnails();

        if (thumbnails.size() == 0) {
            return null;
        }

        return thumbnails.get(thumbnails.size() - 1).getUrl();
    }

    public static String obtainHighResThumbnailUrl(com.liskovsoft.youtubeapi.common.models.videos.MusicItem item) {
        List<Thumbnail> thumbnails = item.getThumbnails();

        if (thumbnails.size() == 0) {
            return null;
        }

        return thumbnails.get(thumbnails.size() - 1).getUrl();
    }

    public static String obtainDescription(com.liskovsoft.youtubeapi.common.models.videos.VideoItem item) {
        return YouTubeHelper.itemsToDescription(
                item.getUserName(),
                item.getPublishedTime(),
                item.getShortViewCount()
        );
    }
}
