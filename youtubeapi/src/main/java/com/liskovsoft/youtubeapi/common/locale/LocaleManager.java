package com.liskovsoft.youtubeapi.common.locale;

import java.util.Locale;

public class LocaleManager {
    private static LocaleManager sInstance;
    private final String mLang;
    private final String mCountry;

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
}
