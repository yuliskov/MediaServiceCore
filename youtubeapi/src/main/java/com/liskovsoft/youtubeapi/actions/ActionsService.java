package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.browse.v1.BrowseService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper;
import retrofit2.Call;

public class ActionsService {
    private static final String TAG = ActionsService.class.getSimpleName();
    private static ActionsService sInstance;
    private final ActionsApi mActionsManager;
    private final BrowseService mBrowseService;

    private ActionsService() {
        mActionsManager = RetrofitHelper.create(ActionsApi.class);
        mBrowseService = BrowseService.instance();
    }

    public static ActionsService instance() {
        if (sInstance == null) {
            sInstance = new ActionsService();
        }

        return sInstance;
    }

    public void setLike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsManager.setLike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeLike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsManager.removeLike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void setDislike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsManager.setDislike(ActionsApiHelper.getLikeActionQuery(videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeDislike(String videoId) {
        Call<ActionResult> wrapper =
                mActionsManager.removeDislike(ActionsApiHelper.getLikeActionQuery(videoId));

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
                mActionsManager.subscribe(ActionsApiHelper.getSubscribeActionQuery(channelId, params));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void unsubscribe(String channelId) {
        if (channelId == null) {
            Log.e(TAG, "Can't unsubscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsManager.unsubscribe(ActionsApiHelper.getSubscribeActionQuery(channelId, null));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void pauseWatchHistory() {
        Call<Void> wrapper = mActionsManager.pauseWatchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    public void resumeWatchHistory() {
        Call<Void> wrapper = mActionsManager.resumeWatchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    public void clearWatchHistory() {
        Call<Void> wrapper = mActionsManager.clearWatchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper);
    }

    public void clearSearchHistory() {
        // Empty start suggestions fix: use anonymous search
        boolean skipAuth = mBrowseService.getSuggestToken() == null;

        Call<Void> wrapper = mActionsManager.clearSearchHistory(ActionsApiHelper.getEmptyQuery());
        RetrofitHelper.get(wrapper, skipAuth);
    }
}
