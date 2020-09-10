package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.sharedutils.mylogger.Log;

public class YouTubeMediaService implements MediaService {
    private static final String TAG = YouTubeMediaService.class.getSimpleName();
    private static YouTubeMediaService sInstance;
    private final YouTubeSignInManager mSignInManager;
    private final MediaGroupManager mMediaTabManager;
    private final MediaItemManager mMediaItemManager;

    private YouTubeMediaService() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
        mMediaTabManager = YouTubeMediaGroupManagerV2.instance();
        mMediaItemManager = YouTubeMediaItemManager.instance();
    }

    public static MediaService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaService();
        }

        return sInstance;
    }

    @Override
    public SignInManager getSignInManager() {
        return mSignInManager;
    }

    @Override
    public MediaGroupManager getMediaGroupManager() {
        return mMediaTabManager;
    }

    @Override
    public MediaItemManager getMediaItemManager() {
        return mMediaItemManager;
    }
}
