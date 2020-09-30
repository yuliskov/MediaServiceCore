package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;
import com.liskovsoft.youtubeapi.service.internal.MediaItemManagerInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaItemManagerSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaItemManagerUnsigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import io.reactivex.Observable;

public class YouTubeMediaItemManager implements MediaItemManager {
    private static final String TAG = YouTubeMediaItemManager.class.getSimpleName();
    private static MediaItemManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaItemManagerInt mMediaItemManagerReal;

    private YouTubeMediaItemManager() {
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static MediaItemManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemManager();
        }

        return sInstance;
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(MediaItem item) {
        checkSigned();

        YouTubeMediaItem ytMediaItem = (YouTubeMediaItem) item;

        YouTubeMediaItemFormatInfo formatInfo = ytMediaItem.getFormatInfo();

        if (formatInfo == null) {
            VideoInfoResult videoInfo = mMediaItemManagerReal.getVideoInfo(item.getMediaId());

            formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo);

            ytMediaItem.setFormatInfo(formatInfo);
        }

        return formatInfo;
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(String videoId) {
        checkSigned();

        VideoInfoResult videoInfo = mMediaItemManagerReal.getVideoInfo(videoId);

        return YouTubeMediaItemFormatInfo.from(videoInfo);
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item) {
        return ObservableHelper.fromNullable(() -> getFormatInfo(item));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId) {
        return ObservableHelper.fromNullable(() -> getFormatInfo(videoId));
    }

    @Override
    public YouTubeMediaItemMetadata getMetadata(MediaItem item) {
        YouTubeMediaItem ytMediaItem = (YouTubeMediaItem) item;

        YouTubeMediaItemMetadata metadata = ytMediaItem.getMetadata();

        if (metadata == null) {
            metadata = getMetadata(item.getMediaId());

            ytMediaItem.setMetadata(metadata);
        }

        return metadata;
    }

    @Override
    public YouTubeMediaItemMetadata getMetadata(String videoId) {
        checkSigned();

        WatchNextResult watchNextResult = mMediaItemManagerReal.getWatchNextResult(videoId);

        return YouTubeMediaItemMetadata.from(watchNextResult);
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(MediaItem item) {
        return Observable.create(emitter -> {
            YouTubeMediaItemMetadata metadata = getMetadata(item);

            if (metadata != null) {
                emitter.onNext(metadata);
            }

            emitter.onComplete();
        });
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId) {
        return Observable.create(emitter -> {
            YouTubeMediaItemMetadata metadata = getMetadata(videoId);

            if (metadata != null) {
                emitter.onNext(metadata);
            }

            emitter.onComplete();
        });
    }

    @Override
    public void updateHistoryPosition(MediaItem item, float positionSec) {
        checkSigned();

        updateHistoryPosition(item.getMediaId(), positionSec);
    }

    @Override
    public void updateHistoryPosition(String videoId, float positionSec) {
        checkSigned();

        YouTubeMediaItemFormatInfo formatInfo = getFormatInfo(videoId);

        mMediaItemManagerReal.updateHistoryPosition(formatInfo.getVideoId(), formatInfo.getLengthSeconds(),
                formatInfo.getEventId(), formatInfo.getVisitorMonitoringData(), positionSec);
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec) {
        return ObservableHelper.fromVoidable(() -> updateHistoryPosition(item, positionSec));
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec) {
        return ObservableHelper.fromVoidable(() -> updateHistoryPosition(videoId, positionSec));
    }

    @Override
    public Observable<Void> subscribeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> subscribe(item));
    }

    @Override
    public Observable<Void> unsubscribeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> unsubscribe(item));
    }

    @Override
    public Observable<Void> setLikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> setLike(item));
    }

    @Override
    public Observable<Void> removeLikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> removeLike(item));
    }

    @Override
    public Observable<Void> setDislikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> setDislike(item));
    }

    @Override
    public Observable<Void> removeDislikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> removeDislike(item));
    }

    @Override
    public void setLike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.setLike(item.getMediaId());
    }

    @Override
    public void removeLike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.removeLike(item.getMediaId());
    }

    @Override
    public void setDislike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.setDislike(item.getMediaId());
    }

    @Override
    public void removeDislike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.removeDislike(item.getMediaId());
    }

    @Override
    public void subscribe(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.subscribe(item.getChannelId());
    }

    @Override
    public void unsubscribe(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.unsubscribe(item.getChannelId());
    }

    private void checkSigned() {
        if (mSignInManager.isSigned()) {
            Log.d(TAG, "User signed.");

            mMediaItemManagerReal = YouTubeMediaItemManagerSigned.instance();
            YouTubeMediaItemManagerUnsigned.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaItemManagerReal = YouTubeMediaItemManagerUnsigned.instance();
            YouTubeMediaItemManagerSigned.unhold();
        }
    }
}
