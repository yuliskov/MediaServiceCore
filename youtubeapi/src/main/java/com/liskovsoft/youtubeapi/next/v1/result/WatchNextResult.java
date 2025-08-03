package com.liskovsoft.youtubeapi.next.v1.result;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.v1.models.ButtonStates;
import com.liskovsoft.youtubeapi.next.v1.models.VideoMetadata;
import com.liskovsoft.youtubeapi.next.v1.models.NextVideo;
import com.liskovsoft.youtubeapi.next.v1.models.Playlist;
import com.liskovsoft.youtubeapi.next.v1.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.v1.models.VideoOwner;

import java.util.List;

public class WatchNextResult {
    /**
     * Sections == Rows in web app version
     */
    @JsonPath({"$.contents.singleColumnWatchNextResults.pivot.pivot.contents[*].shelfRenderer",
            "$.contents.singleColumnWatchNextResults.pivot.sectionListRenderer.contents[*].shelfRenderer"})
    private List<SuggestedSection> mSuggestedSections;
    @JsonPath({"$.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].videoMetadataRenderer",
               "$.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].musicWatchMetadataRenderer"}) // youtube music format
    private VideoMetadata mVideoMetadata;
    @JsonPath("$.contents.singleColumnWatchNextResults.results.results.contents[0].itemSectionRenderer.contents[0].videoMetadataRenderer.owner.videoOwnerRenderer")
    private VideoOwner mVideoOwner;
    @JsonPath({"$.contents.singleColumnWatchNextResults.autoplay.autoplay.sets[0].nextVideoRenderer.maybeHistoryEndpointRenderer",
               "$.contents.singleColumnWatchNextResults.autoplay.autoplay.sets[0].nextVideoRenderer.autoplayEndpointRenderer"}) // present only on playlist
    private NextVideo mNextVideo;
    @JsonPath("$.contents.singleColumnWatchNextResults.autoplay.autoplay.sets.replayVideoRenderer.pivotVideoRenderer") // V2
    private VideoItem mVideoDetails;
    @JsonPath("$.contents.singleColumnWatchNextResults.playlist.playlist")
    private Playlist mPlaylist;
    @JsonPath("$.contents.singleColumnWatchNextResults.autoplay.autoplay.replayVideoRenderer")
    private ItemWrapper mReplayItem;
    @JsonPath("$.transportControls.transportControlsRenderer")
    private ButtonStates mButtonStates;

    public List<SuggestedSection> getSuggestedSections() {
        return mSuggestedSections;
    }

    public VideoMetadata getVideoMetadata() {
        return mVideoMetadata;
    }

    public VideoOwner getVideoOwner() {
        return mVideoOwner;
    }

    public NextVideo getNextVideo() {
        return mNextVideo;
    }

    public Playlist getPlaylist() {
        return mPlaylist;
    }

    /**
     * Contains same info as video item<br/>
     * Used as the mark that next data is correct (other items might be empty).
     */
    public ItemWrapper getReplayItem() {
        return mReplayItem;
    }

    public ButtonStates getButtonStates() {
        return mButtonStates;
    }

    public VideoItem getVideoDetails() {
        return mVideoDetails;
    }
}
