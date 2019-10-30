package com.liskovsoft.youtubeapi.support.utils;

import java.util.concurrent.TimeUnit;

public class YouTubeHelper {
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
        String result = "";

        for (String item : items) {
            if (item == null) {
                continue;
            }

            if (result.isEmpty()) {
                result = item;
            } else {
                result = String.format("%s %s %s", result, BULLET_SYMBOL, item);
            }
        }

        return result;
    }
}
