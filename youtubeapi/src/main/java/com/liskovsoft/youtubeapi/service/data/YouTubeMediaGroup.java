package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.models.SuggestedSection;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroup implements MediaGroup {
    private List<MediaItem> mMediaItems;
    private String mTitle;
    public String mNextPageKey;
    private int mType = MediaGroup.TYPE_HOME;

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

        if (section.getVideoItems() != null) {
            for (com.liskovsoft.youtubeapi.common.models.items.VideoItem item : section.getVideoItems()) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (section.getMusicItems() != null) {
            for (com.liskovsoft.youtubeapi.common.models.items.MusicItem item : section.getMusicItems()) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (section.getChannelItems() != null) {
            for (com.liskovsoft.youtubeapi.common.models.items.ChannelItem item : section.getChannelItems()) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (section.getRadioItems() != null) {
            for (RadioItem item : section.getRadioItems()) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        youTubeMediaGroup.mTitle = section.getTitle();
        youTubeMediaGroup.mMediaItems = mediaItems.isEmpty() ? null : mediaItems;
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
        List<MusicItem> musicItems = searchResult.getMusicItems();
        List<ChannelItem> channelItems = searchResult.getChannelItems();
        List<RadioItem> playlistItems = searchResult.getPlaylistItems();
        String nextPageKey = searchResult.getNextPageKey();

        return create(new YouTubeMediaGroup(type), videoItems, musicItems, channelItems, playlistItems, nextPageKey);
    }

    public static MediaGroup from(SuggestedSection section) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup();

        youTubeMediaGroup.mMediaItems = new ArrayList<>();

        if (section.getChannelSuggestions() != null) {
            for (ChannelItem item : section.getChannelSuggestions()) {
                youTubeMediaGroup.mMediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (section.getVideoSuggestions() != null) {
            for (VideoItem item : section.getVideoSuggestions()) {
                youTubeMediaGroup.mMediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (section.getRadioSuggestions() != null) {
            for (RadioItem item : section.getRadioSuggestions()) {
                youTubeMediaGroup.mMediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (section.getPlaylistSuggestions() != null) {
            for (PlaylistItem item : section.getPlaylistSuggestions()) {
                youTubeMediaGroup.mMediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (youTubeMediaGroup.mMediaItems.isEmpty()) {
            youTubeMediaGroup.mMediaItems = null;
        }

        youTubeMediaGroup.mTitle = section.getTitle();
        youTubeMediaGroup.mNextPageKey = section.getNextPageKey();

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

    private static YouTubeMediaGroup create(YouTubeMediaGroup baseGroup, List<VideoItem> videoItems, String nextPageKey) {
        return create(baseGroup, videoItems, null, null, null, nextPageKey);
    }

    private static YouTubeMediaGroup create(
            YouTubeMediaGroup baseGroup,
            List<VideoItem> videoItems,
            List<MusicItem> musicItems,
            List<ChannelItem> channelItems,
            List<RadioItem> playlistItems,
            String nextPageKey) {
        YouTubeMediaGroup youTubeMediaGroup = baseGroup;

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        if (videoItems != null) {
            for (VideoItem item : videoItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (musicItems != null) {
            for (MusicItem item : musicItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (channelItems != null) {
            for (ChannelItem item : channelItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (playlistItems != null) {
            for (RadioItem item : playlistItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        youTubeMediaGroup.mMediaItems = mediaItems;
        youTubeMediaGroup.mNextPageKey = nextPageKey;

        return youTubeMediaGroup;
    }
}
