package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import retrofit2.Call;

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

    public WatchNextResult getWatchNextResult(String videoId, String authorization) {
        Call<WatchNextResult> wrapper = mWatchNextManagerSigned.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(videoId), authorization);
        return RetrofitHelper.get(wrapper);
    }
}
