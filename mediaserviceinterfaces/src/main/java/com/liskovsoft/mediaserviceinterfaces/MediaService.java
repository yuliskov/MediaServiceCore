package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInManager getSignInManager();
    RemoteManager getRemoteManager();
    MediaGroupManager getMediaGroupManager();
    MediaItemManager getMediaItemManager();
    void invalidateCache();
    void enableAltDataSource(boolean enable);
    boolean isAltDataSourceEnabled();
}
