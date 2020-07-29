package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class YouTubeVideoSection implements VideoSection {
    private List<Video> mVideos;
    private String mTitle;

    public static VideoSection from(BrowseSection section) {
        YouTubeVideoSection youTubeVideoSection = new YouTubeVideoSection();
        youTubeVideoSection.setTitle(section.getTitle());
        ArrayList<Video> videos = new ArrayList<>();
        for (VideoItem item : section.getVideoItems()) {
            videos.add(YouTubeVideo.from(item));
        }
        youTubeVideoSection.setVideos(videos);
        return youTubeVideoSection;
    }

    @Override
    public List<Video> getVideos() {
        return mVideos;
    }

    @Override
    public void setVideos(List<Video> videos) {
        mVideos = videos;
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
