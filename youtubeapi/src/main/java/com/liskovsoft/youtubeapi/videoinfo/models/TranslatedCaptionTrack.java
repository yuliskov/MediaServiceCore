package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
import com.liskovsoft.sharedutils.helpers.Helpers;

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
        // Don't try to translate the same lang or you'll get a mess
        if (Helpers.equals(mOriginTrack.getLanguageCode(), mLanguage.getLanguageCode())) {
            return mOriginTrack.getBaseUrl();
        }

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
        // NOTE: tag contain weird chars: (simplified) - chinese (simplified)
        //return mLanguage.getLanguageName() + (mTag != null ? " " + mTag : "") + TRANSLATE_MARKER;

        return YouTubeHelper.exoNameFix(mLanguage.getLanguageName()) + TRANSLATE_MARKER;
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

    // Doesn't work!!!!
    private String countryCodeToFlag(String countryCode) {
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
}
