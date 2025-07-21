package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.LiveChatService;
import com.liskovsoft.mediaserviceinterfaces.data.ChatItem;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.chat.LiveChatServiceInt;
import io.reactivex.Observable;

class YouTubeLiveChatService implements LiveChatService {
    private static YouTubeLiveChatService sInstance;
    private final LiveChatServiceInt mLiveChatServiceInt;

    private YouTubeLiveChatService() {
        mLiveChatServiceInt = LiveChatServiceInt.INSTANCE;
    }

    public static YouTubeLiveChatService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeLiveChatService();
        }

        return sInstance;
    }

    @Override
    public Observable<ChatItem> openLiveChatObserve(String chatKey) {
        return RxHelper.createLong(emitter -> {
            mLiveChatServiceInt.openLiveChat(
                    chatKey, emitter::onNext
            );

            emitter.onComplete();
        });
    }
}
