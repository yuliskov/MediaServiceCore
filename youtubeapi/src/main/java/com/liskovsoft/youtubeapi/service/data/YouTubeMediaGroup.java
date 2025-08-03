package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.Chip;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.Section;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.next.v1.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchSection;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroup implements MediaGroup {
    private final int mType;
    private String mTitle;
    private List<MediaItem> mMediaItems;
    public String mNextPageKey;
    private String mChannelUrl;
    private String mChannelId;
    private String mParams;
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

    public static MediaGroup from(GridTabContinuation continuation, String reloadPageKey, String groupTitle, int groupType) {
        YouTubeMediaGroup baseGroup = new YouTubeMediaGroup(groupType);
        baseGroup.mReloadPageKey = reloadPageKey;
        MediaGroup mediaGroup = from(continuation, baseGroup);
        if (mediaGroup instanceof YouTubeMediaGroup) {
            ((YouTubeMediaGroup) mediaGroup).setTitle(groupTitle);
        }
        return mediaGroup;
    }

    public static MediaGroup from(GridTabContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null || baseGroup == null) {
            return null;
        }

        YouTubeMediaGroup newGroup = new YouTubeMediaGroup(baseGroup.getType());
        newGroup.mTitle = baseGroup.getTitle();

        // Subscribed channel view. Add details.
        if (continuation.getBrowseId() != null) {
            newGroup.mChannelId = continuation.getBrowseId();
        }
        if (continuation.getParams() != null) {
            newGroup.mParams = continuation.getParams();
        }
        if (continuation.getCanonicalBaseUrl() != null) {
            newGroup.mChannelUrl = continuation.getCanonicalBaseUrl();
        }

        return create(newGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
    }

    public static MediaGroup from(WatchNextResultContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null) {
            return null;
        }

        YouTubeMediaGroup newGroup = new YouTubeMediaGroup(baseGroup.getType());
        newGroup.mTitle = baseGroup.getTitle();

        return create(newGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
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
        if (continuation == null || baseGroup == null) {
            return null;
        }

        YouTubeMediaGroup newGroup = new YouTubeMediaGroup(baseGroup.getType());
        newGroup.mTitle = baseGroup.getTitle();

        return create(newGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
    }

    public static List<MediaGroup> from(SearchResult searchResult, int type) {
        if (searchResult == null || searchResult.getSections() == null || searchResult.getSections().size() == 0) {
            return null;
        }

        List<MediaGroup> result = new ArrayList<>();

        for (SearchSection section : searchResult.getSections()) {
            result.add(from(section, type));
        }

        return result;
    }

    public static MediaGroup from(SearchSection searchSection, int type) {
        if (searchSection == null) {
            return null;
        }

        YouTubeMediaGroup baseGroup = new YouTubeMediaGroup(type);
        baseGroup.setTitle(searchSection.getTitle());

        return create(baseGroup, searchSection.getItemWrappers(), searchSection.getNextPageKey());
    }

    public static MediaGroup from(SearchResultContinuation continuation, MediaGroup baseGroup) {
        if (continuation == null || baseGroup == null) {
            return null;
        }

        YouTubeMediaGroup newGroup = new YouTubeMediaGroup(baseGroup.getType());
        newGroup.mTitle = baseGroup.getTitle();

        return create(newGroup, continuation.getItemWrappers(), continuation.getNextPageKey());
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

    public void setMediaItems(List<MediaItem> items) {
        mMediaItems = items;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public int getType() {
        return mType;
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
    public String getParams() {
        return mParams;
    }

    @Override
    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    @Override
    public String getNextPageKey() {
        return mNextPageKey;
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

                mediaItems.add(item);
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
                    mediaItem.setParams(baseGroup.mParams);
                    mediaItems.add(mediaItem);
                }
            }
        }

        // Fix duplicated items after previous group reuse
        baseGroup.mMediaItems = !mediaItems.isEmpty() ? mediaItems : null;
        baseGroup.mNextPageKey = nextPageKey;

        YouTubeHelper.filterIfNeeded(baseGroup);

        return baseGroup;
    }
}
