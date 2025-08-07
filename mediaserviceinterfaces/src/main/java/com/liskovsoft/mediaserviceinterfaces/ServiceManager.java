package com.liskovsoft.mediaserviceinterfaces;

public interface ServiceManager {
    SignInService getSignInService();
    RemoteControlService getRemoteControlService();
    ContentService getContentService();
    MediaItemService getMediaItemService();
    LiveChatService getLiveChatService();
    CommentsService getCommentsService();
    NotificationsService getNotificationsService();
    ChannelGroupService getChannelGroupService();
    void invalidateCache();
    void invalidateMediaItemCache();
    void refreshCacheIfNeeded();
    void applyNoPlaybackFix();
}
