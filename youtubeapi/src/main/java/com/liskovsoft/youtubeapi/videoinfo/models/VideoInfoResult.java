package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class VideoInfoResult {
    @JsonPath("$.streamingData.formats[*]")
    private List<AdaptiveFormat> mFormats;

    @JsonPath("$.streamingData.adaptiveFormats[*]")
    private List<AdaptiveFormat> mAdaptiveFormats;

    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    @JsonPath("$.playbackTracking.videostatsWatchtimeUrl.baseUrl")
    private String mVideostatsWatchtimeUrl;

    @JsonPath("$.captions.playerCaptionsTracklistRenderer.captionTracks[*]")
    private List<CaptionTrack> mCaptionTracks;

    @JsonPath("$.streamingData.dashManifestUrl")
    private String mDashManifestUrl;

    @JsonPath("$.videoDetails.lengthSeconds")
    private String mLengthSeconds;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mPlayerStoryboardSpec;

    public List<AdaptiveFormat> getAdaptiveFormats() {
        return mAdaptiveFormats;
    }

    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }

    public String getVideostatsWatchtimeUrl() {
        return mVideostatsWatchtimeUrl;
    }

    public List<CaptionTrack> getCaptionTracks() {
        return mCaptionTracks;
    }

    public String getDashManifestUrl() {
        return mDashManifestUrl;
    }

    public String getLengthSeconds() {
        return mLengthSeconds;
    }

    public String getPlayerStoryboardSpec() {
        return mPlayerStoryboardSpec;
    }

    public List<AdaptiveFormat> getFormats() {
        return mFormats;
    }
}
