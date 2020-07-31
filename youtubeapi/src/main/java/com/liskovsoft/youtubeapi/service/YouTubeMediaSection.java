package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaSection implements MediaSection {
    private List<MediaItem> mVideos;
    private String mTitle;

    public static MediaSection from(BrowseSection section) {
        YouTubeMediaSection youTubeVideoSection = new YouTubeMediaSection();
        youTubeVideoSection.setTitle(section.getTitle());

        ArrayList<MediaItem> videos = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : section.getVideoItems()) {
            videos.add(YouTubeMediaItem.from(item));
        }

        for (com.liskovsoft.youtubeapi.common.models.videos.MusicItem item : section.getMusicItems()) {
            videos.add(YouTubeMediaItem.from(item));
        }

        youTubeVideoSection.setVideoItems(videos);

        return youTubeVideoSection;
    }

    @Override
    public List<MediaItem> getVideoItems() {
        return mVideos;
    }

    @Override
    public void setVideoItems(List<MediaItem> items) {
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
