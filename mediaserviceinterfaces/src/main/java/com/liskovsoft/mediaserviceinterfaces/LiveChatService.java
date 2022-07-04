package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.ChatItem;
import io.reactivex.Observable;

public interface LiveChatService {
    Observable<ChatItem> openLiveChatObserve(String chatKey);
}
