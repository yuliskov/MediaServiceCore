package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;

public class YouTubeMediaItemManager implements MediaItemManager {
    private static final String TAG = YouTubeMediaItemManager.class.getSimpleName();
    private static MediaItemManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaItemManager mMediaItemManagerReal;

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
    public MediaItemMetadata getMetadata(MediaItem item) {
        checkSigned();

        return mMediaItemManagerReal.getMetadata(item);
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId) {
        checkSigned();

        return mMediaItemManagerReal.getMetadata(videoId);
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item) {
        checkSigned();

        return mMediaItemManagerReal.getFormatInfoObserve(item);
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId) {
        checkSigned();

        return mMediaItemManagerReal.getFormatInfoObserve(videoId);
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(MediaItem item) {
        checkSigned();

        return mMediaItemManagerReal.getMetadataObserve(item);
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId) {
        checkSigned();

        return mMediaItemManagerReal.getMetadataObserve(videoId);
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
