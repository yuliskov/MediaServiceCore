package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class BrowseSection {
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mTitle;
    @JsonPath("$.content.horizontalListRenderer.items[*].gridVideoRenderer")
    private List<VideoItem> mVideoItems;
    @JsonPath("$.content.horizontalListRenderer.items[*].tvMusicVideoRenderer")
    private List<MusicItem> mMusicItems;
    @JsonPath("$.content.horizontalListRenderer.items[*].gridChannelRenderer")
    private List<ChannelItem> mChannelItems;
    @JsonPath("$.content.horizontalListRenderer.items[*].gridRadioRenderer")
    private List<PlaylistItem> mPlaylistItems;
    @JsonPath("$.content.horizontalListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public String getTitle() {
        return mTitle;
    }

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<MusicItem> getMusicItems() {
        return mMusicItems;
    }

    public List<ChannelItem> getChannelItems() {
        return mChannelItems;
    }

    public List<PlaylistItem> getPlaylistItems() {
        return mPlaylistItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
