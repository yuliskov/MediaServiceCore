package com.liskovsoft.youtubeapi.videoinfo.models.formats;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.formatbuilders.utils.ITagUtils;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder;

import java.util.List;

public class VideoFormat {
    private static final String FORMAT_STREAM_TYPE_OTF = "FORMAT_STREAM_TYPE_OTF";

    // DASH params
    public static final String CLEN = "clen";
    public static final String BITRATE = "bitrate";
    public static final String PROJECTION_TYPE = "projection_type";
    public static final String XTAGS = "xtags";
    public static final String SIZE = "size";
    public static final String INDEX = "index";
    public static final String FPS = "fps";
    public static final String LMT = "lmt";
    public static final String QUALITY_LABEL = "quality_label";
    public static final String INIT = "init";
    // End DASH params

    // Regular video params
    public static final String QUALITY = "quality";
    // End Regular params
    
    @JsonPath("$.itag")
    private int mITag;
    @JsonPath("$.url")
    private String mUrl;
    @JsonPath("$.cipher")
    private String mCipher;
    @JsonPath("$.signatureCipher")
    private String mSignatureCipher;
    @JsonPath("$.mimeType")
    private String mMimeType;
    @JsonPath("$.contentLength")
    private String mContentLength;
    @JsonPath("$.bitrate")
    private int mBitrate;
    @JsonPath("$.averageBitrate")
    private int mAverageBitrate;
    @JsonPath("$.projectionType")
    private String mProjectionType;
    private String mXtags;
    @JsonPath("$.width")
    private int mWidth = -1;
    @JsonPath("$.height")
    private int mHeight = -1;
    @JsonPath("$.fps")
    private int mFps;
    private String mLmt;
    @JsonPath("$.qualityLabel")
    private String mQualityLabel;
    @JsonPath("$.quality")
    private String mQuality;
    private String mRealSignature;
    @JsonPath("$.audioSampleRate")
    private String mAudioSamplingRate;
    private String mSourceURL;
    private List<String> mSegmentUrlList;
    private List<String> mGlobalSegmentList;
    /**
     * New format type FORMAT_STREAM_TYPE_OTF or null
     */
    @JsonPath("$.type")
    private String mFormat;
    private String mTemplateMetadataUrl;
    private String mTemplateSegmentUrl;
    @JsonPath("$.approxDurationMs")
    private String mApproxDurationMs;
    @JsonPath("$.lastModified")
    private String mLastModified;
    private String mLanguage;
    @JsonPath("$.targetDurationSec")
    private int mTargetDurationSec;
    @JsonPath("$.maxDvrDurationSec")
    private int mMaxDvrDurationSec;
    @JsonPath("$.isDrc")
    private boolean mIsDrc;
    private VideoUrlHolder mUrlHolder;

    public String getUrl() {
        return getUrlHolder().getUrl();
    }

    public void setUrl(String url) {
        getUrlHolder().setUrl(url);
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    public int getITag() {
        return mITag;
    }

    public void setITag(int itag) {
        mITag = itag;
    }

    public boolean isDrc() {
        return mIsDrc;
    }

    public String getContentLength() {
        return mContentLength;
    }

    public void setContentLength(String contentLength) {
        mContentLength = contentLength;
    }

    public int getBitrate() {
        return mBitrate;
    }

    public void setBitrate(int bitrate) {
        mBitrate = bitrate;
    }

    public String getProjectionType() {
        return mProjectionType;
    }

    public void setProjectionType(String projectionType) {
        mProjectionType = projectionType;
    }

    public String getXtags() {
        return mXtags;
    }

    public void setXtags(String xtags) {
        mXtags = xtags;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getFps() {
        return mFps;
    }

    public void setFps(int fps) {
        mFps = fps;
    }

    public String getLmt() {
        return mLmt;
    }

    public void setLmt(String lmt) {
        mLmt = lmt;
    }

    public String getQualityLabel() {
        return mQualityLabel;
    }

    public void setQualityLabel(String qualityLabel) {
        mQualityLabel = qualityLabel;
    }

    public String getQuality() {
        return mQuality;
    }

    public void setQuality(String quality) {
        mQuality = quality;
    }

    public boolean belongsToType(String type) {
        return type == null || ITagUtils.belongsToType(type, getITag());
    }

    public void setAudioSamplingRate(String audioSamplingRate) {
        mAudioSamplingRate = audioSamplingRate;
    }

    public String getAudioSamplingRate() {
        return mAudioSamplingRate;
    }

    public String getSourceURL() {
        return mSourceURL;
    }

    public void setSourceURL(String sourceURL) {
        mSourceURL = sourceURL;
    }

    public List<String> getSegmentUrlList() {
        return mSegmentUrlList;
    }

    public void setSegmentUrlList(List<String> urls) {
        mSegmentUrlList = urls;
    }

    public List<String> getGlobalSegmentList() {
        return mGlobalSegmentList;
    }

    public void setGlobalSegmentList(List<String> segments) {
        mGlobalSegmentList = segments;
    }

    public String getFormat() {
        return mFormat;
    }

    public boolean isOTF() {
        return mFormat != null && mFormat.equals(FORMAT_STREAM_TYPE_OTF);
    }

    /**
     * Contains segments list and durations (required for stream switch!!!)
     */
    public String getOtfInitUrl() {
        if (mTemplateMetadataUrl == null && getUrl() != null) {
            mTemplateMetadataUrl = getUrl() + "&sq=0";
        }

        return mTemplateMetadataUrl;
    }

    public String getOtfTemplateUrl() {
        if (mTemplateSegmentUrl == null && getUrl() != null) {
            mTemplateSegmentUrl = getUrl() + "&sq=$Number$";
        }

        return mTemplateSegmentUrl;
    }

    public String getLanguage() {
        return mLanguage != null ? mLanguage : getUrlHolder().getLanguage();
    }

    public String getApproxDurationMs() {
        return mApproxDurationMs;
    }

    public int getAverageBitrate() {
        return mAverageBitrate;
    }

    public String getLastModified() {
        return mLastModified;
    }

    public int getTargetDurationSec() {
        return mTargetDurationSec;
    }

    public int getMaxDvrDurationSec() {
        return mMaxDvrDurationSec;
    }

    public VideoUrlHolder getUrlHolder() {
        if (mUrlHolder == null) {
            mUrlHolder = new VideoUrlHolder(mUrl, mCipher, mSignatureCipher);
        }

        return mUrlHolder;
    }

    @NonNull
    public String toString() {
        return String.format(
                "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Width: %s, Height: %s, ITag: %s}",
                getUrl(),
                getSourceURL(),
                getUrlHolder().getSignature(),
                getContentLength(),
                getWidth(),
                getHeight(),
                getITag());
    }

    /**
     * TODO: to be removed. Temp SABR fix
     */
    public boolean isBroken() {
        return Helpers.allNulls(mUrl, mCipher, mSignatureCipher);
    }
}
