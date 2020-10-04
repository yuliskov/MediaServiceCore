package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

import java.util.concurrent.TimeUnit;

public class AppHelper {
    private static final String BULLET_SYMBOL = "\u2022";
    private static final String TIME_TEXT_DELIM = ":";

    public static String videoIdToFullUrl(String videoId) {
        return String.format("https://www.youtube.com/watch?v=%s", videoId);
    }

    /**
     * Convert YouTube video length text to milliseconds<br/>
     * Example of input text: <b>4:44:51</b>
     * @param lengthText video length
     * @return length in milliseconds
     */
    public static int timeTextToMillis(String lengthText) {
        if (lengthText == null || lengthText.contains(",")) {
            return 0;
        }

        String[] timeParts = lengthText.split(TIME_TEXT_DELIM);

        int hours = timeParts.length == 3 ? Integer.parseInt(timeParts[0]) : 0;
        int minutes = timeParts.length == 3 ? Integer.parseInt(timeParts[1]) : Integer.parseInt(timeParts[0]);
        int seconds = timeParts.length == 3 ? Integer.parseInt(timeParts[2]) : Integer.parseInt(timeParts[1]);

        return (int) (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds));
    }

    public static String itemsToDescription(String... items) {
        return combineItems(items, BULLET_SYMBOL);
    }

    public static String combineText(String... items) {
        return combineItems(items, null);
    }

    public static String createQuery(String template) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(AppConstants.JSON_POST_DATA_TEMPLATE, localeManager.getCountry(), localeManager.getLanguage(), template);
    }

    private static String combineItems(String[] items, String divider) {
        String result = "";

        if (items != null) {
            for (String item : items) {
                if (item == null || item.isEmpty()) {
                    continue;
                }

                if (result.isEmpty()) {
                    result = item;
                } else {
                    if (divider == null) {
                        result = String.format("%s %s", result, item);
                    } else {
                        result = String.format("%s %s %s", result, divider, item);
                    }
                }
            }
        }

        return result;
    }
}
