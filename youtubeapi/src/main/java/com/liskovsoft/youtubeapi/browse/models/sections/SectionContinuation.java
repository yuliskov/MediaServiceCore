package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class SectionContinuation {
    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].gridVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].tvMusicVideoRenderer")
    private List<MusicItem> mMusicItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].gridRadioRenderer")
    private List<RadioItem> mRadioItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].gridChannelRenderer")
    private List<ChannelItem> mChannelItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].gridPlaylistRenderer")
    private List<PlaylistItem> mPlaylistItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<MusicItem> getMusicItems() {
        return mMusicItems;
    }

    public List<RadioItem> getRadioItems() {
        return mRadioItems;
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
