package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

import java.util.ArrayList;
import java.util.List;

public final class YouTubeMediaServiceHelper {
    public static MediaGroup convertBrowseResult(BrowseResult browseResult, int type) {
        MediaGroup result = null;

        if (browseResult != null) {
            result = YouTubeMediaGroup.from(browseResult, type);
        }

        return result;
    }

    public static MediaGroup convertSearchResult(SearchResult searchResult, int type) {
        MediaGroup result = null;

        if (searchResult != null) {
            result = YouTubeMediaGroup.from(searchResult, type);
        }

        return result;
    }

    public static MediaGroup convertNextBrowseResult(NextBrowseResult nextBrowseResult, MediaGroup tab) {
        MediaGroup result = null;

        if (nextBrowseResult != null) {
            result = YouTubeMediaGroup.from(nextBrowseResult, tab);
        }

        return result;
    }

    public static MediaGroup convertNextSearchResult(NextSearchResult nextSearchResult, MediaGroup tab) {
        MediaGroup result = null;

        if (nextSearchResult != null) {
            result = YouTubeMediaGroup.from(nextSearchResult, tab);
        }

        return result;
    }

    public static List<MediaGroup> convertBrowseSections(List<BrowseSection> sections) {
        List<MediaGroup> result = new ArrayList<>();

        if (sections != null && sections.size() > 0) {
            for (BrowseSection section : sections) {
                result.add(YouTubeMediaGroup.from(section));
            }
        }

        return result;
    }

    public static MediaGroup convertBrowseSection(BrowseSection section) {
        return YouTubeMediaGroup.from(section);
    }

    public static MediaGroup convertTabs(List<MediaGroup> mediaTabs, int type) {
        return YouTubeMediaGroup.from(mediaTabs, type);
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

    public static String extractNextKey(MediaGroup mediaTab) {
        String result = null;

        if (mediaTab instanceof YouTubeMediaGroup) {
            result = ((YouTubeMediaGroup) mediaTab).mNextPageKey;
        }

        return result;
    }
}
