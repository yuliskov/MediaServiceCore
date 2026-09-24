package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;

public class TranslatedCaptionTrack extends CaptionTrack {
    public final static String TRANSLATE_MARKER = "*";
    private final CaptionTrack mOriginTrack;
    private final TranslationLanguage mLanguage;

    public TranslatedCaptionTrack(CaptionTrack originTrack, TranslationLanguage language) {
        mOriginTrack = originTrack;
        mLanguage = language;
    }

    @Override
    public String getBaseUrl() {
        // Don't try to translate the same lang or you'll get a mess
        if (Helpers.equals(mOriginTrack.getLanguageCode(), mLanguage.getLanguageCode())) {
            return mOriginTrack.getBaseUrl();
        }

        UrlQueryString baseUrlQuery = UrlQueryStringFactory.parse(mOriginTrack.getBaseUrl());
        if (baseUrlQuery == null) {
            return mOriginTrack.getBaseUrl();
        }

        baseUrlQuery.set("tlang", mLanguage.getLanguageCode());
        baseUrlQuery.set("fmt", CaptionFormat.VTT.name);
        return baseUrlQuery.toString();
    }

    @Override
    public String getMimeType() {
        return CaptionFormat.VTT.mimeType;
    }

    @Override
    public String getCodecs() {
        return CaptionFormat.VTT.codecs;
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
        return mOriginTrack.getVssId() + "." + mLanguage.getLanguageCode();
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



    // Doesn't work!!!!
    private String countryCodeToFlag(String countryCode) {
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
}
