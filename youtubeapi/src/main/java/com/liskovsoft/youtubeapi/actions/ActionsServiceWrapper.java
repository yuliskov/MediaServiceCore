package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl;
import com.liskovsoft.youtubeapi.notifications.NotificationStorage;

public class ActionsServiceWrapper extends ActionsService {
    private static ActionsServiceWrapper sInstance;

    public static ActionsServiceWrapper instance() {
        if (sInstance == null) {
            sInstance = new ActionsServiceWrapper();
        }

        return sInstance;
    }

    @Override
    public void subscribe(String channelId, String params) {
        super.subscribe(channelId, params);

        ChannelGroupServiceImpl.subscribe(true, channelId, null, null); // save locally
    }

    @Override
    public void unsubscribe(String channelId) {
        super.unsubscribe(channelId);

        ChannelGroupServiceImpl.subscribe(false, channelId, null, null); // save locally
    }

    @Override
    public void setLike(String videoId) {
        super.setLike(videoId);

        NotificationStorage.setLike(true);
    }

    @Override
    public void removeLike(String videoId) {
        super.removeLike(videoId);

        NotificationStorage.setLike(false);
    }

    @Override
    public void setDislike(String videoId) {
        super.setDislike(videoId);

        NotificationStorage.setLike(false);
    }

    @Override
    public void removeDislike(String videoId) {
        super.removeDislike(videoId);

        NotificationStorage.setLike(true);
    }
}
