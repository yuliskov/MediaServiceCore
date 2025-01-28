package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.googleapi.youtubedata3.YouTubeDataServiceInt;
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata;
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl;

import java.util.List;

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
        List<ItemMetadata> channelMetadata = YouTubeDataServiceInt.getChannelMetadata(channelId);
        String title = channelMetadata != null && !channelMetadata.isEmpty() ? channelMetadata.get(0).getTitle() : null;
        String iconUrl = channelMetadata != null && !channelMetadata.isEmpty() ? channelMetadata.get(0).getCardImageUrl() : null;
        ChannelGroupServiceImpl.subscribe(true, channelId, title, iconUrl); // save locally
    }

    @Override
    public void unsubscribe(String channelId) {
        super.unsubscribe(channelId);
        ChannelGroupServiceImpl.subscribe(false, channelId, null, null); // save locally
    }
}
