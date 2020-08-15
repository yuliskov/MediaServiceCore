package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;

public class WatchNextServiceSigned {
    private static WatchNextServiceSigned sInstance;
    private final WatchNextManagerSigned mWatchNextManagerSigned;

    private WatchNextServiceSigned() {
        mWatchNextManagerSigned = RetrofitHelper.withJsonPath(WatchNextManagerSigned.class);
    }

    public static WatchNextServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new WatchNextServiceSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    // TODO: not implemented
}
