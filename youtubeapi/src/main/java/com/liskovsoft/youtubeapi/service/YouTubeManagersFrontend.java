package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.LiveChatManager;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.ManagersFrontend;
import com.liskovsoft.mediaserviceinterfaces.RemoteManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxUtils;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class YouTubeManagersFrontend implements ManagersFrontend {
    private static final String TAG = YouTubeManagersFrontend.class.getSimpleName();
    private static YouTubeManagersFrontend sInstance;
    private final YouTubeSignInManager mSignInManager;
    private final YouTubeRemoteManager mDeviceLinkManager;
    private final MediaGroupManager mMediaGroupManager;
    private final MediaItemManager mMediaItemManager;
    private final YouTubeLiveChatManager mLiveChatManager;
    private Disposable mRefreshCacheAction;

    private YouTubeManagersFrontend() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
        mDeviceLinkManager = YouTubeRemoteManager.instance();
        mMediaGroupManager = YouTubeMediaGroupManager.instance();
        mMediaItemManager = YouTubeMediaItemManager.instance();
        mLiveChatManager = YouTubeLiveChatManager.instance();
    }

    public static ManagersFrontend instance() {
        if (sInstance == null) {
            sInstance = new YouTubeManagersFrontend();
        }

        return sInstance;
    }

    @Override
    public SignInManager getSignInManager() {
        return mSignInManager;
    }

    @Override
    public RemoteManager getRemoteManager() {
        return mDeviceLinkManager;
    }

    @Override
    public LiveChatManager getLiveChatManager() {
        return mLiveChatManager;
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
        YouTubeMediaItemManager.instance().invalidateCache();
        YouTubeSignInManager.instance().invalidateCache(); // sections infinite loading fix (request timed out fix)
        LocaleManager.unhold();
    }

    @Override
    public void refreshCacheIfNeeded() {
        if (RxUtils.isAnyActionRunning(mRefreshCacheAction)) {
            return;
        }

        mRefreshCacheAction = RxUtils.execute(refreshCacheIfNeededObserve());
    }

    private Observable<Void> refreshCacheIfNeededObserve() {
        return ObservableHelper.fromVoidable(AppService.instance()::refreshCacheIfNeeded);
    }

    public static String serialize(MediaItem mediaItem) {
        if (mediaItem == null) {
            return null;
        }

        return mediaItem.toString();
    }

    public static MediaItem deserializeMediaItem(String itemSpec) {
        if (itemSpec == null) {
            return null;
        }

        return YouTubeMediaItem.fromString(itemSpec);
    }
}
