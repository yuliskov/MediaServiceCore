package com.liskovsoft.youtubeapi.browse.ver1.models.sections;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
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
    private List<RadioItem> mRadioItems;
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

    public List<RadioItem> getRadioItems() {
        return mRadioItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
