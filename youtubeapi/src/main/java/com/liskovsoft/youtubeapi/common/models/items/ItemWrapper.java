package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class ItemWrapper {
    @JsonPath("$.gridVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.tvMusicVideoRenderer")
    private List<MusicItem> mMusicItems;

    @JsonPath("$.gridRadioRenderer")
    private List<RadioItem> mRadioItems;

    @JsonPath("$.gridChannelRenderer")
    private List<ChannelItem> mChannelItems;

    @JsonPath("$.gridPlaylistRenderer")
    private List<PlaylistItem> mPlaylistItems;
}
