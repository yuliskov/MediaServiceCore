package com.liskovsoft.youtubeapi.videoinfo.models;

public class TranslatedCaptionTrack extends CaptionTrack {
    public final static String TRANSLATE_MARKER = "*";
    private final CaptionTrack mOriginTrack;
    private final TranslationLanguage mLanguage;
    private final String mTag;

    public TranslatedCaptionTrack(CaptionTrack originTrack, TranslationLanguage language, String tag) {
        mOriginTrack = originTrack;
        mLanguage = language;
        mTag = tag;
    }

    @Override
    public String getBaseUrl() {
        return mOriginTrack.getBaseUrl() + "&tlang=" + mLanguage.getLanguageCode();
    }

    @Override
    public boolean isTranslatable() {
        return mOriginTrack.isTranslatable();
    }

    @Override
    public String getLanguageCode() {
        return mLanguage.getLanguageCode();
    }

    @Override
    public String getVssId() {
        return mOriginTrack.getVssId();
    }

    @Override
    public String getName() {
        return mLanguage.getLanguageName() + (mTag != null ? " " + mTag : "") + TRANSLATE_MARKER;
    }

    @Override
    public String getType() {
        return mOriginTrack.getType();
    }

    @Override
    public String getMimeType() {
        return mOriginTrack.getMimeType();
    }

    @Override
    public String getCodecs() {
        return mOriginTrack.getCodecs();
    }
}
