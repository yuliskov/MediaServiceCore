package com.liskovsoft.youtubeapi.videoinfo.models.formats;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
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
    public static final String PARAM_CVER = "cver";
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
    private String mEventId;
    private UrlQueryString mUrlQuery;
    private String mLanguage;
    @JsonPath("$.targetDurationSec")
    private int mTargetDurationSec;
    @JsonPath("$.maxDvrDurationSec")
    private int mMaxDvrDurationSec;
    private String mExtractedCipher;
    @JsonPath("$.isDrc")
    private boolean mIsDrc;

    public String getUrl() {
        parseCipher();
        // Bypass query creation if url isn't transformed
        return mUrlQuery != null ? mUrlQuery.toString() : mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getSignatureCipher() {
        parseCipher();
        return mExtractedCipher;
    }

    public void setSignature(String signature) {
        if (signature != null) {
            //if (url.contains(PARAM_SIGNATURE_SPECIAL_MARK)) {
            //    url.set(PARAM_SIGNATURE_SPECIAL, signature);
            //} else {
            //    url.set(PARAM_SIGNATURE, signature);
            //}

            setParam(PARAM_SIGNATURE_SPECIAL, signature);

            mRealSignature = signature;
        }
    }

    public void setClientVersion(String clientVersion) {
        setParam(PARAM_CVER, clientVersion);
    }

    public String getSignature() {
        return mRealSignature;
    }

    public String getThrottleCipher() {
        return getParam(THROTTLE_PARAM);
    }

    public void setThrottleCipher(String throttleCipher) {
        setParam(THROTTLE_PARAM, throttleCipher);
    }

    public void setCpn(String cpn) {
        setParam(PARAM_CPN, cpn);
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
        if (mLanguage == null && getUrlQuery() != null) {
            String xtags = getUrlQuery().get("xtags");

            if (xtags != null) {
                // Example: acont=dubbed:lang=ar
                UrlQueryString xtagsQuery = UrlQueryStringFactory.parse(xtags.replace(":", "&"));
                String lang = xtagsQuery.get("lang");
                String acont = xtagsQuery.get("acont");
                // original, descriptive, dubbed, dubbed-auto, secondary
                mLanguage = lang != null && acont != null ? String.format("%s (%s)", YouTubeHelper.exoNameFix(lang), acont) : lang;
            }
        }

        return mLanguage;
    }

    private void parseCipher() {
        if (mUrl == null) { // items signatures are ciphered
            String cipherUri = mCipher == null ? mSignatureCipher : mCipher;

            if (cipherUri != null) {
                UrlQueryString queryString = UrlQueryStringFactory.parse(cipherUri);
                mUrl = queryString.get(PARAM_URL);
                mExtractedCipher = queryString.get(PARAM_S);
            }
        }
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

    public String getParam(String paramName) {
        UrlQueryString queryString = getUrlQuery();

        if (queryString != null) {
            return queryString.get(paramName);
        }

        return null;
    }

    public void setParam(String paramName, String paramValue) {
        UrlQueryString queryString = getUrlQuery();

        if (queryString != null && paramName != null && paramValue != null) {
            queryString.set(paramName, paramValue);
        }
    }

    private UrlQueryString getUrlQuery() {
        if (mUrl == null) {
            return null;
        }

        if (mUrlQuery == null) {
            mUrlQuery = UrlQueryStringFactory.parse(mUrl);
        }

        return mUrlQuery;
    }

    @NonNull
    public String toString() {
        return String.format(
                "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Width: %s, Height: %s, ITag: %s}",
                getUrl(),
                getSourceURL(),
                getSignature(),
                getContentLength(),
                getWidth(),
                getHeight(),
                getITag());
    }
}
