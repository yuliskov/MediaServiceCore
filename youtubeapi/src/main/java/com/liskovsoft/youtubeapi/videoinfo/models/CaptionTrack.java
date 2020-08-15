package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class CaptionTrack {
    private static final String VTT_MIME_TYPE = "text/vtt";
    private static final String VTT_PARAM = "vtt";
    private static final String VTT_CODECS = "wvtt";

    /**
     * Example: "https://www.youtube.com/api/timedtext?caps=&key=yt…&sparams=caps%2Cv%2Cxorp%2Cexpire&lang=en&name=en"
     */
    @JsonPath("$.baseUrl")
    private String mBaseUrl;
    /**
     * Example: true
     */
    @JsonPath("$.isTranslatable")
    private boolean mIsTranslatable;
    /**
     * Example: "en"
     */
    @JsonPath("$.languageCode")
    private String mLanguageCode;
    /**
     * Example: ".en.nP7-2PuUl7o"
     */
    @JsonPath("$.vssId")
    private String mVssId;
    @JsonPath("$.name.simpleText")
    private String mName;
    /**
     * E.g. asr (Automatic Speech Recognition)
     */
    @JsonPath("$.kind")
    private String mType;
    private String mMimeType = VTT_MIME_TYPE;
    private String mCodecs = VTT_CODECS;

    public String getBaseUrl() {
        return String.format("%s&fmt=%s", mBaseUrl, VTT_PARAM);
    }

    public boolean isTranslatable() {
        return mIsTranslatable;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public String getVssId() {
        return mVssId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public String getCodecs() {
        return mCodecs;
    }
}
