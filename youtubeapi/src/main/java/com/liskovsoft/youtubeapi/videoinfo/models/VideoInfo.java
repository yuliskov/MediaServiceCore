package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.models.V2.TextItem;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.List;

public class VideoInfo {
    private static final String PARAM_EVENT_ID = "ei";
    private static final String PARAM_VM = "vm";
    private static final String STATUS_UNPLAYABLE = "UNPLAYABLE";
    private static final String STATUS_ERROR = "ERROR";
    private static final String STATUS_OFFLINE = "LIVE_STREAM_OFFLINE";
    private static final String STATUS_LOGIN_REQUIRED = "LOGIN_REQUIRED";
    private static final String STATUS_AGE_CHECK_REQUIRED = "AGE_CHECK_REQUIRED";
    private static final String STATUS_CONTENT_CHECK_REQUIRED = "CONTENT_CHECK_REQUIRED";

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

    @JsonPath("$.playabilityStatus.errorScreen.playerErrorMessageRenderer.subreason")
    private TextItem mPlayabilityDescription;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mStoryboardSpec;

    @JsonPath("$.playabilityStatus.errorScreen.playerLegacyDesktopYpcTrailerRenderer.trailerVideoId")
    private String mTrailerVideoId;

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

    /**
     * Intended to merge signed and unsigned infos (no-playback fix)
     */
    public void setEventId(String eventId) {
        mEventId = eventId;
    }

    public String getVisitorMonitoringData() {
        parseTrackingParams();

        return mVisitorMonitoringData;
    }

    /**
     * Intended to merge signed and unsigned infos (no-playback fix)
     */
    public void setVisitorMonitoringData(String visitorMonitoringData) {
        mVisitorMonitoringData = visitorMonitoringData;
    }

    public String getPlaybackUrl() {
        return mPlaybackUrl;
    }

    public String getWatchTimeUrl() {
        return mWatchTimeUrl;
    }

    public boolean isRent() {
        return isUnplayable() && getTrailerVideoId() != null;
    }

    public boolean isUnplayable() {
        return isEmbedRestricted() || isAgeRestricted();
    }

    /**
     * Video cannot be embedded
     */
    public boolean isEmbedRestricted() {
        return ServiceHelper.atLeastOneEquals(mPlayabilityStatus, STATUS_UNPLAYABLE, STATUS_ERROR);
    }

    /**
     * Age restricted video
     */
    public boolean isAgeRestricted() {
        return ServiceHelper.atLeastOneEquals(mPlayabilityStatus, STATUS_LOGIN_REQUIRED, STATUS_AGE_CHECK_REQUIRED, STATUS_CONTENT_CHECK_REQUIRED);
    }

    public String getPlayabilityStatus() {
        return ServiceHelper.itemsToInfo(mPlayabilityReason, mPlayabilityDescription);
    }

    public String getStoryboardSpec() {
        return mStoryboardSpec;
    }

    public String getTrailerVideoId() {
        return mTrailerVideoId;
    }

    public boolean isHfr() {
        return mDashManifestUrl != null && mDashManifestUrl.contains("/hfr/all");
    }

    public boolean isValid() {
        if (STATUS_OFFLINE.equals(mPlayabilityStatus)) {
            return true;
        }

        // Check that history data is present
        return getEventId() != null && getVisitorMonitoringData() != null;
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
