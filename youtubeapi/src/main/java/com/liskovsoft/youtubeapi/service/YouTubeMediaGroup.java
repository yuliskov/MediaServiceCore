package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroup implements MediaGroup {
    private List<MediaItem> mMediaItems;
    private String mTitle;
    public String mNextPageKey;
    private int mType = MediaGroup.TYPE_HOME;
    private List<MediaGroup> mNestedTabs;

    public static MediaGroup EMPTY_GROUP = new MediaGroup() {
        @Override
        public int getType() {
            return TYPE_UNDEFINED;
        }

        @Override
        public void setType(int type) {

        }

        @Override
        public List<MediaItem> getMediaItems() {
            return null;
        }

        @Override
        public void setMediaItems(List<MediaItem> tabs) {

        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public void setTitle(String title) {

        }
    };

    public static MediaGroup from(BrowseSection section) {
        YouTubeMediaGroup youTubeMediaTab = new YouTubeMediaGroup();

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : section.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        for (com.liskovsoft.youtubeapi.common.models.videos.MusicItem item : section.getMusicItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mTitle = section.getTitle();
        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = section.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaGroup from(NextBrowseResult nextBrowseResult, MediaGroup tab) {
        YouTubeMediaGroup youTubeMediaTab = (YouTubeMediaGroup) tab;

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : nextBrowseResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = nextBrowseResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaGroup from(NextSearchResult nextSearchResult, MediaGroup tab) {
        YouTubeMediaGroup youTubeMediaTab = (YouTubeMediaGroup) tab;

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : nextSearchResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = nextSearchResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaGroup from(BrowseResult browseResult, int type) {
        YouTubeMediaGroup youTubeMediaTab = new YouTubeMediaGroup();

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : browseResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mType = type;
        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = browseResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaGroup from(SearchResult searchResult, int type) {
        YouTubeMediaGroup youTubeMediaTab = new YouTubeMediaGroup();

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : searchResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mType = type;
        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = searchResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaGroup from(List<MediaGroup> tabs, int type) {
        YouTubeMediaGroup youTubeMediaTab = new YouTubeMediaGroup();

        youTubeMediaTab.mType = type;
        youTubeMediaTab.mNestedTabs = tabs;

        return youTubeMediaTab;
    }

    @Override
    public List<MediaItem> getMediaItems() {
        return mMediaItems;
    }

    @Override
    public void setMediaItems(List<MediaItem> items) {
        mMediaItems = items;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void setType(int type) {
        mType = type;
    }

    public List<MediaGroup> getNestedTabs() {
        return mNestedTabs;
    }

    public void setNestedTabs(List<MediaGroup> tabs) {
        mNestedTabs = tabs;
    }
}
