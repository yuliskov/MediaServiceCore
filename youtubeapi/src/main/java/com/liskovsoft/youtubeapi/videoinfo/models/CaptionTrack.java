package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class CaptionTrack {
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
    /**
     * Example: see {@link Name} class
     */
    @JsonPath("$.name")
    private Name2 mName;
    private String mMimeType;
    private String mCodecs;

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public boolean isTranslatable() {
        return mIsTranslatable;
    }

    public void setTranslatable(boolean translatable) {
        mIsTranslatable = translatable;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        mLanguageCode = languageCode;
    }

    public String getVssId() {
        return mVssId;
    }

    public void setVssId(String vssId) {
        mVssId = vssId;
    }

    public String getName() {
        if (mName == null || mName.getTitles() == null) {
            return null;
        }

        return mName.getTitles()[0].getText();
    }

    public void setName(String name) {
        if (mName == null || mName.getTitles() == null) {
            return;
        }

        mName.getTitles()[0].setText(name);
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    public String getCodecs() {
        return mCodecs;
    }

    public void setCodecs(String codecs) {
        mCodecs = codecs;
    }

    public class Name {
        /**
         * Example: "English+-+en"
         */
        @JsonPath("$.simpleText")
        private String mSimpleText;

        public String getSimpleText() {
            return mSimpleText;
        }

        public void setSimpleText(String simpleText) {
            mSimpleText = simpleText;
        }
    }

    public class Name2 {
        @JsonPath("$.runs[*]")
        private Title[] mTitles;

        public Title[] getTitles() {
            return mTitles;
        }

        private class Title {
            @JsonPath("$.text")
            private String mText;

            public String getText() {
                return mText;
            }

            public void setText(String text) {
                mText = text;
            }
        }
    }
}
