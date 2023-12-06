package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInService getSignInService();
    RemoteControlService getRemoteControlService();
    HomeService getHomeService();
    MediaItemService getMediaItemService();
    LiveChatService getLiveChatService();
    CommentsService getCommentsService();
    NotificationsService getNotificationsService();
    void invalidateCache();
    void refreshCacheIfNeeded();
}
