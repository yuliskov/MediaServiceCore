package com.liskovsoft.youtubeapi.service.data;

import androidx.annotation.NonNull;
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.formatbuilders.utils.ITagUtils;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;

import java.util.List;

public class YouTubeMediaFormat implements MediaFormat {
    private String mIndex;
    private String mIndexRange;
    private String mUrl;
    private String mMimeType;
    private String mITag;
    private boolean mIsDrc;
    private String mClen;
    private String mBitrate;
    private String mProjectionType;
    private String mXtags;
    private int mWidth;
    private int mHeight;
    private String mInit;
    private String mFps;
    private String mLmt;
    private String mQualityLabel;
    private String mFormat;
    private boolean mIsOtf;
    private String mOtfInitUrl;
    private String mOtfTemplateUrl;
    private String mSourceUrl;
    private List<String> mSegmentUrlList;
    private List<String> mGlobalSegmentList;
    private String mAudioQuality;
    private int mFormatType;
    private String mLanguage;
    private int mTargetDurationSec;
    private int mMaxDvrDurationSec;
    private int mApproxDurationMs;

    public static MediaFormat from(AdaptiveVideoFormat format) {
        YouTubeMediaFormat mediaFormat = createBaseFormat(format);

        mediaFormat.mFormatType = format.isBroken() ? FORMAT_TYPE_SABR : FORMAT_TYPE_DASH;

        mediaFormat.mIndex = format.getIndex();

        if (format.getIndexRange() != null) {
            mediaFormat.mIndexRange = format.getIndexRange().toString();
        }

        mediaFormat.mInit = format.getInit();

        return mediaFormat;
    }

    public static MediaFormat from(RegularVideoFormat format) {
        YouTubeMediaFormat mediaFormat = createBaseFormat(format);

        mediaFormat.mFormatType = FORMAT_TYPE_REGULAR;

        mediaFormat.mAudioQuality = format.getAudioQuality();

        return mediaFormat;
    }

    private static YouTubeMediaFormat createBaseFormat(VideoFormat format) {
        YouTubeMediaFormat mediaFormat = new YouTubeMediaFormat();

        mediaFormat.mUrl = format.getUrl();
        mediaFormat.mMimeType = format.getMimeType();
        String iTag = format.getITag() == 0 ? "" : String.valueOf(format.getITag());
        mediaFormat.mITag = iTag;
        mediaFormat.mIsDrc = format.isDrc();
        mediaFormat.mClen = format.getContentLength();
        String bitrate = format.getBitrate() == 0 ? "" : String.valueOf(format.getBitrate());
        mediaFormat.mBitrate = bitrate;
        mediaFormat.mWidth = format.getWidth();
        mediaFormat.mHeight = format.getHeight();
        String fps = format.getFps() == 0 ? "" : String.valueOf(format.getFps());
        mediaFormat.mFps = fps;
        mediaFormat.mFormat = format.getFormat();
        mediaFormat.mIsOtf = format.isOTF();
        mediaFormat.mOtfInitUrl = format.getOtfInitUrl();
        mediaFormat.mOtfTemplateUrl = format.getOtfTemplateUrl();
        mediaFormat.mSourceUrl = format.getSourceURL();
        mediaFormat.mSegmentUrlList = format.getSegmentUrlList();
        mediaFormat.mGlobalSegmentList = format.getGlobalSegmentList();
        mediaFormat.mLanguage = format.getLanguage();
        mediaFormat.mTargetDurationSec = format.getTargetDurationSec();
        mediaFormat.mLmt = format.getLastModified();
        mediaFormat.mQualityLabel = format.getQualityLabel();
        mediaFormat.mMaxDvrDurationSec = format.getMaxDvrDurationSec();
        mediaFormat.mApproxDurationMs = Helpers.parseInt(format.getApproxDurationMs());

        return mediaFormat;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }
    
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }
    
    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    @Override
    public String getITag() {
        return mITag;
    }
    
    public void setITag(String itag) {
        mITag = itag;
    }

    @Override
    public boolean isDrc() {
        return mIsDrc;
    }

    @Override
    public String getClen() {
        return mClen;
    }
    
    public void setClen(String clen) {
        mClen = clen;
    }

    @Override
    public String getBitrate() {
        return mBitrate;
    }
    
    public void setBitrate(String bitrate) {
        mBitrate = bitrate;
    }

    @Override
    public String getProjectionType() {
        return mProjectionType;
    }

    @Override
    public String getXtags() {
        return mXtags;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }
    
    public void setWidth(int width) {
        mWidth = width;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    @Override
    public String getIndex() {
        return mIndex;
    }

    public void setIndex(String index) {
        mIndex = index;
    }

    @Override
    public String getInit() {
        return mInit;
    }

    public void setInit(String init) {
        mInit = init;
    }

    @Override
    public String getFps() {
        return mFps;
    }

    public void setFps(String fps) {
        mFps = fps;
    }

    @Override
    public String getLmt() {
        return mLmt;
    }

    @Override
    public String getQualityLabel() {
        return mQualityLabel;
    }

    @Override
    public String getFormat() {
        return mFormat;
    }

    @Override
    public boolean isOtf() {
        return mIsOtf;
    }

    @Override
    public String getOtfInitUrl() {
        return mOtfInitUrl;
    }

    @Override
    public String getOtfTemplateUrl() {
        return mOtfTemplateUrl;
    }

    @Override
    public String getQuality() {
        return null;
    }

    @Override
    public String getSignature() {
        return null;
    }

    public void setAudioSamplingRate(String audioSamplingRate) {

    }

    @Override
    public String getAudioSamplingRate() {
        return null;
    }

    public void setSourceUrl(String sourceUrl) {
        mSourceUrl = sourceUrl;
    }

    @Override
    public String getSourceUrl() {
        return mSourceUrl;
    }

    @Override
    public List<String> getSegmentUrlList() {
        return mSegmentUrlList;
    }

    public void setSegmentUrlList(List<String> urls) {
        mSegmentUrlList = urls;
    }

    @Override
    public List<String> getGlobalSegmentList() {
        return mGlobalSegmentList;
    }

    public void setGlobalSegmentList(List<String> segments) {
        mGlobalSegmentList = segments;
    }

    @Override
    public String getLanguage() {
        return mLanguage;
    }

    @Override
    public int getTargetDurationSec() {
        return mTargetDurationSec;
    }

    @Override
    public int getMaxDvrDurationSec() {
        return mMaxDvrDurationSec;
    }

    @Override
    public int getApproxDurationMs() {
        return mApproxDurationMs;
    }

    public String getIndexRange() {
        return mIndexRange;
    }

    public void setIndexRange(String indexRange) {
        mIndexRange = indexRange;
    }

    @Override
    public int compareTo(MediaFormat format) {
        if (format == null) {
            return 1;
        }

        return ITagUtils.compare(getITag(), format.getITag());
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Width: %s, Height: %s, ITag: %s}",
                getUrl(),
                getSourceUrl(),
                getSignature(),
                getClen(),
                getWidth(),
                getHeight(),
                getITag());
    }

    public String getAudioQuality() {
        return mAudioQuality;
    }

    @Override
    public int getFormatType() {
        return mFormatType;
    }
}
