package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInService getSignInService();
    RemoteControlService getRemoteControlService();
    MediaGroupService getMediaGroupService();
    MediaItemService getMediaItemService();
    LiveChatService getLiveChatService();
    CommentsService getCommentsService();
    void invalidateCache();
    void refreshCacheIfNeeded();
}
