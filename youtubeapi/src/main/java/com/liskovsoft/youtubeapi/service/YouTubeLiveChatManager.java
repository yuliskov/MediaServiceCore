package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.LiveChatManager;
import com.liskovsoft.mediaserviceinterfaces.data.ChatItem;
import com.liskovsoft.youtubeapi.chat.LiveChatService;
import io.reactivex.Observable;

public class YouTubeLiveChatManager implements LiveChatManager {
    private static YouTubeLiveChatManager sInstance;
    private final LiveChatService mLiveChatService;

    public YouTubeLiveChatManager() {
        mLiveChatService = LiveChatService.instance();
    }

    public static YouTubeLiveChatManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeLiveChatManager();
        }

        return sInstance;
    }

    @Override
    public Observable<ChatItem> openLiveChatObserve(String chatKey) {
        return null;
    }
}
