package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.TabbedBrowseResult;
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

    public static String findHighResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null || thumbnails.size() == 0) {
            return null;
        }

        return thumbnails.get(thumbnails.size() - 1).getUrl();
    }

    public static String createDescription(String... items) {
        return YouTubeHelper.itemsToDescription(items);
    }

    public static String extractNextKey(MediaGroup mediaTab) {
        String result = null;

        if (mediaTab instanceof YouTubeMediaGroup) {
            result = ((YouTubeMediaGroup) mediaTab).mNextPageKey;
        }

        return result;
    }
}
