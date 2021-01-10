package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInManager getSignInManager();
    DeviceLinkManager getDeviceLinkManager();
    MediaGroupManager getMediaGroupManager();
    MediaItemManager getMediaItemManager();
    void invalidateCache();
}
