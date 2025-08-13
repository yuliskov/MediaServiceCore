package com.liskovsoft.googlecommon.common.helpers;

import android.annotation.SuppressLint;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.text.BidiFormatter;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.AppConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceHelper {
    private static final String BULLET_SYMBOL = "\u2022";
    private static final String ITEMS_DIVIDER = " " + BULLET_SYMBOL + " ";
    private static final String TIME_TEXT_DELIM = ":";
    // Regex to extract hours, minutes, and seconds
    private static final Pattern sIsoDurationPattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?");

    @Nullable
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
    public static long timeTextToMillis(String lengthText) {
        if (lengthText == null || lengthText.contains(",")) {
            return -1;
        }

        String[] timeParts = lengthText.split(TIME_TEXT_DELIM);
        int length = timeParts.length;

        // TODO: time conversion doesn't take into account locale specific delimiters (e.g ".", ",")
        int hours = Helpers.parseInt(timeParts, length - 3, 0);
        int minutes = Helpers.parseInt(timeParts, length - 2, 0);
        int seconds = Helpers.parseInt(timeParts, length - 1, 0);

        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
    }

    public static String millisToTimeText(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);
        return hours != 0 ?
                String.format(Locale.US, "%d%s%02d%s%02d", hours, TIME_TEXT_DELIM, minutes, TIME_TEXT_DELIM, seconds) :
                String.format(Locale.US, "%d%s%02d", minutes, TIME_TEXT_DELIM, seconds);
    }

    /**
     * Additional video info such as user, published etc.
     */
    public static @Nullable CharSequence createInfo(Object... items) {
        return combineItems(ITEMS_DIVIDER, items);
    }

    public static @Nullable CharSequence combineText(Object... items) {
        return combineItems(null, items);
    }

    /**
     * NOTE: ADDS SPECIAL BIDI CHARS. DON'T USE THIS INSIDE JSON
     */
    public static @Nullable CharSequence combineItems(CharSequence divider, Object... items) {
        SpannableStringBuilder result = new SpannableStringBuilder();

        if (items != null) {
            for (Object item : items) {
                if (item == null) {
                    continue;
                }

                CharSequence strItem = item instanceof CharSequence ? (CharSequence) item : item.toString();

                if (TextUtils.isEmpty(strItem)) {
                    continue;
                }

                // Fix mixed RTL and LTR content
                strItem = BidiFormatter.getInstance().unicodeWrap(strItem);

                if (divider == null || result.length() == 0) {
                    result.append(strItem);
                } else {
                    result.append(divider).append(strItem);
                }
            }
        }

        return result.length() != 0 ? result : null;
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
    
    @SuppressLint("DefaultLocale")
    public static String convertIsoDurationToHHMMSS(String duration) {
        if (duration == null) {
            return null;
        }

        Matcher matcher = sIsoDurationPattern.matcher(duration);

        if (matcher.matches()) {
            // Extract hours, minutes, and seconds (default to 0 if null)
            String hours = matcher.group(1) != null ? matcher.group(1) : "0";
            String minutes = matcher.group(2) != null ? matcher.group(2) : "0";
            String seconds = matcher.group(3) != null ? matcher.group(3) : "0";

            // Parse integers
            int hoursInt = Integer.parseInt(hours);
            int minutesInt = Integer.parseInt(minutes);
            int secondsInt = Integer.parseInt(seconds);

            // Build the duration string dynamically
            if (hoursInt > 0) {
                return String.format("%d:%02d:%02d", hoursInt, minutesInt, secondsInt);
            } else {
                return String.format("%d:%02d", minutesInt, secondsInt);
            }
        }

        return null;
    }

    public static String generateRandomId(int length) {
        String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomId = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            randomId.append(charPool.charAt(index));
        }

        return randomId.toString();
    }
}
