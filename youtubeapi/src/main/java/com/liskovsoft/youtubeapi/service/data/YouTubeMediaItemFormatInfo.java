package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle;
import com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder.YouTubeUrlListBuilder;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.YouTubeMPDBuilder;
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser;
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser.Storyboard;
import com.liskovsoft.youtubeapi.videoinfo.models.CaptionTrack;
import com.liskovsoft.youtubeapi.pageinfo.models.TranslationLanguage;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoDetails;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.pageinfo.models.PageInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;
import io.reactivex.Observable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaItemFormatInfo implements MediaItemFormatInfo {
    private String mLengthSeconds;
    private String mTitle;
    private String mAuthor;
    private String mViewCount;
    private String mTimestamp;
    private String mDescription;
    private String mVideoId;
    private String mChannelId;
    private boolean mIsLive;
    private boolean mIsLiveContent;
    private boolean mIsLowLatencyLiveStream;
    private boolean mIsStreamSeekable;
    private List<MediaFormat> mAdaptiveFormats;
    private List<MediaFormat> mRegularFormats;
    private List<MediaSubtitle> mSubtitles;
    private String mDashManifestUrl;
    private String mHlsManifestUrl;
    private String mEventId; // used in tracking
    private String mVisitorMonitoringData; // used in tracking
    private String mStoryboardSpec;
    private boolean mIsUnplayable;
    private String mPlayabilityStatus;
    private final long mCreatedTimeMs;

    public YouTubeMediaItemFormatInfo() {
        mCreatedTimeMs = System.currentTimeMillis();
    }

    public static YouTubeMediaItemFormatInfo from(VideoInfo videoInfo, PageInfo pageInfo) {
        if (videoInfo == null || pageInfo == null) {
            return null;
        }

        YouTubeMediaItemFormatInfo formatInfo = new YouTubeMediaItemFormatInfo();

        if (videoInfo.getAdaptiveFormats() != null) {
            formatInfo.mAdaptiveFormats = new ArrayList<>();

            for (AdaptiveVideoFormat format : videoInfo.getAdaptiveFormats()) {
                formatInfo.mAdaptiveFormats.add(YouTubeMediaFormat.from(format));
            }
        }

        if (videoInfo.getRegularFormats() != null) {
            formatInfo.mRegularFormats = new ArrayList<>();

            for (RegularVideoFormat format : videoInfo.getRegularFormats()) {
                formatInfo.mRegularFormats.add(YouTubeMediaFormat.from(format));
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
        formatInfo.mEventId = videoInfo.getEventId();
        formatInfo.mVisitorMonitoringData = videoInfo.getVisitorMonitoringData();
        formatInfo.mStoryboardSpec = videoInfo.getStoryboardSpec();
        formatInfo.mIsUnplayable = videoInfo.isUnplayable();
        formatInfo.mPlayabilityStatus = videoInfo.getPlayabilityStatus();
        formatInfo.mIsStreamSeekable = videoInfo.isHfr();

        List<CaptionTrack> captionTracks = videoInfo.getCaptionTracks();
        List<TranslationLanguage> translationLanguages = pageInfo.getTranslationLanguages();

        if (captionTracks != null) {
            formatInfo.mSubtitles = new ArrayList<>();

            for (CaptionTrack track : captionTracks) {
                formatInfo.mSubtitles.add(YouTubeMediaSubtitle.from(track));

                if (translationLanguages != null) {
                    for (TranslationLanguage language : translationLanguages) {
                        formatInfo.mSubtitles.add(YouTubeMediaSubtitle.from(language, track));
                    }
                }
            }
        }

        return formatInfo;
    }

    @Override
    public List<MediaFormat> getAdaptiveFormats() {
        return mAdaptiveFormats;
    }

    @Override
    public List<MediaFormat> getRegularFormats() {
        return mRegularFormats;
    }

    @Override
    public List<MediaSubtitle> getSubtitles() {
        return mSubtitles;
    }

    @Override
    public String getLengthSeconds() {
        return mLengthSeconds;
    }

    @Override
    public void setLengthSeconds(String lengthSeconds) {
        mLengthSeconds = lengthSeconds;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public void setAuthor(String author) {
        mAuthor = author;
    }

    @Override
    public String getViewCount() {
        return mViewCount;
    }

    @Override
    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

    @Override
    public String getTimestamp() {
        return mTimestamp;
    }

    @Override
    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public void setChannelId(String channelId) {
        mChannelId = channelId;
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
        return containsDashUrl() || containsHlsUrl() || containsDashVideoInfo() || containsUrlListInfo();
    }

    @Override
    public boolean containsDashInfo() {
        return mAdaptiveFormats != null;
    }

    @Override
    public boolean containsDashVideoInfo() {
        if (mAdaptiveFormats == null) {
            return false;
        }

        for (MediaFormat format : mAdaptiveFormats) {
            String mimeType = format.getMimeType();
            if (mimeType != null && mimeType.startsWith("video/")) {
                return true;
            }
        }

        return false;
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
    public boolean containsUrlListInfo() {
        return mRegularFormats != null;
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
        return Observable.fromCallable(this::createMpdStream);
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

        Storyboard storyboard = YouTubeStoryParser.from(mStoryboardSpec).extractStory();

        return YouTubeMediaItemStoryboard.from(storyboard);
    }

    @Override
    public boolean isUnplayable() {
        return mIsUnplayable;
    }

    @Override
    public String getPlayabilityStatus() {
        return mPlayabilityStatus;
    }

    @Override
    public boolean isStreamSeekable() {
        return mIsStreamSeekable;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String eventId) {
        mEventId = eventId;
    }

    public String getVisitorMonitoringData() {
        return mVisitorMonitoringData;
    }

    public void setVisitorMonitoringData(String visitorMonitoringData) {
        mVisitorMonitoringData = visitorMonitoringData;
    }

    public long getCreatedTimeMs() {
        return mCreatedTimeMs;
    }
}
