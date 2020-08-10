package com.liskovsoft.youtubeapi.service;

import androidx.annotation.NonNull;
import com.liskovsoft.mediaserviceinterfaces.MediaFormat;
import com.liskovsoft.youtubeapi.formatbuilders.misc.ITag;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;

import java.util.List;

public class YouTubeMediaFormat implements MediaFormat {
    private String mIndex;
    private String mIndexRange;
    private String mUrl;
    private String mSignatureCipher;
    private String mType;
    private String mITag;
    private String mClen;
    private String mBitrate;
    private String mProjectionType;
    private String mXtags;
    private String mSize;
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

    public static MediaFormat from(AdaptiveVideoFormat format) {
        YouTubeMediaFormat mediaFormat = new YouTubeMediaFormat();
        mediaFormat.mIndex = format.getIndex();
        mediaFormat.mIndexRange = format.getIndexRange().toString();
        mediaFormat.mUrl = format.getUrl();
        mediaFormat.mSignatureCipher = format.getSignatureCipher();
        mediaFormat.mType = format.getType();
        String iTag = format.getITag() == 0 ? "" : String.valueOf(format.getITag());
        mediaFormat.mITag = iTag;
        mediaFormat.mClen = format.getContentLength();
        String bitrate = format.getBitrate() == 0 ? "" : String.valueOf(format.getBitrate());
        mediaFormat.mBitrate = bitrate;
        mediaFormat.mSize = format.getSize();
        mediaFormat.mInit = format.getInit();
        String fps = format.getFps() == 0 ? "" : String.valueOf(format.getFps());
        mediaFormat.mFps = fps;
        mediaFormat.mFormat = format.getFormat();
        mediaFormat.mIsOtf = format.isOTF();
        mediaFormat.mOtfInitUrl = format.getOtfInitUrl();
        mediaFormat.mOtfTemplateUrl = format.getOtfTemplateUrl();
        mediaFormat.mSourceUrl = format.getSourceURL();
        mediaFormat.mSegmentUrlList = format.getSegmentUrlList();
        mediaFormat.mGlobalSegmentList = format.getGlobalSegmentList();

        return mediaFormat;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String getSignatureCipher() {
        return mSignatureCipher;
    }

    @Override
    public void setSignatureCipher(String cipher) {
        mSignatureCipher = cipher;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public void setType(String type) {
        mType = type;
    }

    @Override
    public String getITag() {
        return mITag;
    }

    @Override
    public void setITag(String itag) {
        mITag = itag;
    }

    @Override
    public String getClen() {
        return mClen;
    }

    @Override
    public void setClen(String clen) {
        mClen = clen;
    }

    @Override
    public String getBitrate() {
        return mBitrate;
    }

    @Override
    public void setBitrate(String bitrate) {
        mBitrate = bitrate;
    }

    @Override
    public String getProjectionType() {
        return mProjectionType;
    }

    @Override
    public void setProjectionType(String projectionType) {
        mProjectionType = projectionType;
    }

    @Override
    public String getXtags() {
        return mXtags;
    }

    @Override
    public void setXtags(String xtags) {
        mXtags = xtags;
    }

    @Override
    public String getSize() {
        return mSize;
    }

    @Override
    public void setSize(String size) {
        mSize = size;
    }

    @Override
    public String getIndex() {
        return mIndex;
    }

    @Override
    public void setIndex(String index) {
        mIndex = index;
    }

    @Override
    public String getInit() {
        return mInit;
    }

    @Override
    public void setInit(String init) {
        mInit = init;
    }

    @Override
    public String getFps() {
        return mFps;
    }

    @Override
    public void setFps(String fps) {
        mFps = fps;
    }

    @Override
    public String getLmt() {
        return mLmt;
    }

    @Override
    public void setLmt(String lmt) {
        mLmt = lmt;
    }

    @Override
    public String getQualityLabel() {
        return mQualityLabel;
    }

    @Override
    public void setQualityLabel(String qualityLabel) {
        mQualityLabel = qualityLabel;
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
    public void setQuality(String quality) {

    }

    @Override
    public boolean belongsToType(String type) {
        return false;
    }

    @Override
    public void setSignature(String signature) {

    }

    @Override
    public String getSignature() {
        return null;
    }

    @Override
    public void setAudioSamplingRate(String audioSamplingRate) {

    }

    @Override
    public String getAudioSamplingRate() {
        return null;
    }

    @Override
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

    @Override
    public void setSegmentUrlList(List<String> urls) {
        mSegmentUrlList = urls;
    }

    @Override
    public List<String> getGlobalSegmentList() {
        return mGlobalSegmentList;
    }

    @Override
    public void setGlobalSegmentList(List<String> segments) {
        mGlobalSegmentList = segments;
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

        return ITag.compare(getITag(), format.getITag());
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Size: %s, ITag: %s}",
                getUrl(),
                getSourceUrl(),
                getSignature(),
                getClen(),
                getSize(),
                getITag());
    }
}
