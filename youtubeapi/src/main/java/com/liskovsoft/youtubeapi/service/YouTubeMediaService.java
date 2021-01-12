package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.CommandManager;
import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;

public class YouTubeMediaService implements MediaService {
    private static final String TAG = YouTubeMediaService.class.getSimpleName();
    private static YouTubeMediaService sInstance;
    private final YouTubeSignInManager mSignInManager;
    private final YouTubeCommandManager mDeviceLinkManager;
    private final MediaGroupManager mMediaGroupManager;
    private final MediaItemManager mMediaItemManager;

    private YouTubeMediaService() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
        mDeviceLinkManager = YouTubeCommandManager.instance();
        mMediaGroupManager = YouTubeMediaGroupManager.instance();
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
    public CommandManager getCommandManager() {
        return mDeviceLinkManager;
    }

    @Override
    public MediaGroupManager getMediaGroupManager() {
        return mMediaGroupManager;
    }

    @Override
    public MediaItemManager getMediaItemManager() {
        return mMediaItemManager;
    }

    @Override
    public void invalidateCache() {
        AppService.instance().invalidateCache();
        mSignInManager.invalidateCache();
    }
}
