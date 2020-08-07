package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaTab implements MediaTab {
    private List<MediaItem> mMediaItems;
    private String mTitle;
    public String mNextPageKey;
    private int mType = MediaTab.TYPE_HOME;
    private List<MediaTab> mNestedTabs;

    public static MediaTab EMPTY_TAB = new MediaTab() {
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

    public static MediaTab from(BrowseSection section) {
        YouTubeMediaTab youTubeMediaTab = new YouTubeMediaTab();

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

    public static MediaTab from(NextBrowseResult nextBrowseResult, MediaTab tab) {
        YouTubeMediaTab youTubeMediaTab = (YouTubeMediaTab) tab;

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : nextBrowseResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = nextBrowseResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaTab from(NextSearchResult nextSearchResult, MediaTab tab) {
        YouTubeMediaTab youTubeMediaTab = (YouTubeMediaTab) tab;

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : nextSearchResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = nextSearchResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaTab from(BrowseResult browseResult, int type) {
        YouTubeMediaTab youTubeMediaTab = new YouTubeMediaTab();

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : browseResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mType = type;
        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = browseResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaTab from(SearchResult searchResult, int type) {
        YouTubeMediaTab youTubeMediaTab = new YouTubeMediaTab();

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : searchResult.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaTab.mType = type;
        youTubeMediaTab.mMediaItems = mediaItems;
        youTubeMediaTab.mNextPageKey = searchResult.getNextPageKey();

        return youTubeMediaTab;
    }

    public static MediaTab from(List<MediaTab> tabs, int type) {
        YouTubeMediaTab youTubeMediaTab = new YouTubeMediaTab();

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

    public List<MediaTab> getNestedTabs() {
        return mNestedTabs;
    }

    public void setNestedTabs(List<MediaTab> tabs) {
        mNestedTabs = tabs;
    }
}
