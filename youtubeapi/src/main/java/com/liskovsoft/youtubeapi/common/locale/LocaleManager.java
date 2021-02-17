package com.liskovsoft.youtubeapi.common.locale;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LocaleManager {
    private static LocaleManager sInstance;
    private String mLang;
    private String mCountry;
    private int mOffsetFromUtcMinutes;

    private LocaleManager() {
        initLang();
        initUtcOffset();
    }

    private void initLang() {
        Locale defaultLocale = Locale.getDefault();
        mLang = defaultLocale.toLanguageTag();
        mCountry = defaultLocale.getCountry();
    }

    private void initUtcOffset() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        mOffsetFromUtcMinutes = tz.getOffset(now.getTime()) / 1_000 / 60;
    }

    public static LocaleManager instance() {
        if (sInstance == null) {
            sInstance = new LocaleManager();
        }

        return sInstance;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getLanguage() {
        return mLang;
    }

    public void setLanguage(String lang) {
        mLang = lang;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public int getUtcOffsetMinutes() {
        return mOffsetFromUtcMinutes;
    }
}
