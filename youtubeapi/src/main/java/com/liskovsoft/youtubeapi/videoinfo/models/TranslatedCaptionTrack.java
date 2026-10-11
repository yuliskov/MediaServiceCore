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
        String originBase = getBaseLanguage(mOriginTrack.getLanguageCode());
        String transBase = getBaseLanguage(mLanguage.getLanguageCode());
        if (!originBase.isEmpty() && originBase.equalsIgnoreCase(transBase)) {
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



    private static String getBaseLanguage(String lang) {
        if (lang == null) {
            return "";
        }
        int idx = lang.indexOf('-');
        if (idx != -1) {
            lang = lang.substring(0, idx);
        }
        idx = lang.indexOf('_');
        if (idx != -1) {
            lang = lang.substring(0, idx);
        }
        return lang.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
