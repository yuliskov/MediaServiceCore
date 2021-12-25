package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInManager getSignInManager();
    RemoteManager getRemoteManager();
    MediaGroupManager getMediaGroupManager();
    MediaItemManager getMediaItemManager();
    void invalidateCache();
    void refreshCacheIfNeeded();
}
