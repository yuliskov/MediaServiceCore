package com.liskovsoft.mediaserviceinterfaces;

public interface HubService {
    SignInService getSignInService();
    RemoteControlService getRemoteControlService();
    ContentService getContentService();
    MediaItemService getMediaItemService();
    LiveChatService getLiveChatService();
    CommentsService getCommentsService();
    NotificationsService getNotificationsService();
    void invalidateCache();
    void refreshCacheIfNeeded();
}
