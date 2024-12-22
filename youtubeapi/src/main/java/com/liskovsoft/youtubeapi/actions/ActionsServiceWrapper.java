package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;

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
        if (YouTubeSignInService.instance().isSigned()) {
            super.subscribe(channelId, params);
        } else {
            ChannelGroupServiceImpl.INSTANCE.subscribe(null, null, channelId, true);
        }
    }

    @Override
    public void unsubscribe(String channelId) {
        if (YouTubeSignInService.instance().isSigned()) {
            super.unsubscribe(channelId);
        } else {
            ChannelGroupServiceImpl.INSTANCE.subscribe(null, null, channelId, false);
        }
    }
}
