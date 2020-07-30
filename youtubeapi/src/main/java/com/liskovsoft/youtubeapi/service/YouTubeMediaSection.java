package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MusicItem;
import com.liskovsoft.mediaserviceinterfaces.VideoItem;
import com.liskovsoft.mediaserviceinterfaces.MediaSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaSection implements MediaSection {
    private List<VideoItem> mVideos;
    private List<MusicItem> mMusicVideos;
    private String mTitle;

    public static MediaSection from(BrowseSection section) {
        YouTubeMediaSection youTubeVideoSection = new YouTubeMediaSection();
        youTubeVideoSection.setTitle(section.getTitle());

        ArrayList<VideoItem> videos = new ArrayList<>();
        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : section.getVideoItems()) {
            videos.add(YouTubeVideoItem.from(item));
        }
        youTubeVideoSection.setVideoItems(videos);

        ArrayList<MusicItem> musics = new ArrayList<>();
        for (com.liskovsoft.youtubeapi.common.models.videos.MusicItem item : section.getMusicItems()) {
            musics.add(YouTubeMusicItem.from(item));
        }
        youTubeVideoSection.setMusicItems(musics);

        return youTubeVideoSection;
    }

    @Override
    public List<VideoItem> getVideoItems() {
        return mVideos;
    }

    @Override
    public void setVideoItems(List<VideoItem> items) {
        mVideos = items;
    }

    @Override
    public List<MusicItem> getMusicItems() {
        return mMusicVideos;
    }

    @Override
    public void setMusicItems(List<MusicItem> items) {
        mMusicVideos = items;
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
