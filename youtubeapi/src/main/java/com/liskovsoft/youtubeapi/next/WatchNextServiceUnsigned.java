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
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId));
    }

    public WatchNextResult getWatchNextResult(String videoId, String playlistId) {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId));
    }

    private WatchNextResult getWatchNext(String query) {
        Call<WatchNextResult> wrapper = mWatchNextManagerUnsigned.getWatchNextResult(query);
        return RetrofitHelper.get(wrapper);
    }
}
