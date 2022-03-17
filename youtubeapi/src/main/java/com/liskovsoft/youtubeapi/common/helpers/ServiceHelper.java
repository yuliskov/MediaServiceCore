package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServiceHelper {
    private static final String BULLET_SYMBOL = "\u2022";
    private static final String ITEMS_DIVIDER = " " + BULLET_SYMBOL + " ";
    private static final String TIME_TEXT_DELIM = ":";

    public static String videoIdToFullUrl(String videoId) {
        if (videoId == null) {
            return null;
        }

        return String.format("https://www.youtube.com/watch?v=%s", videoId);
    }

    public static String channelIdToFullUrl(String channelId) {
        if (channelId == null) {
            return null;
        }

        return String.format("https://www.youtube.com/channel/%s", channelId);
    }

    /**
     * Convert YouTube video length text to milliseconds<br/>
     * Example of input text: <b>4:44:51</b>
     * @param lengthText video length
     * @return length in milliseconds
     */
    public static int timeTextToMillis(String lengthText) {
        if (lengthText == null || lengthText.contains(",")) {
            return -1;
        }

        String[] timeParts = lengthText.split(TIME_TEXT_DELIM);
        int length = timeParts.length;

        // TODO: time conversion doesn't take into account locale specific delimiters (e.g ".", ",")
        int hours = Helpers.parseInt(timeParts, length - 3, 0);
        int minutes = Helpers.parseInt(timeParts, length - 2, 0);
        int seconds = Helpers.parseInt(timeParts, length - 1, 0);

        return (int) (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds));
    }

    public static String createQuery(String template) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(AppConstants.JSON_POST_DATA_TEMPLATE_TV,
                localeManager.getCountry(), localeManager.getLanguage(), localeManager.getUtcOffsetMinutes(), template);
    }

    public static String createQueryWeb(String template) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(AppConstants.JSON_POST_DATA_TEMPLATE_WEB,
                localeManager.getCountry(), localeManager.getLanguage(), localeManager.getUtcOffsetMinutes(), template);
    }

    /**
     * Additional video info such as user, published etc.
     */
    public static String itemsToInfo(Object... items) {
        return combineItems(ITEMS_DIVIDER, items);
    }

    public static String combineText(Object... items) {
        return combineItems(null, items);
    }

    public static String combineItems(String divider, Object... items) {
        StringBuilder result = new StringBuilder();

        if (items != null) {
            for (Object item : items) {
                if (item == null) {
                    continue;
                }

                String strItem = item.toString();

                if (strItem == null || strItem.isEmpty()) {
                    continue;
                }

                if (divider == null || result.length() == 0) {
                    result.append(strItem);
                } else {
                    result.append(divider).append(strItem);
                }
            }
        }

        return result.length() != 0 ? result.toString() : null;
    }

    /**
     * Create absolute url from relative if needed<br/>
     * There was a serious bug when absolute url prepended twice<br/>
     * Also unescapes url for some cases
     */
    public static String tidyUrl(String url) {
        if (url == null) {
            return null;
        }

        url = unescapeUrl(url);

        return url.startsWith("/") ? AppConstants.SCRIPTS_URL_BASE + url : url;
    }

    public static String unescapeUrl(String url) {
        url = url.replace("\\/", "/");
        url = url.replace("\\x3d", "="); // Hexadecimal escape sequences
        return url;
    }

    public static String insertSeparator(String str, String separator, int groupLen) {
        StringBuilder builder = new StringBuilder();

        int index = 0;

        for (int i = 0; i < str.length(); i++) {
            index++;

            builder.append(str.charAt(i));

            if (index % groupLen == 0 && index != str.length()) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    public static boolean atLeastOneEquals(String origin, String... values) {
        if (origin == null || values == null) {
            return false;
        }

        for (String value : values) {
            if (origin.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkNonNull(Object... values) {
        if (values == null) {
            return false;
        }

        for (Object value : values) {
            if (value == null) {
                return false;
            }
        }

        return true;
    }

    public static long toMillis(String currentTimeSec) {
        if (currentTimeSec == null || currentTimeSec.isEmpty()) {
            return 0;
        }

        return (long) (Helpers.parseFloat(currentTimeSec) * 1_000);
    }

    public static String toJsonArrayString(String... list) {
        return toJsonArrayString(Arrays.asList(list));
    }

    public static String toJsonArrayString(Collection<String> list) {
        if (list == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        result.append("[");

        int i = 0;

        for (String item : list) {
            result.append(String.format("\"%s\"", item));

            if (i != (list.size() - 1)) {
                result.append(",");
            }

            i++;
        }

        result.append("]");

        return result.toString();
    }

    public static String getToken(String authorization) {
        if (authorization == null) {
            return null;
        }

        // Remove Bearer prefix
        String[] split = authorization.split("\\s+");

        return split.length == 2 ? split[1] : split[0];
    }

    public static List<MediaGroup> extractEmpty(List<MediaGroup> groups) {
        List<MediaGroup> result = new ArrayList<>();

        for (MediaGroup group : groups) {
            if (group.isEmpty()) {
                result.add(group);
            }
        }

        for (MediaGroup group : result) {
            groups.remove(group);
        }

        return result;
    }

    public interface OnElementCallback<T> {
        void onElement(T element, int i);
    }

    public static <T> void process(List<T> elements, OnElementCallback<T> callback) {
        for (int i = 0; i < elements.size(); i++) {
            callback.onElement(elements.get(i), i);
        }
    }
}
