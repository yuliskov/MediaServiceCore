package com.liskovsoft.youtubeapi.actions;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.browse.v1.BrowseService;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;

import retrofit2.Call;

public class ActionsService {
    private static final String TAG = ActionsService.class.getSimpleName();
    private final ActionsApi mActionsApi;

    public ActionsService() {
        mActionsApi = RetrofitHelper.create(ActionsApi.class);
    }

    public void setLike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsApi.setLike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeLike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsApi.removeLike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void setDislike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsApi.setDislike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeDislike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsApi.removeDislike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    /**
     * params needed for mobile notifications
     */
    public void subscribe(String channelId, String params) {
        if (channelId == null) {
            Log.e(TAG, "Can't subscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsApi.subscribe(ActionsApiHelper.getSubscribeActionQuery(channelId, params));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void unsubscribe(String channelId) {
        if (channelId == null) {
            Log.e(TAG, "Can't unsubscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsApi.unsubscribe(ActionsApiHelper.getSubscribeActionQuery(channelId, null));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void pauseWatchHistory() {
        Call<Void> wrapper = mActionsApi.pauseWatchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    public void resumeWatchHistory() {
        Call<Void> wrapper = mActionsApi.resumeWatchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    public void clearWatchHistory() {
        Call<Void> wrapper = mActionsApi.clearWatchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    public void clearSearchHistory() {
        // Empty start suggestions fix: use anonymous search
        //boolean skipAuth = getBrowseService().getSuggestToken() == null;

        Call<Void> wrapper = mActionsApi.clearSearchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    @NonNull
    private static BrowseService getBrowseService() {
        return BrowseService.instance();
    }
}
