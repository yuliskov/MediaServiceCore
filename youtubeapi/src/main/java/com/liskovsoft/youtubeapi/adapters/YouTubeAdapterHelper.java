package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;

import java.util.ArrayList;
import java.util.List;

final class YouTubeAdapterHelper {
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
}
