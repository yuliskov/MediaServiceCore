package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.data.SearchOptions;
import com.liskovsoft.sharedutils.helpers.Helpers;

final class SearchFilterHelper {
    private SearchFilterHelper() {
    }

    private static final int MAX_PARAMS = 7;

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
    private static final int FEATURE_LIVE_4K = 4;
    private static final int FEATURE_4K_HDR = 5;
    private static final int FEATURE_LIVE_4K_HDR = 6;

    private static final int SORT_BY_RELEVANCE = 0;
    private static final int SORT_BY_UPLOAD_DATE = 1;
    private static final int SORT_BY_VIEW_COUNT = 2;
    private static final int SORT_BY_RATING = 3;

    private static String[][][][][] sParams;

    private static void init() {
        if (sParams != null) {
            return;
        }

        sParams = new String[MAX_PARAMS][MAX_PARAMS][MAX_PARAMS][MAX_PARAMS][MAX_PARAMS];

        // Single criteria

        sParams[UPLOAD_DATE_LAST_HOUR][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQIARAB";
        sParams[UPLOAD_DATE_TODAY][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQIAhAB";
        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQIAxAB";
        sParams[UPLOAD_DATE_THIS_MONTH][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQIBBAB";
        sParams[UPLOAD_DATE_THIS_YEAR][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQIBRAB";

        sParams[UPLOAD_DATE_ALL][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQQARgB";
        sParams[UPLOAD_DATE_ALL][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQQARgD";
        sParams[UPLOAD_DATE_ALL][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgQQARgC";

        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_VIDEO][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgIQAQ%3D%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_CHANNEL][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgIQAg%3D%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_PLAYLIST][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgIQAw%3D%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_MOVIE][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgIQBA%3D%3D";

        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_LIVE][SORT_BY_RELEVANCE] = "EgJAAQ%3D%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_4K][SORT_BY_RELEVANCE] = "EgJwAQ%3D%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_HDR][SORT_BY_RELEVANCE] = "EgPIAQE%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_LIVE_4K][SORT_BY_RELEVANCE] = "EgRAAXAB";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_4K_HDR][SORT_BY_RELEVANCE] = "EgVwAcgBAQ%3D%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_LIVE_4K_HDR][SORT_BY_RELEVANCE] = "EgdAAXAByAEB";

        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_UPLOAD_DATE] = "CAI%3D";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_VIEW_COUNT] = "CAMSAhAB";
        sParams[UPLOAD_DATE_ALL][DURATION_ANY][TYPE_ANY][FEATURE_ANY][SORT_BY_RATING] = "CAESAhAB";


        // Various combinations

        sParams[UPLOAD_DATE_TODAY][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIAhABGAE%3D";
        sParams[UPLOAD_DATE_TODAY][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIAhABGAM%3D";
        sParams[UPLOAD_DATE_TODAY][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIAhABGAI%3D";

        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIAxABGAE%3D";
        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIAxABGAM%3D";
        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIAxABGAI%3D";

        sParams[UPLOAD_DATE_THIS_MONTH][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIBBABGAE%3D";
        sParams[UPLOAD_DATE_THIS_MONTH][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIBBABGAM%3D";
        sParams[UPLOAD_DATE_THIS_MONTH][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RELEVANCE] = "EgYIBBABGAI%3D";

        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_UNDER_4][TYPE_ANY][FEATURE_ANY][SORT_BY_UPLOAD_DATE] = "CAISBBABGAI%3D";
        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_BETWEEN_4_20][TYPE_ANY][FEATURE_ANY][SORT_BY_VIEW_COUNT] = "CAMSBBABGAI%3D";
        sParams[UPLOAD_DATE_THIS_WEEK][DURATION_OVER_20][TYPE_ANY][FEATURE_ANY][SORT_BY_RATING] = "CAESBBABGAI%3D";
    }

    public static String toParams(int options) {
        if (options == -1 || options == 0) {
            return null;
        }

        init();

        return sParams[getUploadDateIndex(options)][getDurationIndex(options)][getTypeIndex(options)][getFeaturesIndex(options)][getSortByIndex(options)];
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

        if (Helpers.check(options, SearchOptions.FEATURE_LIVE | SearchOptions.FEATURE_4K | SearchOptions.FEATURE_HDR)) {
            index = FEATURE_LIVE_4K_HDR;
        } else if (Helpers.check(options, SearchOptions.FEATURE_LIVE | SearchOptions.FEATURE_4K)) {
            index = FEATURE_LIVE_4K;
        } else if (Helpers.check(options, SearchOptions.FEATURE_4K | SearchOptions.FEATURE_HDR)) {
            index = FEATURE_4K_HDR;
        } else if (Helpers.check(options, SearchOptions.FEATURE_LIVE)) {
            index = FEATURE_LIVE;
        } else if (Helpers.check(options, SearchOptions.FEATURE_4K)) {
            index = FEATURE_4K;
        } else if (Helpers.check(options, SearchOptions.FEATURE_HDR)) {
            index = FEATURE_HDR;
        }

        return index;
    }

    private static int getSortByIndex(int options) {
        int index = SORT_BY_RELEVANCE;

        if (Helpers.check(options, SearchOptions.SORT_BY_UPLOAD_DATE)) {
            index = SORT_BY_UPLOAD_DATE;
        } else if (Helpers.check(options, SearchOptions.SORT_BY_VIEW_COUNT)) {
            index = SORT_BY_VIEW_COUNT;
        } else if (Helpers.check(options, SearchOptions.SORT_BY_RATING)) {
            index = SORT_BY_RATING;
        }

        return index;
    }
}
