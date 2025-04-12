package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl;

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
}
