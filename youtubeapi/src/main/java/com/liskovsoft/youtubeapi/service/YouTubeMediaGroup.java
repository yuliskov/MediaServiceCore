package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroup implements MediaGroup {
    private List<MediaItem> mMediaItems;
    private String mTitle;
    public String mNextPageKey;
    private int mType = MediaGroup.TYPE_HOME;
    private List<MediaGroup> mNestedGroups;

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

        @Override
        public List<MediaGroup> getNestedGroups() {
            return null;
        }
    };

    public YouTubeMediaGroup() {
        
    }

    public YouTubeMediaGroup(int type) {
        mType = type;
    }

    public static MediaGroup from(BrowseSection section) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup();

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (com.liskovsoft.youtubeapi.common.models.videos.VideoItem item : section.getVideoItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        for (com.liskovsoft.youtubeapi.common.models.videos.MusicItem item : section.getMusicItems()) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaGroup.mTitle = section.getTitle();
        youTubeMediaGroup.mMediaItems = mediaItems;
        youTubeMediaGroup.mNextPageKey = section.getNextPageKey();

        return youTubeMediaGroup;
    }

    public static MediaGroup from(BrowseResultContinuation nextBrowseResult, MediaGroup baseGroup) {
        if (nextBrowseResult == null) {
            return null;
        }

        List<VideoItem> videoItems = nextBrowseResult.getVideoItems();
        String nextPageKey = nextBrowseResult.getNextPageKey();

        return create((YouTubeMediaGroup) baseGroup, videoItems, nextPageKey);
    }

    public static MediaGroup from(SearchResultContinuation nextSearchResult, MediaGroup baseGroup) {
        if (nextSearchResult == null) {
            return null;
        }

        List<VideoItem> videoItems = nextSearchResult.getVideoItems();
        String nextPageKey = nextSearchResult.getNextPageKey();

        return create((YouTubeMediaGroup) baseGroup, videoItems, nextPageKey);
    }

    public static MediaGroup from(BrowseResult browseResult, int type) {
        if (browseResult == null) {
            return null;
        }

        List<VideoItem> videoItems = browseResult.getVideoItems();
        String nextPageKey = browseResult.getNextPageKey();

        return create(new YouTubeMediaGroup(type), videoItems, nextPageKey);
    }

    public static MediaGroup from(SearchResult searchResult, int type) {
        if (searchResult == null) {
            return null;
        }

        List<VideoItem> videoItems = searchResult.getVideoItems();
        String nextPageKey = searchResult.getNextPageKey();

        return create(new YouTubeMediaGroup(type), videoItems, nextPageKey);
    }

    public static MediaGroup from(List<MediaGroup> groups, int type) {
        if (groups == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(type);

        youTubeMediaGroup.mNestedGroups = groups;

        return youTubeMediaGroup;
    }

    public static List<MediaGroup> from(List<BrowseSection> sections) {
        List<MediaGroup> result = new ArrayList<>();

        if (sections != null && sections.size() > 0) {
            for (BrowseSection section : sections) {
                result.add(YouTubeMediaGroup.from(section));
            }
        }

        return result;
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

    @Override
    public List<MediaGroup> getNestedGroups() {
        return mNestedGroups;
    }

    public void setNestedGroups(List<MediaGroup> tabs) {
        mNestedGroups = tabs;
    }

    private static YouTubeMediaGroup create(YouTubeMediaGroup baseGroup, List<VideoItem> videoItems, String nextPageKey) {
        YouTubeMediaGroup youTubeMediaGroup = baseGroup;

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (VideoItem item : videoItems) {
            mediaItems.add(YouTubeMediaItem.from(item));
        }

        youTubeMediaGroup.mMediaItems = mediaItems;
        youTubeMediaGroup.mNextPageKey = nextPageKey;

        return youTubeMediaGroup;
    }
}
