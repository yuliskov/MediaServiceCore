package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TileItem;

public class ItemWrapper {
    @JsonPath("$.tileRenderer")
    private TileItem mTileItem;

    @JsonPath({"$.gridVideoRenderer",
               "$.pivotVideoRenderer", // suggested item
               "$.compactVideoRenderer"}) // history/search result item
    private VideoItem mVideoItem;

    @JsonPath("$.tvMusicVideoRenderer")
    private MusicItem mMusicItem;

    @JsonPath({"$.gridRadioRenderer",
               "$.pivotRadioRenderer",
               "$.compactRadioRenderer"})
    private RadioItem mRadioItem;

    @JsonPath({"$.gridChannelRenderer",
               "$.pivotChannelRenderer",
               "$.compactChannelRenderer"})
    private ChannelItem mChannelItem;

    @JsonPath({"$.gridPlaylistRenderer",
               "$.pivotPlaylistRenderer",
               "$.compactPlaylistRenderer"})
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

    public TileItem getTileItem() {
        return mTileItem;
    }

    public boolean isLive() {
        return mVideoItem != null ? mVideoItem.isLive() : mTileItem != null ? mTileItem.isLive() : false;
    }

    public boolean isUpcoming() {
        return mVideoItem != null ? mVideoItem.isUpcoming() : mTileItem != null ? mTileItem.isUpcoming() : false;
    }

    public boolean isMovie() {
        return  mVideoItem != null ? mVideoItem.isMovie() : mTileItem != null ? mTileItem.isMovie() : false;
    }
}
