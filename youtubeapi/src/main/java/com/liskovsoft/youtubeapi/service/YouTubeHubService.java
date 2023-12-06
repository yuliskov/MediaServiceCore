package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.CommentsService;
import com.liskovsoft.mediaserviceinterfaces.LiveChatService;
import com.liskovsoft.mediaserviceinterfaces.ContentService;
import com.liskovsoft.mediaserviceinterfaces.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.HubService;
import com.liskovsoft.mediaserviceinterfaces.NotificationsService;
import com.liskovsoft.mediaserviceinterfaces.RemoteControlService;
import com.liskovsoft.mediaserviceinterfaces.SignInService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class YouTubeHubService implements HubService {
    private static final String TAG = YouTubeHubService.class.getSimpleName();
    private static YouTubeHubService sInstance;
    private final YouTubeSignInService mSignInManager;
    private final YouTubeRemoteControlService mDeviceLinkManager;
    private final ContentService mMediaGroupManager;
    private final MediaItemService mMediaItemManager;
    private final YouTubeLiveChatService mLiveChatService;
    private final YouTubeCommentsService mCommentsService;
    private Disposable mRefreshCacheAction;

    private YouTubeHubService() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInService.instance();
        mDeviceLinkManager = YouTubeRemoteControlService.instance();
        mMediaGroupManager = YouTubeContentService.instance();
        mMediaItemManager = YouTubeMediaItemService.instance();
        mLiveChatService = YouTubeLiveChatService.instance();
        mCommentsService = YouTubeCommentsService.instance();
    }

    public static HubService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeHubService();
        }

        return sInstance;
    }

    @Override
    public SignInService getSignInService() {
        return mSignInManager;
    }

    @Override
    public RemoteControlService getRemoteControlService() {
        return mDeviceLinkManager;
    }

    @Override
    public LiveChatService getLiveChatService() {
        return mLiveChatService;
    }

    @Override
    public CommentsService getCommentsService() {
        return mCommentsService;
    }

    @Override
    public ContentService getContentService() {
        return mMediaGroupManager;
    }

    @Override
    public MediaItemService getMediaItemService() {
        return mMediaItemManager;
    }

    @Override
    public NotificationsService getNotificationsService() {
        return YouTubeNotificationsService.INSTANCE;
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
        if (RxHelper.isAnyActionRunning(mRefreshCacheAction)) {
            return;
        }

        mRefreshCacheAction = RxHelper.execute(refreshCacheIfNeededObserve());
    }

    private Observable<Void> refreshCacheIfNeededObserve() {
        return RxHelper.fromVoidable(AppService.instance()::refreshCacheIfNeeded);
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
