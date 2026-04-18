package com.liskovsoft.mediaserviceinterfaces.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses YouTube "published X ago" / "vor X" strings into approximate unix time (ms).
 * Used when the API exposes only human-readable time, not millis.
 */
public final class RelativePublishedTime {
    private static final long MS_DAY = 24L * 60 * 60 * 1000;
    private static final Pattern PATTERN_EN_AGO = Pattern.compile(
            "(\\d+)\\s*(second|minute|hour|day|week|month|year)s?\\s+ago",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_DE_VOR = Pattern.compile(
            "vor\\s*(\\d+)\\s*(sekunden?|minuten?|stunden?|tagen?|wochen?|monaten?|jahren?|monat|jahr)",
            Pattern.CASE_INSENSITIVE);

    private RelativePublishedTime() {
    }

    public static long publishedTimeTextToUnixMs(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        String s = text.trim();
        String lower = s.toLowerCase(Locale.ENGLISH);
        for (String p : new String[] {"streamed ", "premiered ", "scheduled for ", "starts "}) {
            if (lower.startsWith(p)) {
                s = s.substring(p.length()).trim();
                lower = s.toLowerCase(Locale.ENGLISH);
                break;
            }
        }
        long spanMs = parseRelativeAgoSpanMs(s);
        if (spanMs < 0) {
            return 0;
        }
        if (spanMs == 0) {
            return System.currentTimeMillis();
        }
        return System.currentTimeMillis() - spanMs;
    }

    private static long parseRelativeAgoSpanMs(String s) {
        if (s == null || s.isEmpty()) {
            return -1;
        }
        String t = s.toLowerCase(Locale.ROOT).trim();
        if (t.contains("just now") || t.contains("moments ago")) {
            return 0;
        }
        if (t.equals("yesterday") || t.equals("gestern")) {
            return MS_DAY;
        }

        Matcher m = PATTERN_EN_AGO.matcher(t);
        if (m.find()) {
            return englishUnitToSpanMs(Integer.parseInt(m.group(1)), m.group(2).toLowerCase(Locale.ROOT));
        }
        m = PATTERN_DE_VOR.matcher(t);
        if (m.find()) {
            return germanWordToSpanMs(Integer.parseInt(m.group(1)), m.group(2).toLowerCase(Locale.ROOT));
        }
        return -1;
    }

    private static long englishUnitToSpanMs(int n, String unit) {
        if (unit.startsWith("second")) {
            return n * 1000L;
        }
        if (unit.startsWith("minute")) {
            return n * 60_000L;
        }
        if (unit.startsWith("hour")) {
            return n * 3_600_000L;
        }
        if (unit.startsWith("day")) {
            return n * MS_DAY;
        }
        if (unit.startsWith("week")) {
            return n * 7L * MS_DAY;
        }
        if (unit.startsWith("month")) {
            return n * 30L * MS_DAY;
        }
        if (unit.startsWith("year")) {
            return n * 365L * MS_DAY;
        }
        return -1;
    }

    private static long germanWordToSpanMs(int n, String w) {
        if (w.startsWith("sekund")) {
            return n * 1000L;
        }
        if (w.startsWith("minut")) {
            return n * 60_000L;
        }
        if (w.startsWith("stund")) {
            return n * 3_600_000L;
        }
        if (w.startsWith("tag")) {
            return n * MS_DAY;
        }
        if (w.startsWith("woch")) {
            return n * 7L * MS_DAY;
        }
        if (w.startsWith("monat")) {
            return n * 30L * MS_DAY;
        }
        if (w.startsWith("jahr")) {
            return n * 365L * MS_DAY;
        }
        return -1;
    }
}
