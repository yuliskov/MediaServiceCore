package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInManager getSignInManager();

    MediaTabManager getMediaTabManager();

    MediaInfoManager getMediaInfoManager();
}
