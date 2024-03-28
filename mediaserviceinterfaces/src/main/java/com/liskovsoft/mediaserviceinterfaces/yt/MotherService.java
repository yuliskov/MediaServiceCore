package com.liskovsoft.mediaserviceinterfaces.yt;

public interface MotherService {
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
