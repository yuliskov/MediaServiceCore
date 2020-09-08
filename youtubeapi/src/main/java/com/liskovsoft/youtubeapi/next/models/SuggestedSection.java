package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class SuggestedSection {
    @JsonPath({"$.title.runs[0].text", "$.title.simpleText"})
    private String mTitle;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotVideoRenderer")
    private List<VideoItem> mVideoSuggestions;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotRadioRenderer")
    private List<RadioItem> mRadioSuggestions;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotPlaylistRenderer")
    private List<PlaylistItem> mPlaylistSuggestions;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotChannelRenderer")
    private List<ChannelItem> mChannelSuggestions;
    @JsonPath("$.content.horizontalListRenderer.continuations[*].nextContinuationData.continuation")
    private List<String> mNextPageKey;

    public String getTitle() {
        return mTitle;
    }

    public List<VideoItem> getVideoSuggestions() {
        return mVideoSuggestions;
    }

    public List<RadioItem> getRadioSuggestions() {
        return mRadioSuggestions;
    }

    public List<PlaylistItem> getPlaylistSuggestions() {
        return mPlaylistSuggestions;
    }

    public List<ChannelItem> getChannelSuggestions() {
        return mChannelSuggestions;
    }

    public String getNextPageKey() {
        if (mNextPageKey == null || mNextPageKey.isEmpty()) {
            return null;
        }

        return mNextPageKey.get(0);
    }
}
