package com.liskovsoft.youtubeapi.next.v1;

import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import retrofit2.Call;

public class WatchNextServiceOld {
    private static WatchNextServiceOld sInstance;
    private final WatchNextApi mWatchNextManagerSigned;

    private WatchNextServiceOld() {
        mWatchNextManagerSigned = RetrofitHelper.create(WatchNextApi.class);
    }

    public static WatchNextServiceOld instance() {
        if (sInstance == null) {
            sInstance = new WatchNextServiceOld();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public WatchNextResult getWatchNextResult(String videoId) {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId));
    }

    public WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex) {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId, playlistId, playlistIndex));
    }

    public WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams));
    }

    private WatchNextResult getWatchNext(String query) {
        Call<WatchNextResult> wrapper = mWatchNextManagerSigned.getWatchNextResult(query);
        return RetrofitHelper.get(wrapper);
    }

    public WatchNextResultContinuation continueWatchNext(String nextKey) {
        Call<WatchNextResultContinuation> wrapper = mWatchNextManagerSigned.continueWatchNextResult(BrowseApiHelper.getContinuationQuery(nextKey));
        return RetrofitHelper.get(wrapper);
    }
}
