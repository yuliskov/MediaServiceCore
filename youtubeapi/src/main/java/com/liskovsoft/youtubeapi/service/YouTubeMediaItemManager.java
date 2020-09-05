package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;
import com.liskovsoft.youtubeapi.service.internal.MediaItemManagerInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaItemManagerSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaItemManagerUnsigned;
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
    public MediaItemFormatInfo getFormatInfo(MediaItem item) {
        checkSigned();

        return mMediaItemManagerReal.getFormatInfo(item);
    }

    @Override
    public MediaItemFormatInfo getFormatInfo(String videoId) {
        checkSigned();

        return mMediaItemManagerReal.getFormatInfo(videoId);
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item) {
        return Observable.fromCallable(() -> getFormatInfo(item));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId) {
        return Observable.fromCallable(() -> getFormatInfo(videoId));
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

        return mMediaItemManagerReal.getMetadata(videoId);
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(MediaItem item) {
        return Observable.fromCallable(() -> getMetadata(item));
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId) {
        return Observable.fromCallable(() -> getMetadata(videoId));
    }

    @Override
    public void updateHistoryPosition(MediaItem item, float positionSec) {
        checkSigned();

        mMediaItemManagerReal.updateHistoryPosition(item, positionSec);
    }

    @Override
    public void updateHistoryPosition(String videoId, float positionSec) {
        checkSigned();

        mMediaItemManagerReal.updateHistoryPosition(videoId, positionSec);
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec) {
        return Observable.create(emitter -> {
            updateHistoryPosition(item, positionSec);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec) {
        return Observable.create(emitter -> {
            updateHistoryPosition(videoId, positionSec);
            emitter.onComplete();
        });
    }

    @Override
    public void setLike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.setLike(item);
    }

    @Override
    public void removeLike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.removeLike(item);
    }

    @Override
    public void setDislike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.setDislike(item);
    }

    @Override
    public void removeDislike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.removeDislike(item);
    }

    @Override
    public void subscribe(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.subscribe(item);
    }

    @Override
    public void unsubscribe(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.unsubscribe(item);
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
