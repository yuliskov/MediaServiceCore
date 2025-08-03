package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.googlecommon.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.app.AppService;

public class PostDataHelper {
    public static String createQueryTV_UA(String data) {
        return createQuery(AppClient.TV.getBrowseTemplate(), null, data, "uk", "UA");
    }

    public static String createQueryTV(String data) {
        return createQuery(AppClient.TV.getBrowseTemplate(), data);
    }

    /**
     * Contains live chat key
     */
    public static String createQueryTVLegacy(String data) {
        return createQuery(AppClient.TV_LEGACY.getBrowseTemplate(), data);
    }

    public static String createQueryWeb(String data) {
        return createQuery(AppClient.WEB.getBrowseTemplate(), data);
    }

    public static String createQueryMWeb(String data) {
        return createQuery(AppClient.MWEB.getBrowseTemplate(), data);
    }

    public static String createQueryAndroid(String data) {
        return createQuery(AppClient.ANDROID.getBrowseTemplate(), data);
    }

    public static String createQueryKids(String data) {
        return createQuery(AppClient.TV_KIDS.getBrowseTemplate(), data);
    }

    public static String createQueryRemix(String data) {
        return createQuery(AppClient.WEB_REMIX.getBrowseTemplate(), data);
    }

    public static String createQuery(AppClient client, String data) {
        return createQuery(client.getBrowseTemplate(), data);
    }

    public static String createQuery(String postTemplate, String data) {
        return createQuery(postTemplate, null, data, null, null);
    }

    public static String createQuery(AppClient client, String data1, String data2) {
        return createQuery(client.getBaseTemplate(), data1, data2);
    }

    public static String createQuery(String postTemplate, String data1, String data2) {
        return createQuery(postTemplate, data1, data2, null, null);
    }

    private static String createQuery(String postTemplate, String data1, String data2, String language, String country) {
        LocaleManager localeManager = LocaleManager.instance();
        AppService appService = AppService.instance();
        if (language == null) {
            language = localeManager.getLanguage();
        }
        if (country == null) {
            country = localeManager.getCountry();
        }
        return String.format(postTemplate, language, country,
                localeManager.getUtcOffsetMinutes(), appService.getVisitorData(), data1 != null ? data1 : "", data2 != null ? data2 : "");
    }
}
