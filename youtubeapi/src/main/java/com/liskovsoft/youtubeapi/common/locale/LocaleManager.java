package com.liskovsoft.youtubeapi.common.locale;

import android.os.Build.VERSION;

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

        if (VERSION.SDK_INT >= 21) {
            // Use BCP-47 code for language code to get content with correct language.
            // For example, BCP-47 has zn-CN for simplified Chinese and zh-TW for traditional Chinese.
            mLang = defaultLocale.toLanguageTag();
        } else {
            mLang = defaultLocale.getLanguage();
        }

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

    public static void unhold() {
        sInstance = null;
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
