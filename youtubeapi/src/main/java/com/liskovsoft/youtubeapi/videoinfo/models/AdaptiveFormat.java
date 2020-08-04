package com.liskovsoft.youtubeapi.videoinfo.models;

import androidx.annotation.NonNull;
import com.liskovsoft.sharedutils.querystringparser.MyQueryString;
import com.liskovsoft.sharedutils.querystringparser.MyQueryStringFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.videoinfo.old.misc.ITag;

import java.util.List;

public class AdaptiveFormat {
    private static final String FORMAT_STREAM_TYPE_OTF = "FORMAT_STREAM_TYPE_OTF";

    // Common params
    public static final String URL = "url";
    public static final String CIPHER = "cipher";
    public static final String TYPE = "type";
    public static final String ITAG = "itag";
    public static final String S = "s";
    public static final String SIGNATURE = "signature";
    public static final String SIGNATURE2 = "sig";
    public static final String SIGNATURE2_MARK = "lsig";
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
    // End Regular params

    private static class Range {
        @JsonPath("$.start")
        String start;
        @JsonPath("$.end")
        String end;

        @NonNull
        public String toString() {
            if (start == null || end == null) {
                return "";
            }

            return String.format("%s-%s", start, end);
        }
    }
    @JsonPath("$.itag")
    private String mITag;
    @JsonPath("$.url")
    private String mUrl;
    @JsonPath("$.cipher")
    private String mCipher;
    private String mSignatureCiphered;
    @JsonPath("$.mimeType")
    private String mType;
    @JsonPath("$.contentLength")
    private String mClen;
    @JsonPath("$.bitrate")
    private String mBitrate;
    private String mProjectionType;
    private String mXtags;
    @JsonPath("$.width")
    private String mWidth;
    @JsonPath("$.height")
    private String mHeight;
    private String mSize;
    @JsonPath("$.indexRange")
    private Range mIndexRange;
    @JsonPath("$.initRange")
    private Range mInitRange;
    private String mIndex;
    private String mInit;
    @JsonPath("$.fps")
    private String mFps;
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
    @JsonPath("$.signatureCipher")
    private String mSignatureCipher;
    private String mTemplateMetadataUrl;
    private String mTemplateSegmentUrl;

    //@Override
    //public boolean equals(Object obj) {
    //    if (obj == null) {
    //        return false;
    //    }
    //    if (!(obj instanceof MediaItem)) {
    //        return false;
    //    }
    //    MediaItem rightItem = (MediaItem) obj;
    //    return getITag().equals(rightItem.getITag());
    //}

    public String getUrl() {
        parseCipher();

        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getSignatureCipher() {
        parseCipher();

        return mSignatureCiphered;
    }

    public void setSignatureCipher(String s) {
        mSignatureCiphered = s;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getITag() {
        return mITag;
    }

    public void setITag(String itag) {
        mITag = itag;
    }

    public String getClen() {
        return mClen;
    }

    public void setClen(String clen) {
        mClen = clen;
    }

    public String getBitrate() {
        return mBitrate;
    }

    public void setBitrate(String bitrate) {
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
        if (mSize == null && mWidth != null && mHeight != null) {
            return String.format("%sx%s", mWidth, mHeight);
        }

        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getIndex() {
        if (mIndex == null && mIndexRange != null) {
            mIndex = mIndexRange.toString();
        }

        return mIndex;
    }

    public void setIndex(String index) {
        mIndex = index;
    }

    public String getInit() {
        if (mInit == null && mInitRange != null) {
            mInit = mInitRange.toString();
        }

        return mInit;
    }

    public void setInit(String init) {
        mInit = init;
    }

    public String getFps() {
        return mFps;
    }

    public void setFps(String fps) {
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
        return type == null || ITag.belongsToType(type, getITag());
    }

    public void setSignature(String signature) {
        mRealSignature = signature;
    }

    public String getSignature() {
        return mRealSignature;
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

    private void parseCipher() {
        if (mUrl == null && mSignatureCiphered == null) {
            String cipherUri = mCipher == null ? mSignatureCipher : mCipher;

            if (cipherUri != null) {
                MyQueryString queryString = MyQueryStringFactory.parse(cipherUri);
                mUrl = queryString.get(URL);
                mSignatureCiphered = queryString.get(S);
            }
        }
    }

    //public int compareTo(MediaItem item) {
    //    if (item == null) {
    //        return 1;
    //    }
    //
    //    return ITag.compare(getITag(), item.getITag());
    //}

    @NonNull
    public String toString() {
        return String.format(
                "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Size: %s, ITag: %s}",
                getUrl(),
                getSourceURL(),
                getSignature(),
                getClen(),
                getSize(),
                getITag());
    }
}
