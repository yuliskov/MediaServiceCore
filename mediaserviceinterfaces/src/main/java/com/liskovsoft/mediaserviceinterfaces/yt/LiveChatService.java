package com.liskovsoft.mediaserviceinterfaces.yt;

import com.liskovsoft.mediaserviceinterfaces.yt.data.ChatItem;
import io.reactivex.Observable;

public interface LiveChatService {
    Observable<ChatItem> openLiveChatObserve(String chatKey);
}
