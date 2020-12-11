package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.List;

public class VideoInfo {
    private static final String PARAM_EVENT_ID = "ei";
    private static final String PARAM_VM = "vm";
    private static final String STATUS_UNPLAYABLE = "UNPLAYABLE";
    private static final String STATUS_LOGIN_REQUIRED = "LOGIN_REQUIRED";
    private static final String STATUS_AGE_CHECK_REQUIRED = "AGE_CHECK_REQUIRED";

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

    @JsonPath("$.playabilityStatus.status")
    private String mPlayabilityStatus;

    @JsonPath("$.playabilityStatus.reason")
    private String mPlayabilityReason;

    @JsonPath("$.playabilityStatus.errorScreen.playerErrorMessageRenderer.subreason.runs[0].text")
    private String mPlayabilityDescription;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mStoryboardSpec;

    // Values used in tracking actions
    private String mEventId;
    private String mVisitorMonitoringData;

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

    public String getVisitorMonitoringData() {
        parseTrackingParams();

        return mVisitorMonitoringData;
    }

    public String getPlaybackUrl() {
        return mPlaybackUrl;
    }

    public String getWatchTimeUrl() {
        return mWatchTimeUrl;
    }

    /**
     * Video cannot be embedded
     */
    public boolean isUnplayable() {
        return STATUS_UNPLAYABLE.equals(mPlayabilityStatus);
    }

    public String getPlayabilityStatus() {
        return AppHelper.itemsToDescription(mPlayabilityReason, mPlayabilityDescription);
    }

    /**
     * Age restricted video
     */
    public boolean isLoginRequired() {
        return STATUS_LOGIN_REQUIRED.equals(mPlayabilityStatus) ||
               STATUS_AGE_CHECK_REQUIRED.equals(mPlayabilityStatus);
    }

    public String getStoryboardSpec() {
        return mStoryboardSpec;
    }

    public boolean isHfr() {
        return mDashManifestUrl != null && mDashManifestUrl.contains("hfr");
    }

    private void parseTrackingParams() {
        boolean parseDone = mEventId != null || mVisitorMonitoringData != null;

        if (!parseDone && mWatchTimeUrl != null) {
            UrlQueryString queryString = UrlQueryStringFactory.parse(mWatchTimeUrl);

            mEventId = queryString.get(PARAM_EVENT_ID);
            mVisitorMonitoringData = queryString.get(PARAM_VM);
        }
    }
}
