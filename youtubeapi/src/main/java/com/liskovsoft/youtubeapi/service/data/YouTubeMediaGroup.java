package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.Chip;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.Section;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.v1.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.common.models.V2.TileItem;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroup implements MediaGroup {
    private final int mType;
    private String mTitle;
    private List<MediaItem> mMediaItems;
    public String mNextPageKey;
    private String mChannelUrl;
    private String mChannelId;
    private String mPlaylistParams;
    private String mReloadPageKey;

    public YouTubeMediaGroup(int type) {
        mType = type;
    }

    public static MediaGroup from(GridTab browseResult, int type) {
        if (browseResult == null) {
            return null;
        }

        return create(new YouTubeMediaGroup(type), browseResult.getItemWrappers(), browseResult.getNextPageKey());
    }

    public static MediaGroup from(GridTabContinuation continuation, String reloadPageKey, String groupTitle) {
        YouTubeMediaGroup baseGroup = new YouTubeMediaGroup(MediaGroup.TYPE_UNDEFINED);
        baseGroup.mReloadPageKey = reloadPageKey;
        MediaGroup mediaGroup = from(continuation, baseGroup);
        if (mediaGroup != null) {
            mediaGroup.setTitle(groupTitle);
        }
        return mediaGroup;
    }

    public static MediaGroup from(GridTabContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null || baseGroup == null) {
            return null;
        }

        // Subscribed channel view. Add details.
        if (continuation.getBrowseId() != null) {
            ((YouTubeMediaGroup) baseGroup).mChannelId = continuation.getBrowseId();
        }
        if (continuation.getParams() != null) {
            ((YouTubeMediaGroup) baseGroup).mPlaylistParams = continuation.getParams();
        }
        if (continuation.getCanonicalBaseUrl() != null) {
            ((YouTubeMediaGroup) baseGroup).mChannelUrl = continuation.getCanonicalBaseUrl();
        }

        return create((YouTubeMediaGroup) baseGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
    }

    public static MediaGroup from(WatchNextResultContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null) {
            return null;
        }

        return create((YouTubeMediaGroup) baseGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
    }

    public static MediaGroup from(Section section, int type) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(type);
        youTubeMediaGroup.mTitle = section.getTitle();
        youTubeMediaGroup.mNextPageKey = section.getNextPageKey();

        return create(youTubeMediaGroup, section.getItemWrappers(), section.getNextPageKey());
    }

    public static MediaGroup from(Chip chip, int type) {
        if (chip == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(type);
        youTubeMediaGroup.mTitle = chip.getTitle();
        youTubeMediaGroup.mNextPageKey = chip.getReloadPageKey();

        return create(youTubeMediaGroup, null, chip.getReloadPageKey());
    }

    public static MediaGroup from(SectionContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null) {
            return null;
        }

        return create((YouTubeMediaGroup) baseGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
    }

    public static MediaGroup from(SearchResult searchResult, int type) {
        if (searchResult == null) {
            return null;
        }

        return create(new YouTubeMediaGroup(type), searchResult.getItemWrappers(), searchResult.getNextPageKey());
    }

    public static MediaGroup from(SearchResultContinuation nextSearchResult, MediaGroup baseGroup) {
        if (nextSearchResult == null) {
            return null;
        }

        return create((YouTubeMediaGroup) baseGroup, nextSearchResult.getTileItems(), nextSearchResult.getVideoItems(), nextSearchResult.getMusicItems(),
                nextSearchResult.getChannelItems(), nextSearchResult.getRadioItems(), nextSearchResult.getPlaylistItems(), nextSearchResult.getNextPageKey());
    }

    public static MediaGroup from(Chip chip) {
        if (chip == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_SUGGESTIONS);
        youTubeMediaGroup.mTitle = chip.getTitle();

        return create(youTubeMediaGroup, chip.getItemWrappers(), chip.getNextPageKey());
    }

    public static MediaGroup from(SuggestedSection section) {
        if (section == null) {
            return null;
        }

        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_SUGGESTIONS);
        youTubeMediaGroup.mTitle = section.getTitle();

        return create(youTubeMediaGroup, section.getItemWrappers(), section.getNextPageKey());
    }

    public static List<MediaGroup> from(List<Section> sections, int type) {
        List<MediaGroup> result = new ArrayList<>();

        if (sections != null && sections.size() > 0) {
            for (Section section : sections) {
                // Section contains chips (nested sections) or items. Not both.
                if (section.getChips() != null) {
                    for (Chip chip : section.getChips()) {
                        result.add(YouTubeMediaGroup.from(chip, type));
                    }
                } else {
                    result.add(YouTubeMediaGroup.from(section, type));
                }
            }
        }

        return result;
    }

    public static MediaGroup fromTabs(List<GridTab> tabs, int type) {
        YouTubeMediaGroup youTubeMediaGroup = new YouTubeMediaGroup(type);

        return create(youTubeMediaGroup, tabs);
    }

    @Override
    public List<MediaItem> getMediaItems() {
        return mMediaItems;
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

    /**
     * TODO: create unique id by reload page key
     */
    @Override
    public int getId() {
        return mTitle != null ? mTitle.hashCode() : hashCode();
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public String getChannelUrl() {
        return mChannelUrl;
    }

    @Override
    public String getPlaylistParams() {
        return mPlaylistParams;
    }

    @Override
    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    @Override
    public boolean isEmpty() {
        return mMediaItems == null || mMediaItems.isEmpty();
    }

    private static MediaGroup create(YouTubeMediaGroup baseGroup, List<GridTab> tabs) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        if (tabs != null) {
            for (GridTab tab : tabs) {
                if (tab.isUnselectable()) {
                    continue;
                }

                YouTubeMediaItem item = YouTubeMediaItem.from(tab, baseGroup.getType());

                if (!item.isEmpty()) {
                    mediaItems.add(item);
                }
            }
        }

        // Fix duplicated items after previous group reuse
        baseGroup.mMediaItems = !mediaItems.isEmpty() ? mediaItems : null;

        return baseGroup;
    }

    private static MediaGroup create(YouTubeMediaGroup baseGroup, List<ItemWrapper> items, String nextPageKey) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                ItemWrapper item = items.get(i);
                YouTubeMediaItem mediaItem = YouTubeMediaItem.from(item, i);
                if (mediaItem != null) {
                    mediaItem.setPlaylistParams(baseGroup.mPlaylistParams);
                    mediaItems.add(mediaItem);
                }
            }
        }

        // Fix duplicated items after previous group reuse
        baseGroup.mMediaItems = !mediaItems.isEmpty() ? mediaItems : null;
        baseGroup.mNextPageKey = nextPageKey;

        return baseGroup;
    }

    private static YouTubeMediaGroup create(
            YouTubeMediaGroup baseGroup,
            List<TileItem> titleItems,
            List<VideoItem> videoItems,
            List<MusicItem> musicItems,
            List<ChannelItem> channelItems,
            List<RadioItem> radioItems,
            List<PlaylistItem> playlistItems,
            String nextPageKey) {

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        if (titleItems != null) {
            for (TileItem item : titleItems) {
                mediaItems.add(YouTubeMediaItem.from(item));
            }
        }

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

        // Fix duplicated items after previous group reuse
        baseGroup.mMediaItems = !mediaItems.isEmpty() ? mediaItems : null;
        baseGroup.mNextPageKey = nextPageKey;

        return baseGroup;
    }
}
