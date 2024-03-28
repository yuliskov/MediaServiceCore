package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.yt.CommentsService;
import com.liskovsoft.mediaserviceinterfaces.yt.LiveChatService;
import com.liskovsoft.mediaserviceinterfaces.yt.ContentService;
import com.liskovsoft.mediaserviceinterfaces.yt.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.yt.MotherService;
import com.liskovsoft.mediaserviceinterfaces.yt.NotificationsService;
import com.liskovsoft.mediaserviceinterfaces.yt.RemoteControlService;
import com.liskovsoft.mediaserviceinterfaces.yt.SignInService;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class YouTubeMotherService implements MotherService {
    private static final String TAG = YouTubeMotherService.class.getSimpleName();
    private static YouTubeMotherService sInstance;
    private final YouTubeSignInService mSignInManager;
    private final YouTubeRemoteControlService mDeviceLinkManager;
    private final ContentService mMediaGroupManager;
    private final MediaItemService mMediaItemManager;
    private final YouTubeLiveChatService mLiveChatService;
    private final YouTubeCommentsService mCommentsService;
    private Disposable mRefreshCacheAction;

    private YouTubeMotherService() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInService.instance();
        mDeviceLinkManager = YouTubeRemoteControlService.instance();
        mMediaGroupManager = YouTubeContentService.instance();
        mMediaItemManager = YouTubeMediaItemService.instance();
        mLiveChatService = YouTubeLiveChatService.instance();
        mCommentsService = YouTubeCommentsService.instance();
    }

    public static MotherService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMotherService();
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
