package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

public class ActionsService {
    private static final String TAG = ActionsService.class.getSimpleName();
    private static ActionsService sInstance;
    private final ActionsManager mActionsManager;

    private ActionsService() {
        mActionsManager = RetrofitHelper.withJsonPath(ActionsManager.class);
    }

    public static ActionsService instance() {
        if (sInstance == null) {
            sInstance = new ActionsService();
        }

        return sInstance;
    }

    public void setLike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.setLike(ActionsManagerParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeLike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.removeLike(ActionsManagerParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void setDislike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.setDislike(ActionsManagerParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeDislike(String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mActionsManager.removeDislike(ActionsManagerParams.getLikeActionQuery(videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void subscribe(String channelId, String authorization) {
        if (channelId == null) {
            Log.e(TAG, "Can't subscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsManager.subscribe(ActionsManagerParams.getSubscribeActionQuery(channelId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void unsubscribe(String channelId, String authorization) {
        if (channelId == null) {
            Log.e(TAG, "Can't unsubscribe: ChannelId is null");
            return;
        }

        Call<ActionResult> wrapper =
                mActionsManager.unsubscribe(ActionsManagerParams.getSubscribeActionQuery(channelId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }
}
