package com.liskovsoft.googleapi.common.helpers;

import androidx.annotation.Nullable;

import com.liskovsoft.googleapi.app.AppConstants;
import com.liskovsoft.googleapi.app.AppService;
import com.liskovsoft.googleapi.common.locale.LocaleManager;
import com.liskovsoft.googleapi.common.models.items.Thumbnail;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.sharedutils.helpers.Helpers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ServiceHelper {
    /**
     * NOTE: Optimal thumbnail index is 3. Lower values cause black borders around images on Chromecast and Sony.
     */
    private static final int OPTIMAL_RES_THUMBNAIL_INDEX = 3;
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

    public static String millisToTimeText(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);
        return hours != 0 ?
                String.format(Locale.US, "%d%s%02d%s%02d", hours, TIME_TEXT_DELIM, minutes, TIME_TEXT_DELIM, seconds) :
                String.format(Locale.US, "%d%s%02d", minutes, TIME_TEXT_DELIM, seconds);
    }

    public static String createQueryTV_UA(String data) {
        return createQuery(AppConstants.JSON_POST_DATA_TEMPLATE_TV, null, data, "uk", "UA");
    }

    public static String createQueryTV(String data) {
        return createQuery(AppConstants.JSON_POST_DATA_TEMPLATE_TV, data);
    }

    public static String createQueryWeb(String data) {
        return createQuery(AppConstants.JSON_POST_DATA_TEMPLATE_WEB, data);
    }

    public static String createQueryMWeb(String data) {
        return createQuery(AppConstants.JSON_POST_DATA_TEMPLATE_MWEB, data);
    }

    public static String createQueryAndroid(String data) {
        return createQuery(AppConstants.JSON_POST_DATA_TEMPLATE_ANDROID, data);
    }

    public static String createQueryKids(String data) {
        return createQuery(AppConstants.JSON_POST_DATA_TEMPLATE_KIDS, data);
    }

    public static String createQuery(String postTemplate, String data) {
        return createQuery(postTemplate, null, data, null, null);
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
                localeManager.getUtcOffsetMinutes(), appService.getVisitorId(), data1 != null ? data1 : "", data2);
    }

    /**
     * Additional video info such as user, published etc.
     */
    public static @Nullable String itemsToInfo(Object... items) {
        return Helpers.combineItems(ITEMS_DIVIDER, items);
    }

    public static String combineText(Object... items) {
        return Helpers.combineItems(null, items);
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

        for (CharSequence item : list) {
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

    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public static String prettyCount(Number number) {
        char[] suffix = {' ', 'K', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.#").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    /**
     * Find optimal thumbnail for tv screen<br/>
     * For Kotlin counterpart see: com.liskovsoft.googleapi.common.models.gen.CommonHelperKt#getOptimalResThumbnailUrl(ThumbnailItem)
     */
    public static String findOptimalResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size > OPTIMAL_RES_THUMBNAIL_INDEX ? OPTIMAL_RES_THUMBNAIL_INDEX : size - 1).getUrl();
    }

    /**
     * For Kotlin counterpart see: com.liskovsoft.youtubeapi.common.models.gen.CommonHelperKt#getHighResThumbnailUrl(ThumbnailItem)
     */
    public static String findHighResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size - 1).getUrl();
    }

    /**
     * Avatar blocking fix
     */
    public static String avatarBlockFix(String url) {
        if (url != null) {
            url = url.replaceFirst("^https://yt3.ggpht.com", "https://yt4.ggpht.com");
        }

        return url;
    }
}
