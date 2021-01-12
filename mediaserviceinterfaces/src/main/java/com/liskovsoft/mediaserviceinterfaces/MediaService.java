package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInManager getSignInManager();
    CommandManager getCommandManager();
    MediaGroupManager getMediaGroupManager();
    MediaItemManager getMediaItemManager();
    void invalidateCache();
}
