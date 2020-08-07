package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaInfoManager;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.sharedutils.mylogger.Log;

public class YouTubeMediaService implements MediaService {
    private static final String TAG = YouTubeMediaService.class.getSimpleName();
    private static YouTubeMediaService sInstance;
    private final YouTubeSignInManager mSignInManager;
    private final MediaTabManager mMediaTabManager;

    private YouTubeMediaService() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
        mMediaTabManager = YouTubeMediaTabManager.instance();
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
    public MediaTabManager getMediaTabManager() {
        return mMediaTabManager;
    }

    @Override
    public MediaInfoManager getMediaInfoManager() {
        return null;
    }
}
