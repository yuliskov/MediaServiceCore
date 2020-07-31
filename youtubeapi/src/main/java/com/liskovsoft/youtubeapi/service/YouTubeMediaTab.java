package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaTab implements MediaTab {
    private List<MediaItem> mVideos;
    private String mTitle;

    public static MediaTab from(BrowseSection section) {
        YouTubeMediaTab youTubeVideoSection = new YouTubeMediaTab();
        youTubeVideoSection.setTitle(section.getTitle());

        ArrayList<MediaItem> videos = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : section.getVideoItems()) {
            videos.add(YouTubeMediaItem.from(item));
        }

        for (com.liskovsoft.youtubeapi.common.models.videos.MusicItem item : section.getMusicItems()) {
            videos.add(YouTubeMediaItem.from(item));
        }

        youTubeVideoSection.setMediaItems(videos);

        return youTubeVideoSection;
    }

    @Override
    public List<MediaItem> getMediaItems() {
        return mVideos;
    }

    @Override
    public void setMediaItems(List<MediaItem> items) {
        mVideos = items;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }
}
