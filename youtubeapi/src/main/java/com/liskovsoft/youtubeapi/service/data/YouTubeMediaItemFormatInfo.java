package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder.YouTubeUrlListBuilder;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.YouTubeMPDBuilder;
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser;
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser.Storyboard;
import com.liskovsoft.youtubeapi.videoinfo.models.CaptionTrack;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoDetails;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;
import io.reactivex.Observable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class YouTubeMediaItemFormatInfo implements MediaItemFormatInfo {
    private static final String TAG = YouTubeMediaItemFormatInfo.class.getSimpleName();
    private String mLengthSeconds;
    private String mTitle;
    private String mAuthor;
    private String mViewCount;
    private String mDescription;
    private String mVideoId;
    private String mChannelId;
    private boolean mIsLive;
    private boolean mIsLiveContent;
    private boolean mIsLowLatencyLiveStream;
    private boolean mIsStreamSeekable;
    private List<MediaFormat> mAdaptiveFormats;
    private List<MediaFormat> mUrlFormats;
    private List<MediaSubtitle> mSubtitles;
    private String mDashManifestUrl;
    private String mHlsManifestUrl;
    private String mEventId; // used in tracking
    private String mVisitorMonitoringData; // used in tracking
    private String mOfParam; // used in tracking
    private String mStoryboardSpec;
    private boolean mIsUnplayable;
    private String mPlayabilityStatus;
    private String mStartTimestamp;
    private String mUploadDate;
    private long mStartTimeMs;
    private int mStartSegmentNum;
    private int mSegmentDurationUs;
    private boolean mHasExtendedHlsFormats;
    private float mLoudnessDb;
    private boolean mContainsAdaptiveVideoFormats;
    private boolean mIsAuth;
    private boolean mIsSynced;
    private boolean mIsUnknownError;
    private String mPaidContentText;
    private String mClickTrackingParams;
    private String mVideoPlaybackUstreamerConfig;
    private String mServerAbrStreamingUrl;
    private String mPoToken;
    private AppClient mClient;
    private static final Pattern durationPattern1 = Pattern.compile("dur=([^&]*)");
    private static final Pattern durationPattern2 = Pattern.compile("/dur/([^/]*)");

    private YouTubeMediaItemFormatInfo() {
        
    }

    public static YouTubeMediaItemFormatInfo from(VideoInfo videoInfo) {
        if (videoInfo == null) {
            return null;
        }

        YouTubeMediaItemFormatInfo formatInfo = new YouTubeMediaItemFormatInfo();

        if (videoInfo.getAdaptiveFormats() != null) {
            formatInfo.mContainsAdaptiveVideoFormats = videoInfo.containsAdaptiveVideoInfo();

            formatInfo.mAdaptiveFormats = new ArrayList<>();

            for (AdaptiveVideoFormat format : videoInfo.getAdaptiveFormats()) {
                formatInfo.mAdaptiveFormats.add(YouTubeMediaFormat.from(format));
            }
        }

        if (videoInfo.getRegularFormats() != null) {
            formatInfo.mUrlFormats = new ArrayList<>();

            for (RegularVideoFormat format : videoInfo.getRegularFormats()) {
                formatInfo.mUrlFormats.add(YouTubeMediaFormat.from(format));
            }
        }

        VideoDetails videoDetails = videoInfo.getVideoDetails();

        if (videoDetails != null) {
            formatInfo.mLengthSeconds = videoDetails.getLengthSeconds();
            formatInfo.mVideoId = videoDetails.getVideoId();
            formatInfo.mViewCount = videoDetails.getViewCount();
            formatInfo.mTitle = videoDetails.getTitle();
            formatInfo.mDescription = videoDetails.getShortDescription();
            formatInfo.mChannelId = videoDetails.getChannelId();
            formatInfo.mAuthor = videoDetails.getAuthor();
            formatInfo.mIsLive = videoDetails.isLive();
            formatInfo.mIsLiveContent = videoDetails.isLiveContent();
            formatInfo.mIsLowLatencyLiveStream = videoDetails.isLowLatencyLiveStream();
        }

        formatInfo.mDashManifestUrl = videoInfo.getDashManifestUrl();
        formatInfo.mHlsManifestUrl = videoInfo.getHlsManifestUrl();
        // BEGIN Tracking params
        formatInfo.mEventId = videoInfo.getEventId();
        formatInfo.mVisitorMonitoringData = videoInfo.getVisitorMonitoringData();
        formatInfo.mOfParam = videoInfo.getOfParam();
        // END Tracking params
        formatInfo.mStoryboardSpec = videoInfo.getStoryboardSpec();
        formatInfo.mIsUnplayable = videoInfo.isUnplayable();
        formatInfo.mIsAuth = videoInfo.isAuth();
        formatInfo.mIsUnknownError = videoInfo.isUnknownRestricted();
        formatInfo.mPlayabilityStatus = videoInfo.getPlayabilityStatus();
        formatInfo.mIsStreamSeekable = videoInfo.isHfr() || videoInfo.isStreamSeekable();
        formatInfo.mStartTimestamp = videoInfo.getStartTimestamp();
        formatInfo.mUploadDate = videoInfo.getUploadDate();
        formatInfo.mStartTimeMs = videoInfo.getStartTimeMs();
        formatInfo.mStartSegmentNum = videoInfo.getStartSegmentNum();
        formatInfo.mSegmentDurationUs = videoInfo.getSegmentDurationUs();
        formatInfo.mHasExtendedHlsFormats = videoInfo.hasExtendedHlsFormats();
        formatInfo.mLoudnessDb = videoInfo.getLoudnessDb();
        formatInfo.mPaidContentText = videoInfo.getPaidContentText();
        formatInfo.mVideoPlaybackUstreamerConfig = videoInfo.getVideoPlaybackUstreamerConfig();
        formatInfo.mServerAbrStreamingUrl = videoInfo.getServerAbrStreamingUrl();
        formatInfo.mPoToken = videoInfo.getPoToken();
        formatInfo.mClient = videoInfo.getClient();

        List<CaptionTrack> captionTracks = videoInfo.getCaptionTracks();

        if (captionTracks != null) {
            formatInfo.mSubtitles = new ArrayList<>();

            for (CaptionTrack track : captionTracks) {
                formatInfo.mSubtitles.add(YouTubeMediaSubtitle.from(track));
            }
        }

        return formatInfo;
    }

    @Override
    public List<MediaFormat> getAdaptiveFormats() {
        return mAdaptiveFormats;
    }

    @Override
    public List<MediaFormat> getUrlFormats() {
        return mUrlFormats;
    }

    @Override
    public List<MediaSubtitle> getSubtitles() {
        return mSubtitles;
    }

    @Override
    public String getLengthSeconds() {
        ensureLengthIsSet();

        return mLengthSeconds;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public String getViewCount() {
        return mViewCount;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public boolean isLive() {
        return mIsLive;
    }

    @Override
    public boolean isLiveContent() {
        return mIsLiveContent;
    }
    
    public boolean isLowLatencyStream() {
        return mIsLowLatencyLiveStream;
    }

    @Override
    public boolean containsMedia() {
        return containsDashUrl() || containsHlsUrl() || containsAdaptiveVideoFormats() || containsUrlFormats();
    }

    @Override
    public boolean containsSabrFormats() {
        return mContainsAdaptiveVideoFormats && mAdaptiveFormats.get(0).getFormatType() == MediaFormat.FORMAT_TYPE_SABR;
    }

    @Override
    public boolean containsDashFormats() {
        return mContainsAdaptiveVideoFormats && mAdaptiveFormats.get(0).getFormatType() == MediaFormat.FORMAT_TYPE_DASH;
    }
    
    private boolean containsAdaptiveVideoFormats() {
        return mContainsAdaptiveVideoFormats;
    }

    @Override
    public boolean containsHlsUrl() {
        return mHlsManifestUrl != null;
    }

    @Override
    public boolean containsDashUrl() {
        return mDashManifestUrl != null;
    }

    @Override
    public boolean containsUrlFormats() {
        return mUrlFormats != null;
    }

    @Override
    public boolean hasExtendedHlsFormats() {
        return mHasExtendedHlsFormats;
    }

    @Override
    public float getVolumeLevel() {
        float result = 1.0f; // the live loudness

        //if (mLoudnessDb != 0) {
        //    // Original tv web: Math.min(1, 10 ** (-loudnessDb / 20))
        //    // -5db...5db (0.7...1.4) Base formula: normalLevel*10^(-db/20)
        //    float normalLevel = (float) Math.pow(10.0f, -mLoudnessDb / 50.0f);
        //    result = Math.min(normalLevel, 2.5f);
        //    result *= 0.6f; // minimize distortions
        //}

        //if (mLoudnessDb != 0) {
        //    // Original tv web: Math.min(1, 10 ** (-loudnessDb / 20))
        //    // -5db...5db (0.7...1.4) Base formula: normalLevel*10^(-db/20)
        //    float normalLevel = (float) Math.pow(10.0f, -mLoudnessDb / 20.0f);
        //    //float multiplier = 0.2f;
        //    float multiplier = 0.3f;
        //    result = Math.min(normalLevel, 1.5f / multiplier); // 1.5 max volume
        //    result *= multiplier; // minimize distortions
        //}

        //if (mLoudnessDb != 0) {
        //    // Original tv web: Math.min(1, 10 ** (-loudnessDb / 20))
        //    // -5db...5db (0.7...1.4) Base formula: normalLevel*10^(-db/20)
        //    float normalLevel = (float) Math.pow(10.0f, -mLoudnessDb / 15.0f);
        //    result = Math.min(normalLevel, 4f);
        //    result *= 0.3f; // minimize distortions
        //}

        if (mLoudnessDb != 0) {
            // Original tv web: Math.min(1, 10 ** (-loudnessDb / 20))
            // -5db...5db (0.7...1.4) Base formula: normalLevel*10^(-db/20)
            // Low test - R.E.M. and high test - Lindemann
            float normalLevel = (float) Math.pow(10.0f, mLoudnessDb / 20.0f);
            if (normalLevel > 1.95) { // don't normalize?
                // System of a Down - Lonely Day
                //normalLevel = 1.0f;
                normalLevel = 1.5f;
            }
            // Calculate the result as subtract of the video volume and the max volume
            result = 2.0f - normalLevel;
        }

        return result / 2;
    }

    @Override
    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }

    @Override
    public String getDashManifestUrl() {
        return mDashManifestUrl;
    }

    @Override
    public InputStream createMpdStream() {
        return YouTubeMPDBuilder.from(this).build();
    }

    @Override
    public Observable<InputStream> createMpdStreamObservable() {
        return RxHelper.fromCallable(this::createMpdStream);
    }

    @Override
    public List<String> createUrlList() {
        return YouTubeUrlListBuilder.from(this).buildUriList();
    }

    @Override
    public MediaItemStoryboard createStoryboard() {
        if (mStoryboardSpec == null) {
            return null;
        }

        YouTubeStoryParser storyParser = YouTubeStoryParser.from(mStoryboardSpec);
        storyParser.setSegmentDurationUs(getSegmentDurationUs());
        // TODO: need to calculate real segment shift for 60 hrs streams (e.g. euronews live)
        storyParser.setStartSegmentNum(getStartSegmentNum());
        Storyboard storyboard = storyParser.extractStory();

        return YouTubeMediaItemStoryboard.from(storyboard);
    }

    @Override
    public boolean isUnplayable() {
        return mIsUnplayable;
    }

    public boolean isAuth() {
        return mIsAuth;
    }

    public boolean isSynced() {
        return mIsSynced;
    }

    @Override
    public boolean isUnknownError() {
        return mIsUnknownError;
    }

    @Override
    public String getPlayabilityStatus() {
        return mPlayabilityStatus;
    }

    @Override
    public boolean isStreamSeekable() {
        return mIsStreamSeekable;
    }

    @Override
    public String getStartTimestamp() {
        return mStartTimestamp;
    }

    @Override
    public String getUploadDate() {
        return mUploadDate;
    }

    @Override
    public long getStartTimeMs() {
        return mStartTimeMs;
    }

    @Override
    public int getStartSegmentNum() {
        return mStartSegmentNum;
    }

    @Override
    public int getSegmentDurationUs() {
        return mSegmentDurationUs;
    }

    @Override
    public String getPaidContentText() {
        return mPaidContentText;
    }

    @Override
    public String getVideoPlaybackUstreamerConfig() {
        return mVideoPlaybackUstreamerConfig;
    }

    @Override
    public String getServerAbrStreamingUrl() {
        return mServerAbrStreamingUrl;
    }

    @Override
    public String getPoToken() {
        return mPoToken;
    }

    @Override
    public ClientInfo getClientInfo() {
        return mClient;
    }

    public String getEventId() {
        return mEventId;
    }

    public String getVisitorMonitoringData() {
        return mVisitorMonitoringData;
    }

    public String getOfParam() {
        return mOfParam;
    }

    public String getClickTrackingParams() {
        return mClickTrackingParams;
    }

    public void setClickTrackingParams(String clickTrackingParams) {
        mClickTrackingParams = clickTrackingParams;
    }

    /**
     * Format is used between multiple functions. Do a little cache.
     */
    public boolean isCacheActual() {
        // NOTE: Musical live streams are ciphered too!

        // Check app cipher first. It's not robust check (cipher may be updated not by us).
        // So, also check internal cache state.
        // Future translations (no media) should be polled constantly.
        return containsMedia() && AppService.instance().isPlayerCacheActual() && !PoTokenGate.isWebPotExpired();
    }

    /**
     * Sync history data<br/>
     * Intended to merge signed and unsigned infos (no-playback fix)
     */
    public void sync(YouTubeMediaItemFormatInfo formatInfo) {
        mIsSynced = true;

        if (formatInfo == null || Helpers.anyNull(formatInfo.getEventId(), formatInfo.getVisitorMonitoringData(), formatInfo.getOfParam())) {
            return;
        }

        // Intended to merge signed and unsigned infos (no-playback fix)
        mEventId = formatInfo.getEventId();
        mVisitorMonitoringData = formatInfo.getVisitorMonitoringData();
        mOfParam = formatInfo.getOfParam();
        mIsAuth = formatInfo.isAuth();
    }

    /**
     * MPD file is not valid without duration
     */
    private void ensureLengthIsSet() {
        if (mLengthSeconds != null) {
            return;
        }

        // try to get duration from video url
        mLengthSeconds = extractDurationFromTrack();
    }

    /**
     * Extracts time from video url (if present).
     * Url examples:
     * <br/>
     * "http://example.com?dur=544.99&key=val&key2=val2"
     * <br/>
     * "http://example.com/dur/544.99/key/val/key2/val2"
     *
     * @return duration as string
     */
    private String extractDurationFromTrack() {
        if (mAdaptiveFormats == null && mUrlFormats == null) {
            return null;
        }

        String url = null;
        // mMP4Videos
        List<MediaFormat> videos = mAdaptiveFormats != null ? mAdaptiveFormats : mUrlFormats;
        for (MediaFormat item : videos) {
            url = item.getUrl();
            break; // get first item
        }
        String result = Helpers.runMultiMatcher(url, durationPattern1, durationPattern2);

        if (result == null) {
            //throw new IllegalStateException("Videos in the list doesn't have a duration. Content: " + mMP4Videos);
            Log.e(TAG, "Videos in the list doesn't have a duration. Content: " + videos);
        }

        return result;
    }

    /**
     * Apply the fix right after creation of a MediaFormat
     */
    private void fixOTF(YouTubeMediaFormat mediaItem) {
        if (mediaItem.isOtf()) {
            if (mediaItem.getUrl() != null) {
                // exo: fix 404 code
                mediaItem.setUrl(mediaItem.getUrl() + "&sq=7");
                //mediaItem.setInit("0-740");
                //mediaItem.setIndex("741-2296");
                //mediaItem.setClen("105557711");
            }
        }
    }
}
