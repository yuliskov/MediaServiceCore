package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInManager getSignInManager();
    MediaGroupManager getMediaGroupManager();
    MediaItemManager getMediaItemManager();
}
