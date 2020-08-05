package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.List;

public class VideoInfoResult {
    @JsonPath("$.streamingData.formats[*]")
    private List<RegularVideoFormat> mRegularFormats;

    @JsonPath("$.streamingData.adaptiveFormats[*]")
    private List<AdaptiveVideoFormat> mAdaptiveFormats;

    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    @JsonPath("$.playbackTracking.videostatsWatchtimeUrl.baseUrl")
    private String mVideostatsWatchtimeUrl;

    @JsonPath("$.captions.playerCaptionsTracklistRenderer.captionTracks[*]")
    private List<CaptionTrack> mCaptionTracks;

    @JsonPath("$.streamingData.dashManifestUrl")
    private String mDashManifestUrl;

    @JsonPath("$.videoDetails")
    private VideoDetails mVideoDetails;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mPlayerStoryboardSpec;

    public List<AdaptiveVideoFormat> getAdaptiveFormats() {
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

    public String getPlayerStoryboardSpec() {
        return mPlayerStoryboardSpec;
    }

    public List<RegularVideoFormat> getRegularFormats() {
        return mRegularFormats;
    }

    public VideoDetails getVideoDetails() {
        return mVideoDetails;
    }
}
