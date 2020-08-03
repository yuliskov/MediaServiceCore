package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.YouTubeMediaTab;

import java.util.ArrayList;
import java.util.List;

public final class VideoServiceHelper {
    public static MediaTab convertBrowseResult(BrowseResult browseResult, int type) {
        MediaTab result = null;

        if (browseResult != null) {
            result = YouTubeMediaTab.from(browseResult, type);
        }

        return result;
    }

    public static MediaTab convertSearchResult(SearchResult searchResult, int type) {
        MediaTab result = null;

        if (searchResult != null) {
            result = YouTubeMediaTab.from(searchResult, type);
        }

        return result;
    }

    public static MediaTab convertNextBrowseResult(NextBrowseResult nextBrowseResult, MediaTab tab) {
        MediaTab result = null;

        if (nextBrowseResult != null) {
            result = YouTubeMediaTab.from(nextBrowseResult, tab);
        }

        return result;
    }

    public static MediaTab convertNextSearchResult(NextSearchResult nextSearchResult, MediaTab tab) {
        MediaTab result = null;

        if (nextSearchResult != null) {
            result = YouTubeMediaTab.from(nextSearchResult, tab);
        }

        return result;
    }

    public static List<MediaTab> convertBrowseSections(List<BrowseSection> sections) {
        List<MediaTab> result = new ArrayList<>();

        if (sections != null && sections.size() > 0) {
            for (BrowseSection section : sections) {
                result.add(YouTubeMediaTab.from(section));
            }
        }

        return result;
    }

    public static MediaTab convertBrowseSection(BrowseSection section) {
        return YouTubeMediaTab.from(section);
    }

    public static MediaTab convertTabs(List<MediaTab> mediaTabs, int type) {
        return YouTubeMediaTab.from(mediaTabs, type);
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

    public static String extractNextKey(MediaTab mediaTab) {
        String result = null;

        if (mediaTab instanceof YouTubeMediaTab) {
            result = ((YouTubeMediaTab) mediaTab).mNextPageKey;
        }

        return result;
    }
}
