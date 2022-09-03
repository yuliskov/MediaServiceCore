package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

public class ActionsService {
    private static final String TAG = ActionsService.class.getSimpleName();
    private static ActionsService sInstance;
    private final ActionsApi mActionsManager;

    private ActionsService() {
        mActionsManager = RetrofitHelper.withJsonPath(ActionsApi.class);
    }

    public static ActionsService instance() {
        if (sInstance == null) {
            sInstance = new ActionsService();
        }

        return sInstance;
    }

    public void setLike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.setLike(ActionsApiParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeLike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.removeLike(ActionsApiParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void setDislike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.setDislike(ActionsApiParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeDislike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.removeDislike(ActionsApiParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    /**
     * params needed for mobile notifications
     */
    public void subscribe(String channelId, String params, String authorization) {
        if (channelId == null) {
            Log.e(TAG, "Can't subscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsManager.subscribe(ActionsApiParams.getSubscribeActionQuery(channelId, params), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void unsubscribe(String channelId, String authorization) {
        if (channelId == null) {
            Log.e(TAG, "Can't unsubscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsManager.unsubscribe(ActionsApiParams.getSubscribeActionQuery(channelId, null), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }
}
