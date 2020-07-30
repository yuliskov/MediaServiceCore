package com.liskovsoft.youtubeapi.common;

import com.liskovsoft.videoserviceinterfaces.Video;
import com.liskovsoft.videoserviceinterfaces.VideoSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.service.YouTubeVideo;
import com.liskovsoft.youtubeapi.service.YouTubeVideoSection;

import java.util.ArrayList;
import java.util.List;

public final class VideoServiceHelper {
    public static List<Video> convertVideoItems(List<VideoItem> items) {
        ArrayList<Video> result = new ArrayList<>();

        if (items == null) {
            return result;
        }

        for (VideoItem item : items) {
            result.add(YouTubeVideo.from(item));
        }

        return result;
    }

    public static List<VideoSection> convertBrowseTabs(List<BrowseTab> browseTabs) {
        List<VideoSection> result = new ArrayList<>();

        if (browseTabs != null && browseTabs.size() > 0) {
            BrowseTab browseTab = browseTabs.get(0);
            List<BrowseSection> sections = browseTab.getSections();
            for (BrowseSection section : sections) {
                result.add(YouTubeVideoSection.from(section));
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
}
