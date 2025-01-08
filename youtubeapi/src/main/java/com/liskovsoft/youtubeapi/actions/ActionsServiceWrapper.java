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
        //if (YouTubeSignInService.instance().isSigned()) {
        //    super.subscribe(channelId, params);
        //} else {
        //    ChannelGroupServiceImpl.subscribe(null, null, channelId, true);
        //}

        super.subscribe(channelId, params);
        ChannelGroupServiceImpl.subscribe(null, null, channelId, true); // save locally
    }

    @Override
    public void unsubscribe(String channelId) {
        //if (YouTubeSignInService.instance().isSigned()) {
        //    super.unsubscribe(channelId);
        //} else {
        //    ChannelGroupServiceImpl.subscribe(null, null, channelId, false);
        //}

        super.unsubscribe(channelId);
        ChannelGroupServiceImpl.subscribe(null, null, channelId, false); // save locally
    }
}
