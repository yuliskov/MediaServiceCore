package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import retrofit2.Call;

public class WatchNextServiceUnsigned {
    private static WatchNextServiceUnsigned sInstance;
    private final WatchNextManagerUnsigned mWatchNextManagerUnsigned;

    private WatchNextServiceUnsigned() {
        mWatchNextManagerUnsigned = RetrofitHelper.withJsonPath(WatchNextManagerUnsigned.class);
    }

    public static WatchNextServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new WatchNextServiceUnsigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public WatchNextResult getWatchNextResult(String videoId) {
        Call<WatchNextResult> wrapper = mWatchNextManagerUnsigned.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(videoId));
        return RetrofitHelper.get(wrapper);
    }
}
