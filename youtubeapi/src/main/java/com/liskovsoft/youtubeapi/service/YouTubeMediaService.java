package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.LiveChatService;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupService;
import com.liskovsoft.mediaserviceinterfaces.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.RemoteService;
import com.liskovsoft.mediaserviceinterfaces.SignInService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxUtils;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class YouTubeMediaService implements MediaService {
    private static final String TAG = YouTubeMediaService.class.getSimpleName();
    private static YouTubeMediaService sInstance;
    private final YouTubeSignInService mSignInManager;
    private final YouTubeRemoteService mDeviceLinkManager;
    private final MediaGroupService mMediaGroupManager;
    private final MediaItemService mMediaItemManager;
    private final YouTubeLiveChatService mLiveChatService;
    private Disposable mRefreshCacheAction;

    private YouTubeMediaService() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInService.instance();
        mDeviceLinkManager = YouTubeRemoteService.instance();
        mMediaGroupManager = YouTubeMediaGroupService.instance();
        mMediaItemManager = YouTubeMediaItemService.instance();
        mLiveChatService = YouTubeLiveChatService.instance();
    }

    public static MediaService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaService();
        }

        return sInstance;
    }

    @Override
    public SignInService getSignInService() {
        return mSignInManager;
    }

    @Override
    public RemoteService getRemoteService() {
        return mDeviceLinkManager;
    }

    @Override
    public LiveChatService getLiveChatService() {
        return mLiveChatService;
    }

    @Override
    public MediaGroupService getMediaGroupService() {
        return mMediaGroupManager;
    }

    @Override
    public MediaItemService getMediaItemService() {
        return mMediaItemManager;
    }

    @Override
    public void invalidateCache() {
        AppService.instance().invalidateCache();
        YouTubeMediaItemService.instance().invalidateCache();
        YouTubeSignInService.instance().invalidateCache(); // sections infinite loading fix (request timed out fix)
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
