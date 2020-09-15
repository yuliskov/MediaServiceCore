package com.liskovsoft.youtubeapi.common.locale;

import java.util.Locale;

public class LocaleManager {
    private static LocaleManager sInstance;
    private String mLang;
    private String mCountry;

    private LocaleManager() {
        Locale defaultLocale = Locale.getDefault();
        mLang = defaultLocale.getLanguage();
        mCountry = defaultLocale.getCountry();
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
}
