package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import com.liskovsoft.googlecommon.common.models.V2.TextItem;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.List;
import java.util.regex.Pattern;

public class VideoInfo {
    private static final String PARAM_EVENT_ID = "ei";
    private static final String PARAM_VM = "vm";
    private static final String PARAM_OF = "of";
    private static final String STATUS_OK = "OK";
    private static final String STATUS_UNPLAYABLE = "UNPLAYABLE";
    private static final String STATUS_ERROR = "ERROR";
    private static final String STATUS_OFFLINE = "LIVE_STREAM_OFFLINE";
    private static final String STATUS_LOGIN_REQUIRED = "LOGIN_REQUIRED";
    private static final String STATUS_AGE_CHECK_REQUIRED = "AGE_CHECK_REQUIRED";
    private static final String STATUS_AGE_VERIFICATION_REQUIRED = "AGE_VERIFICATION_REQUIRED";
    private static final String STATUS_CONTENT_CHECK_REQUIRED = "CONTENT_CHECK_REQUIRED";
    private static final Pattern tagPattern = Pattern.compile("\\(.*\\)$");

    @JsonPath("$.streamingData.formats[*]")
    private List<RegularVideoFormat> mRegularFormats;

    @JsonPath("$.streamingData.adaptiveFormats[*]")
    private List<AdaptiveVideoFormat> mAdaptiveFormats;

    //@JsonPath("$.playabilityStatus.paygatedQualitiesMetadata.restrictedAdaptiveFormats[*]")
    private List<AdaptiveVideoFormat> mRestrictedFormats;

    @JsonPath("$.captions.playerCaptionsTracklistRenderer.captionTracks[*]")
    private List<CaptionTrack> mCaptionTracks;

    @JsonPath("$.captions.playerCaptionsTracklistRenderer.translationLanguages[*]")
    private List<TranslationLanguage> mTranslationLanguages;

    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    @JsonPath("$.playbackTracking.videostatsWatchtimeUrl.baseUrl")
    private String mVideoStatsWatchTimeUrl;

    @JsonPath("$.streamingData.dashManifestUrl")
    private String mDashManifestUrl;

    @JsonPath("$.videoDetails")
    private VideoDetails mVideoDetails;

    @JsonPath("$.playbackTracking.videostatsPlaybackUrl.baseUrl")
    private String mPlaybackUrl;

    @JsonPath("$.playbackTracking.videostatsWatchtimeUrl.baseUrl")
    private String mWatchTimeUrl;

    @JsonPath("$.playabilityStatus.status")
    private String mPlayabilityStatus;

    @JsonPath("$.playabilityStatus.reason")
    private String mPlayabilityReason;

    @JsonPath("$.playabilityStatus.playableInEmbed")
    private boolean mIsPlayableInEmbed;

    @JsonPath("$.playabilityStatus.errorScreen.playerErrorMessageRenderer.subreason")
    private TextItem mPlayabilityDescription;

    @JsonPath({"$.storyboards.playerStoryboardSpecRenderer.spec",
               "$.storyboards.playerLiveStoryboardSpecRenderer.spec",
    })
    private String mStoryboardSpec;

    @JsonPath("$.playabilityStatus.errorScreen.playerLegacyDesktopYpcTrailerRenderer.trailerVideoId")
    private String mTrailerVideoId;

    @JsonPath("$.microformat.playerMicroformatRenderer.liveBroadcastDetails.startTimestamp")
    private String mStartTimestamp;

    @JsonPath("$.microformat.playerMicroformatRenderer.uploadDate")
    private String mUploadDate;

    @JsonPath("$.playerConfig.audioConfig.loudnessDb")
    private float mLoudnessDb;

    @JsonPath("$.paidContentOverlay.paidContentOverlayRenderer.text")
    private TextItem mPaidContentText;

    @JsonPath("$.streamingData.serverAbrStreamingUrl")
    private String mServerAbrStreamingUrl; // SABR format url

    @JsonPath("$.playerConfig.mediaCommonConfig.mediaUstreamerRequestConfig.videoPlaybackUstreamerConfig")
    private String mVideoPlaybackUstreamerConfig; // SABR config

    // Values used in tracking actions
    private String mEventId;
    private String mVisitorMonitoringData;
    private String mOfParam;

    private long mStartTimeMs;
    private int mStartSegmentNum;
    private int mSegmentDurationUs;
    private boolean mIsStreamSeekable;
    private List<CaptionTrack> mMergedCaptionTracks;
    private boolean mIsAuth;
    private String mPoToken;
    private AppClient mClient;
    private VideoUrlHolder mUrlHolder;

    public List<AdaptiveVideoFormat> getAdaptiveFormats() {
        return mAdaptiveFormats;
    }

    public List<RegularVideoFormat> getRegularFormats() {
        return mRegularFormats;
    }

    public List<AdaptiveVideoFormat> getRestrictedFormats() {
        return mRestrictedFormats;
    }

    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }

    public String getVideoStatsWatchTimeUrl() {
        return mVideoStatsWatchTimeUrl;
    }

    public List<CaptionTrack> getCaptionTracks() {
        return mergeCaptionTracks();
    }

    public void setCaptionTracks(List<CaptionTrack> tracks) {
        mMergedCaptionTracks = tracks;
    }

    public List<TranslationLanguage> getTranslationLanguages() {
        return mTranslationLanguages;
    }

    public void setTranslationLanguages(List<TranslationLanguage> translationLanguages) {
        mTranslationLanguages = translationLanguages;
    }

    public String getDashManifestUrl() {
        return mDashManifestUrl;
    }

    public void setDashManifestUrl(String dashManifestUrl) {
        mDashManifestUrl = dashManifestUrl;
    }

    public void setHlsManifestUrl(String hlsManifestUrl) {
        mHlsManifestUrl = hlsManifestUrl;
    }

    public void setStoryboardSpec(String storyboardSpec) {
        mStoryboardSpec = storyboardSpec;
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

    public String getOfParam() {
        parseTrackingParams();

        return mOfParam;
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
        return isUnknownRestricted() || isVisibilityRestricted() || isAgeRestricted() || isAdaptiveFormatsBroken();
    }

    /**
     * Video cannot be embedded
     */
    public boolean isEmbedRestricted() {
        return !mIsPlayableInEmbed;
    }

    /**
     * Reason of unavailability unknown or we received a temporal ban
     */
    public boolean isUnknownRestricted() {
        return ServiceHelper.atLeastOneEquals(mPlayabilityStatus, STATUS_UNPLAYABLE);
    }

    /**
     * Removed or hidden by the user
     */
    public boolean isVisibilityRestricted() {
        return ServiceHelper.atLeastOneEquals(mPlayabilityStatus, STATUS_ERROR);
    }

    /**
     * Age restricted video
     */
    public boolean isAgeRestricted() {
        return ServiceHelper.atLeastOneEquals(mPlayabilityStatus, STATUS_LOGIN_REQUIRED, STATUS_AGE_CHECK_REQUIRED, STATUS_CONTENT_CHECK_REQUIRED);
    }

    public boolean isExtendedHlsFormatsBroken() {
        return !isLive() && getHlsManifestUrl() == null && isAdaptiveFullHD();
    }

    //public boolean hasExtendedHlsFormats() {
    //    if (!isLive() && getHlsManifestUrl() != null && isAdaptiveFullHD()) {
    //        long uploadTimeMs = DateHelper.toUnixTimeMs(getUploadDate());
    //        // Extended formats may not work during 3 days after publication
    //        return uploadTimeMs > 0 && System.currentTimeMillis() - uploadTimeMs > 4*24*60*60*1_000;
    //    }
    //
    //    return false;
    //}

    public boolean hasExtendedHlsFormats() {
        // Need upload date check?
        // Extended formats may not work up to 3 days after publication.
        return !isLive() && getHlsManifestUrl() != null && isAdaptiveFullHD();
    }

    public boolean containsAdaptiveVideoInfo() {
        if (getAdaptiveFormats() == null) {
            return false;
        }

        boolean result = false;

        for (AdaptiveVideoFormat format : getAdaptiveFormats()) {
            String mimeType = format.getMimeType();
            if (mimeType != null && mimeType.startsWith("video/")) {
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean containsRegularVideoInfo() {
        if (getRegularFormats() == null) {
            return false;
        }

        boolean result = false;

        for (RegularVideoFormat format : getRegularFormats()) {
            String mimeType = format.getMimeType();
            if (mimeType != null && mimeType.startsWith("video/")) {
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean isStoryboardBroken() {
        return !isLive() && getStoryboardSpec() == null && (containsAdaptiveVideoInfo() || containsRegularVideoInfo());
    }

    public boolean isLive() {
        return getVideoDetails() != null && getVideoDetails().isLive();
    }

    public boolean hasSubtitles() {
        return mCaptionTracks != null;
    }

    public String getPlayabilityStatus() {
        return Helpers.toString(ServiceHelper.createInfo(mPlayabilityReason, mPlayabilityDescription));
    }

    public String getStoryboardSpec() {
        return mStoryboardSpec;
    }

    public String getTrailerVideoId() {
        return mTrailerVideoId;
    }

    public String getStartTimestamp() {
        return mStartTimestamp;
    }

    public String getUploadDate() {
        return mUploadDate;
    }

    public long getStartTimeMs() {
        return mStartTimeMs;
    }

    public int getStartSegmentNum() {
        return mStartSegmentNum;
    }

    public int getSegmentDurationUs() {
        return mSegmentDurationUs;
    }

    public float getLoudnessDb() {
        return mLoudnessDb;
    }

    public boolean isStreamSeekable() {
        return mIsStreamSeekable;
    }

    public boolean isHfr() {
        return mDashManifestUrl != null && mDashManifestUrl.contains("/hfr/all");
    }

    public String getPaidContentText() {
        return mPaidContentText != null ? Helpers.toString(mPaidContentText.getText()) : null;
    }

    public String getServerAbrStreamingUrl() {
        return getUrlHolder().getUrl();
    }

    public String getVideoPlaybackUstreamerConfig() {
        return mVideoPlaybackUstreamerConfig;
    }

    /**
     * Sync live data
     */
    public void sync(DashInfo dashInfo) {
        if (dashInfo == null) {
            return;
        }

        mSegmentDurationUs = dashInfo.getSegmentDurationUs();
        mStartTimeMs = dashInfo.getStartTimeMs();
        mStartSegmentNum = dashInfo.getStartSegmentNum();
        mIsStreamSeekable = dashInfo.isSeekable();
    }

    public void setAuth(boolean auth) {
        mIsAuth = auth;
    }

    public boolean isAuth() {
        return mIsAuth;
    }

    private void parseTrackingParams() {
        boolean parseDone = mEventId != null || mVisitorMonitoringData != null;

        if (!parseDone && mWatchTimeUrl != null) {
            UrlQueryString queryString = UrlQueryStringFactory.parse(mWatchTimeUrl);

            mEventId = queryString.get(PARAM_EVENT_ID);
            mVisitorMonitoringData = queryString.get(PARAM_VM);
            mOfParam = queryString.get(PARAM_OF);
        }
    }

    private List<CaptionTrack> mergeCaptionTracks() {
        if (mMergedCaptionTracks == null) {
            mMergedCaptionTracks = mCaptionTracks;

            if (mTranslationLanguages != null && mCaptionTracks != null) {
                CaptionTrack originTrack = findOriginTrack(mCaptionTracks);
                String tag = Helpers.runMultiMatcher(originTrack.getName(), tagPattern);
                for (TranslationLanguage language : mTranslationLanguages) {
                    mMergedCaptionTracks.add(new TranslatedCaptionTrack(originTrack, language, tag));
                }
            }
        }

        return mMergedCaptionTracks;
    }

    private CaptionTrack findOriginTrack(List<CaptionTrack> captionTracks) {
        CaptionTrack result = null;

        for (CaptionTrack track : captionTracks) {
            if (!track.isAutogenerated()) {
                result = track;
                break;
            }
        }

        return result != null ? result : captionTracks.get(0);
    }

    private boolean isAdaptiveFullHD() {
        return getAdaptiveFormats() != null && !getAdaptiveFormats().isEmpty() && "1080p".equals(getAdaptiveFormats().get(0).getQualityLabel());
    }

    /**
     * TODO: remove when SABR parser will be fixed
     */
    private boolean isAdaptiveFormatsBroken() {
        // TODO: remove when SABR parser will be fixed
        if (mAdaptiveFormats == null || mAdaptiveFormats.isEmpty()) {
            return false;
        }

        boolean allBroken = true;

        for (AdaptiveVideoFormat format : mAdaptiveFormats) {
            if (format != null && !format.isBroken()) {
                allBroken = false;
                break;
            }
        }

        return allBroken;
    }

    public String getPoToken() {
        return mPoToken;
    }

    public void setPoToken(String poToken) {
        mPoToken = poToken;
    }

    public AppClient getClient() {
        return mClient;
    }

    public void setClient(AppClient client) {
        mClient = client;
    }

    public VideoUrlHolder getUrlHolder() {
        if (mUrlHolder == null) {
            mUrlHolder = new VideoUrlHolder(mServerAbrStreamingUrl, null, null);
        }

        return mUrlHolder;
    }
}
