package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TitleItem;

public class ItemWrapper {
    @JsonPath("$.tileRenderer")
    private TitleItem mTitleItem;

    @JsonPath({"$.gridVideoRenderer",
               "$.pivotVideoRenderer"}) // suggested item
    private VideoItem mVideoItem;

    @JsonPath("$.tvMusicVideoRenderer")
    private MusicItem mMusicItem;

    @JsonPath({"$.gridRadioRenderer",
               "$.pivotRadioRenderer"})
    private RadioItem mRadioItem;

    @JsonPath({"$.gridChannelRenderer",
               "$.pivotChannelRenderer"})
    private ChannelItem mChannelItem;

    @JsonPath({"$.gridPlaylistRenderer",
               "$.pivotPlaylistRenderer"})
    private PlaylistItem mPlaylistItem;

    public VideoItem getVideoItem() {
        return mVideoItem;
    }

    public MusicItem getMusicItem() {
        return mMusicItem;
    }

    public RadioItem getRadioItem() {
        return mRadioItem;
    }

    public ChannelItem getChannelItem() {
        return mChannelItem;
    }

    public PlaylistItem getPlaylistItem() {
        return mPlaylistItem;
    }

    public TitleItem getTitleItem() {
        return mTitleItem;
    }
}
