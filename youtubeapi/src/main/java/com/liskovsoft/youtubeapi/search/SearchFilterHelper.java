package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.data.SearchOptions;
import com.liskovsoft.sharedutils.helpers.Helpers;

class SearchFilterHelper {
    private static final int MAX_PARAMS = 6;

    private static final int UPLOAD_DATE_ALL = 0;
    private static final int UPLOAD_DATE_LAST_HOUR = 1;
    private static final int UPLOAD_DATE_TODAY = 2;
    private static final int UPLOAD_DATE_THIS_WEEK = 3;
    private static final int UPLOAD_DATE_THIS_MONTH = 4;
    private static final int UPLOAD_DATE_THIS_YEAR = 5;

    private static final int DURATION_ANY = 0;
    private static final int DURATION_UNDER_4 = 1;
    private static final int DURATION_BETWEEN_4_20 = 2;
    private static final int DURATION_OVER_20 = 3;

    private static final int TYPE_ANY = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_CHANNEL = 2;
    private static final int TYPE_PLAYLIST = 3;
    private static final int TYPE_MOVIE = 4;

    private static final int FEATURE_ANY = 0;
    private static final int FEATURE_LIVE = 1;
    private static final int FEATURE_4K = 2;
    private static final int FEATURE_HDR = 3;

    private static final String[][][][] PARAMS = new String[MAX_PARAMS][MAX_PARAMS][MAX_PARAMS][MAX_PARAMS];

    static {
        // Single criteria

        PARAMS[UPLOAD_DATE_LAST_HOUR][DURATION_ANY][TYPE_ANY][FEATURE_ANY] = "EgQIARAB";
        PARAMS[UPLOAD_DATE_TODAY][DURATION_ANY][TYPE_ANY][FEATURE_ANY] = "EgQIAhAB";
        PARAMS[UPLOAD_DATE_THIS_WEEK][DURATION_ANY][TYPE_ANY][FEATURE_ANY] = "EgQIAxAB";
        PARAMS[UPLOAD_DATE_THIS_MONTH][DURATION_ANY][TYPE_ANY][FEATURE_ANY] = "EgQIBBAB";
        PARAMS[UPLOAD_DATE_THIS_YEAR][DURATION_ANY][TYPE_ANY][FEATURE_ANY] = "EgQIBRAB";

        PARAMS[UPLOAD_DATE_ALL][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY] = "EgQQARgB";
        PARAMS[UPLOAD_DATE_ALL][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY] = "EgQQARgD";
        PARAMS[UPLOAD_DATE_ALL][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY] = "EgQQARgC";

        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_VIDEO][FEATURE_ANY] = "EgIQAQ%3D%3D";
        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_CHANNEL][FEATURE_ANY] = "EgIQAg%3D%3D";
        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_PLAYLIST][FEATURE_ANY] = "EgIQAw%3D%3D";
        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_MOVIE][FEATURE_ANY] = "EgIQBA%3D%3D";

        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_LIVE] = "EgQQAUAB";
        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_4K] = "EgQQAXAB";
        PARAMS[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_HDR] = "EgUQAcgBAQ%3D%3D";

        // Various combinations

        PARAMS[UPLOAD_DATE_TODAY][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY] = "EgYIAhABGAE%3D";
        PARAMS[UPLOAD_DATE_TODAY][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY] = "EgYIAhABGAM%3D";
        PARAMS[UPLOAD_DATE_TODAY][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY] = "EgYIAhABGAI%3D";

        PARAMS[UPLOAD_DATE_THIS_WEEK][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY] = "EgYIAxABGAE%3D";
        PARAMS[UPLOAD_DATE_THIS_WEEK][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY] = "EgYIAxABGAM%3D";
        PARAMS[UPLOAD_DATE_THIS_WEEK][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY] = "EgYIAxABGAI%3D";

        PARAMS[UPLOAD_DATE_THIS_MONTH][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY] = "EgYIBBABGAE%3D";
        PARAMS[UPLOAD_DATE_THIS_MONTH][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY] = "EgYIBBABGAM%3D";
        PARAMS[UPLOAD_DATE_THIS_MONTH][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY] = "EgYIBBABGAI%3D";
    }

    public static String toParams(int options) {
        if (options == -1) {
            return null;
        }

        return PARAMS[getUploadDateIndex(options)][getDurationIndex(options)][getTypeIndex(options)][getFeaturesIndex(options)];
    }

    private static int getUploadDateIndex(int options) {
        int index = UPLOAD_DATE_ALL;

        if (Helpers.check(options, SearchOptions.UPLOAD_DATE_LAST_HOUR)) {
            index = UPLOAD_DATE_LAST_HOUR;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_TODAY)) {
            index = UPLOAD_DATE_TODAY;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_WEEK)) {
            index = UPLOAD_DATE_THIS_WEEK;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_MONTH)) {
            index = UPLOAD_DATE_THIS_MONTH;
        } else if (Helpers.check(options, SearchOptions.UPLOAD_DATE_THIS_YEAR)) {
            index = UPLOAD_DATE_THIS_YEAR;
        }

        return index;
    }

    private static int getDurationIndex(int options) {
        int index = DURATION_ANY;

        if (Helpers.check(options, SearchOptions.DURATION_UNDER_4)) {
            index = DURATION_UNDER_4;
        } else if (Helpers.check(options, SearchOptions.DURATION_BETWEEN_4_20)) {
            index = DURATION_BETWEEN_4_20;
        } else if (Helpers.check(options, SearchOptions.DURATION_OVER_20)) {
            index = DURATION_OVER_20;
        }

        return index;
    }

    private static int getTypeIndex(int options) {
        int index = TYPE_ANY;

        if (Helpers.check(options, SearchOptions.TYPE_VIDEO)) {
            index = TYPE_VIDEO;
        } else if (Helpers.check(options, SearchOptions.TYPE_CHANNEL)) {
            index = TYPE_CHANNEL;
        } else if (Helpers.check(options, SearchOptions.TYPE_PLAYLIST)) {
            index = TYPE_PLAYLIST;
        } else if (Helpers.check(options, SearchOptions.TYPE_MOVIE)) {
            index = TYPE_MOVIE;
        }

        return index;
    }

    private static int getFeaturesIndex(int options) {
        int index = FEATURE_ANY;

        if (Helpers.check(options, SearchOptions.FEATURE_LIVE)) {
            index = FEATURE_LIVE;
        } else if (Helpers.check(options, SearchOptions.FEATURE_4K)) {
            index = FEATURE_4K;
        } else if (Helpers.check(options, SearchOptions.FEATURE_HDR)) {
            index = FEATURE_HDR;
        }

        return index;
    }
}
