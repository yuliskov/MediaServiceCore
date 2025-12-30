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
    public boolean isTranslatable() {
        return mIsTranslatable;
    }

    @Override
    public String getLanguageCode() {
        return mLanguageCode;
    }

    @Override
    public String getVssId() {
        return mVssId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }

    @Override
    public String getCodecs() {
        return mCodecs;
    }

    @Override
    public String getType() {
        return mType;
    }
}
