package com.liskovsoft.youtubeapi.videoinfo.models.formats;

import androidx.annotation.NonNull;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.formatbuilders.utils.ITagUtils;

import java.util.List;

public class VideoFormat {
    private static final String FORMAT_STREAM_TYPE_OTF = "FORMAT_STREAM_TYPE_OTF";

    // Common params
    public static final String PARAM_URL = "url";
    public static final String PARAM_S = "s";
    public static final String PARAM_CIPHER = "cipher";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_ITAG = "itag";
    public static final String PARAM_CPN = "cpn";
    public static final String PARAM_SIGNATURE = "signature";
    public static final String PARAM_SIGNATURE_SPECIAL = "sig";
    public static final String PARAM_SIGNATURE_SPECIAL_MARK = "lsig";
    public static final String PARAM_EVENT_ID = "ei";
    // End Common params

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
    private static final String THROTTLE_PARAM = "n";
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
    private int mWidth;
    @JsonPath("$.height")
    private int mHeight;
    private String mSize;
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
    private String mEventId;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getSignatureCipher() {
        return parseCipher();
    }

    public void setSignature(String signature) {
        if (signature != null) {
            UrlQueryString url = UrlQueryStringFactory.parse(mUrl);

            //if (url.contains(PARAM_SIGNATURE_SPECIAL_MARK)) {
            //    url.set(PARAM_SIGNATURE_SPECIAL, signature);
            //} else {
            //    url.set(PARAM_SIGNATURE, signature);
            //}

            url.set(PARAM_SIGNATURE_SPECIAL, signature);

            mUrl = url.toString();

            mRealSignature = signature;
        }
    }

    public String getSignature() {
        return mRealSignature;
    }

    public String getThrottleCipher() {
        if (mUrl != null) {
            UrlQueryString url = UrlQueryStringFactory.parse(mUrl);

            return url.get(THROTTLE_PARAM);
        }

        return null;
    }

    public void setThrottleCipher(String throttleCipher) {
        if (mUrl != null && throttleCipher != null) {
            UrlQueryString url = UrlQueryStringFactory.parse(mUrl);

            url.set(THROTTLE_PARAM, throttleCipher);

            mUrl = url.toString();
        }
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

    public String getSize() {
        if (mSize == null && mWidth != 0 && mHeight != 0) {
            return String.format("%sx%s", mWidth, mHeight);
        }

        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
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

    private String parseCipher() {
        String result = null;

        if (mUrl == null) { // items signatures are ciphered
            String cipherUri = mCipher == null ? mSignatureCipher : mCipher;

            if (cipherUri != null) {
                UrlQueryString queryString = UrlQueryStringFactory.parse(cipherUri);
                mUrl = queryString.get(PARAM_URL);
                result = queryString.get(PARAM_S);
            }
        }

        return result;
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

    @NonNull
    public String toString() {
        return String.format(
                "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Size: %s, ITag: %s}",
                getUrl(),
                getSourceURL(),
                getSignature(),
                getContentLength(),
                getSize(),
                getITag());
    }
}
