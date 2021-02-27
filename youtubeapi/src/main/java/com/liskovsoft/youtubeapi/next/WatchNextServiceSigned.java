package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.browse.BrowseManagerParams;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.next.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.result.WatchNextResultContinuation;
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
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId), authorization);
    }

    public WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex, String authorization) {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex), authorization);
    }

    public WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex, String playlistParams, String authorization) {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams), authorization);
    }

    private WatchNextResult getWatchNext(String query, String authorization) {
        Call<WatchNextResult> wrapper = mWatchNextManagerSigned.getWatchNextResult(query, authorization);
        return RetrofitHelper.get(wrapper);
    }

    public WatchNextResultContinuation continueWatchNext(String nextKey, String authorization) {
        Call<WatchNextResultContinuation> wrapper = mWatchNextManagerSigned.continueWatchNextResult(BrowseManagerParams.getContinuationQuery(nextKey), authorization);
        return RetrofitHelper.get(wrapper);
    }
}
