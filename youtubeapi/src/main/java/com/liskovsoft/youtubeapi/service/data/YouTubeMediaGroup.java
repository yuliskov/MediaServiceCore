package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.browse.old.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.old.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.Section;
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

    public static MediaGroup from(com.liskovsoft.youtubeapi.browse.old.models.sections.BrowseSection section) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup();
        youTubeMediaGroup.mTitle = section.getTitle();
        youTubeMediaGroup.mNextPageKey = section.getNextPageKey();

        return create(youTubeMediaGroup, section.getVideoItems(), section.getMusicItems(), section.getChannelItems(),
                section.getRadioItems(), null, section.getNextPageKey());
    }

    public static MediaGroup from(Section section, int type) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(type);
        youTubeMediaGroup.mTitle = section.getTitle();
        youTubeMediaGroup.mNextPageKey = section.getNextPageKey();

        return create(youTubeMediaGroup, section.getVideoItems(), section.getMusicItems(), section.getChannelItems(),
                section.getRadioItems(), null, section.getNextPageKey());
    }

    public static MediaGroup from(BrowseResultContinuation nextBrowseResult, MediaGroup baseGroup) {
        if (nextBrowseResult == null) {
            return null;
        }

        List<VideoItem> videoItems = nextBrowseResult.getVideoItems();
        String nextPageKey = nextBrowseResult.getNextPageKey();

        return create((YouTubeMediaGroup) baseGroup, videoItems, nextPageKey);
    }

    public static MediaGroup from(SectionContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null) {
            return null;
        }

        List<VideoItem> videoItems = continuation.getVideoItems();
        String nextPageKey = continuation.getNextPageKey();

        return create((YouTubeMediaGroup) baseGroup, videoItems, nextPageKey);
    }

    public static MediaGroup from(GridTabContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null) {
            return null;
        }

        List<VideoItem> videoItems = continuation.getVideoItems();
        String nextPageKey = continuation.getNextPageKey();

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

    public static MediaGroup from(GridTab browseResult, int type) {
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
        List<RadioItem> radioItems = searchResult.getRadioItems();
        List<PlaylistItem> playlistItems = searchResult.getPlaylistItems();
        String nextPageKey = searchResult.getNextPageKey();

        return create(new YouTubeMediaGroup(type), videoItems, musicItems, channelItems, radioItems, playlistItems, nextPageKey);
    }

    public static MediaGroup from(SuggestedSection section) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_SUGGESTIONS);
        youTubeMediaGroup.mTitle = section.getTitle();

        return create(youTubeMediaGroup, section.getVideoSuggestions(), null, section.getChannelSuggestions(),
                section.getRadioSuggestions(), section.getPlaylistSuggestions(), section.getNextPageKey());
    }

    public static List<MediaGroup> from(List<com.liskovsoft.youtubeapi.browse.old.models.sections.BrowseSection> sections) {
        List<MediaGroup> result = new ArrayList<>();

        if (sections != null && sections.size() > 0) {
            for (com.liskovsoft.youtubeapi.browse.old.models.sections.BrowseSection section : sections) {
                result.add(YouTubeMediaGroup.from(section));
            }
        }

        return result;
    }

    public static List<MediaGroup> from(List<Section> sections, int type) {
        List<MediaGroup> result = new ArrayList<>();

        if (sections != null && sections.size() > 0) {
            for (Section section : sections) {
                result.add(YouTubeMediaGroup.from(section, type));
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
        return create(baseGroup, videoItems, null, null, null, null, nextPageKey);
    }

    private static YouTubeMediaGroup create(
            YouTubeMediaGroup baseGroup,
            List<VideoItem> videoItems,
            List<MusicItem> musicItems,
            List<ChannelItem> channelItems,
            List<RadioItem> radioItems,
            List<PlaylistItem> playlistItems,
            String nextPageKey) {

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        if (channelItems != null) {
            for (ChannelItem item : channelItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

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

        if (radioItems != null) {
            for (RadioItem item : radioItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (playlistItems != null) {
            for (PlaylistItem item : playlistItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

        if (!mediaItems.isEmpty()) {
            baseGroup.mMediaItems = mediaItems;
        }

        baseGroup.mNextPageKey = nextPageKey;

        return baseGroup;
    }
}
