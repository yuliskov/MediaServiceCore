package com.liskovsoft.youtubeapi.support.utils;

import java.util.concurrent.TimeUnit;

public class YouTubeHelper {
    public static String toFullUrl(String videoId) {
        return String.format("https://www.youtube.com/watch?v=%s", videoId);
    }

    /**
     * Convert YouTube video length text to milliseconds<br/>
     * Example of input text: <b>4:44:51</b>
     * @param lengthText video length
     * @return length in milliseconds
     */
    public static int toMillis(String lengthText) {
        String[] timeParts = lengthText.split(":");

        int hours = timeParts.length == 3 ? Integer.parseInt(timeParts[0]) : 0;
        int minutes = timeParts.length >= 2 ? Integer.parseInt(timeParts[1]) : 0;
        int seconds = timeParts.length >= 2 ? Integer.parseInt(timeParts[2]) : 0;

        return (int) (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds));
    }
}
