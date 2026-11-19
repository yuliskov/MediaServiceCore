package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.data.SearchOptions;
import com.liskovsoft.sharedutils.helpers.Helpers;

import java.io.ByteArrayOutputStream;

/**
 * Builds the InnerTube search {@code params} (the "sp" value of the web UI) from {@link SearchOptions}.
 * <p>
 * The value is a protobuf message, base64 encoded:
 * <pre>
 * message Params {
 *   int32 sort = 1;            // 1 rating, 2 upload date, 3 view count (0/absent = relevance)
 *   Filters filters = 2;
 * }
 * message Filters {
 *   int32 uploadDate = 1;      // 1 hour, 2 today, 3 week, 4 month, 5 year
 *   int32 type = 2;            // 1 video, 2 channel, 3 playlist, 4 movie
 *   int32 duration = 3;        // 1 under 4 min, 3 4-20 min, 2 over 20 min
 *   int32 live = 8;
 *   int32 is4k = 14;
 *   int32 hdr = 25;
 * }
 * </pre>
 * Any combination of options is encodable. (This used to be a lookup table that covered only a handful of
 * combinations; anything else silently fell back to unfiltered results.)
 */
final class SearchFilterHelper {
    private SearchFilterHelper() {
    }

    private static final char[] BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static String toParams(int options) {
        if (options == -1 || options == 0) {
            return null;
        }

        int sort = getSort(options);
        int uploadDate = getUploadDate(options);
        int duration = getDuration(options);
        int type = getType(options);

        // Date, duration and the view/rating sorts have always been sent together with type=video. Keep that.
        if (type == 0 && (uploadDate != 0 || duration != 0 || sort == 1 || sort == 3)) {
            type = 1;
        }

        ByteArrayOutputStream filters = new ByteArrayOutputStream();
        writeVarintField(filters, 1, uploadDate);
        writeVarintField(filters, 2, type);
        writeVarintField(filters, 3, duration);
        writeVarintField(filters, 8, Helpers.check(options, SearchOptions.FEATURE_LIVE) ? 1 : 0);
        writeVarintField(filters, 14, Helpers.check(options, SearchOptions.FEATURE_4K) ? 1 : 0);
        writeVarintField(filters, 25, Helpers.check(options, SearchOptions.FEATURE_HDR) ? 1 : 0);

        ByteArrayOutputStream params = new ByteArrayOutputStream();
        writeVarintField(params, 1, sort);
        if (filters.size() > 0) {
            writeVarint(params, (2 << 3) | 2);
            writeVarint(params, filters.size());
            byte[] bytes = filters.toByteArray();
            params.write(bytes, 0, bytes.length);
        }

        if (params.size() == 0) {
            return null;
        }

        return base64UrlSafeEscaped(params.toByteArray());
    }

    private static int getSort(int options) {
        if (Helpers.check(options, SearchOptions.SORT_BY_UPLOAD_DATE)) {
            return 2;
        } else if (Helpers.check(options, SearchOptions.SORT_BY_VIEW_COUNT)) {
            return 3;
        } else if (Helpers.check(options, SearchOptions.SORT_BY_RATING)) {
            return 1;
        }

        return 0;
    }

    private static int getUploadDate(int options) {
        if (Helpers.check(options, SearchOptions.UPLOAD_DATE_LAST_HOUR)) {
            return 1;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_TODAY)) {
            return 2;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_WEEK)) {
            return 3;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_MONTH)) {
            return 4;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_YEAR)) {
            return 5;
        }

        return 0;
    }

    private static int getDuration(int options) {
        if (Helpers.check(options, SearchOptions.DURATION_UNDER_4)) {
            return 1;
        } else if (Helpers.check(options, SearchOptions.DURATION_BETWEEN_4_20)) {
            return 3;
        } else if (Helpers.check(options, SearchOptions.DURATION_OVER_20)) {
            return 2;
        }

        return 0;
    }

    private static int getType(int options) {
        if (Helpers.check(options, SearchOptions.TYPE_VIDEO)) {
            return 1;
        } else if (Helpers.check(options, SearchOptions.TYPE_CHANNEL)) {
            return 2;
        } else if (Helpers.check(options, SearchOptions.TYPE_PLAYLIST)) {
            return 3;
        } else if (Helpers.check(options, SearchOptions.TYPE_MOVIE)) {
            return 4;
        }

        return 0;
    }

    /** Writes a varint field; zero means "not set" and is skipped. */
    private static void writeVarintField(ByteArrayOutputStream out, int fieldNumber, int value) {
        if (value == 0) {
            return;
        }

        writeVarint(out, fieldNumber << 3); // wire type 0
        writeVarint(out, value);
    }

    private static void writeVarint(ByteArrayOutputStream out, int value) {
        while ((value & ~0x7F) != 0) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.write(value);
    }

    /** Standard base64 with padding, then URL-escaped the way the old lookup table was ('=' as %3D). */
    private static String base64UrlSafeEscaped(byte[] data) {
        StringBuilder sb = new StringBuilder((data.length + 2) / 3 * 4 + 8);

        for (int i = 0; i < data.length; i += 3) {
            int b0 = data[i] & 0xFF;
            int b1 = i + 1 < data.length ? data[i + 1] & 0xFF : 0;
            int b2 = i + 2 < data.length ? data[i + 2] & 0xFF : 0;

            sb.append(escape(BASE64[b0 >> 2]));
            sb.append(escape(BASE64[((b0 & 0x3) << 4) | (b1 >> 4)]));
            sb.append(i + 1 < data.length ? escape(BASE64[((b1 & 0xF) << 2) | (b2 >> 6)]) : "%3D");
            sb.append(i + 2 < data.length ? escape(BASE64[b2 & 0x3F]) : "%3D");
        }

        return sb.toString();
    }

    private static String escape(char c) {
        switch (c) {
            case '+':
                return "%2B";
            case '/':
                return "%2F";
            default:
                return String.valueOf(c);
        }
    }
}
