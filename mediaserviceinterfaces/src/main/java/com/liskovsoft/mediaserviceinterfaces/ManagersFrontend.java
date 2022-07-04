package com.liskovsoft.mediaserviceinterfaces;

public interface ManagersFrontend {
    SignInManager getSignInManager();
    RemoteManager getRemoteManager();
    MediaGroupManager getMediaGroupManager();
    MediaItemManager getMediaItemManager();
    LiveChatManager getLiveChatManager();
    void invalidateCache();
    void refreshCacheIfNeeded();
}
