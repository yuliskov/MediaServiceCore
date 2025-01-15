package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle;
import com.liskovsoft.youtubeapi.videoinfo.models.CaptionTrack;

public class YouTubeMediaSubtitle implements MediaSubtitle {
    private String mBaseUrl;
    private boolean mIsTranslatable;
    private String mLanguageCode;
    private String mVssId;
    private String mName;
    private String mType;
    private String mMimeType;
    private String mCodecs;

    public static MediaSubtitle from(CaptionTrack track) {
        YouTubeMediaSubtitle subtitle = new YouTubeMediaSubtitle();

        subtitle.mBaseUrl = track.getBaseUrl();
        subtitle.mIsTranslatable = track.isTranslatable();
        subtitle.mLanguageCode = track.getLanguageCode();
        subtitle.mVssId = track.getVssId();
        subtitle.mName = track.getName();
        subtitle.mType = track.getType();
        subtitle.mMimeType = track.getMimeType();
        subtitle.mCodecs = track.getCodecs();

        return subtitle;
    }

    @Override
    public String getBaseUrl() {
        return mBaseUrl;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Override
    public boolean isTranslatable() {
        return mIsTranslatable;
    }

    @Override
    public void setTranslatable(boolean translatable) {
        mIsTranslatable = translatable;
    }

    @Override
    public String getLanguageCode() {
        return mLanguageCode;
    }

    @Override
    public void setLanguageCode(String languageCode) {
        mLanguageCode = languageCode;
    }

    @Override
    public String getVssId() {
        return mVssId;
    }

    @Override
    public void setVssId(String vssId) {
        mVssId = vssId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    @Override
    public String getCodecs() {
        return mCodecs;
    }

    @Override
    public void setCodecs(String codecs) {
        mCodecs = codecs;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public void setType(String type) {
        mType = type;
    }
}
