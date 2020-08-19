package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.List;

public class VideoInfoResult {
    private static final String PARAM_EVENT_ID = "ei";
    private static final String PARAM_VM = "vm";

    @JsonPath("$.streamingData.formats[*]")
    private List<RegularVideoFormat> mRegularFormats;

    @JsonPath("$.streamingData.adaptiveFormats[*]")
    private List<AdaptiveVideoFormat> mAdaptiveFormats;

    @JsonPath("$.captions.playerCaptionsTracklistRenderer.captionTracks[*]")
    private List<CaptionTrack> mCaptionTracks;

    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    @JsonPath("$.playbackTracking.videostatsWatchtimeUrl.baseUrl")
    private String mVideoStatsWatchTimeUrl;

    @JsonPath("$.streamingData.dashManifestUrl")
    private String mDashManifestUrl;

    @JsonPath("$.videoDetails")
    private VideoDetails mVideoDetails;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mPlayerStoryboardSpec;

    @JsonPath("$.playbackTracking.videostatsPlaybackUrl.baseUrl")
    private String mPlaybackUrl;

    @JsonPath("$.playbackTracking.videostatsWatchtimeUrl.baseUrl")
    private String mWatchTimeUrl;

    // Values used in tracking actions
    private String mEventId;
    private String mVM;

    public List<AdaptiveVideoFormat> getAdaptiveFormats() {
        return mAdaptiveFormats;
    }

    public List<RegularVideoFormat> getRegularFormats() {
        return mRegularFormats;
    }

    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }

    public String getVideoStatsWatchTimeUrl() {
        return mVideoStatsWatchTimeUrl;
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

    public VideoDetails getVideoDetails() {
        return mVideoDetails;
    }

    public String getEventId() {
        parseTrackingParams();

        return mEventId;
    }

    public String getVM() {
        parseTrackingParams();

        return mVM;
    }

    public String getPlaybackUrl() {
        return mPlaybackUrl;
    }

    public String getWatchTimeUrl() {
        return mWatchTimeUrl;
    }

    private void parseTrackingParams() {
        boolean parseDone = mEventId != null || mVM != null;

        if (!parseDone && mWatchTimeUrl != null) {
            UrlQueryString queryString = UrlQueryStringFactory.parse(mWatchTimeUrl);

            mEventId = queryString.get(PARAM_EVENT_ID);
            mVM = queryString.get(PARAM_VM);
        }
    }
}
