package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.sharedutils.mylogger.Log;

import java.io.InputStream;
import java.util.List;

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
    public InputStream getMpdStream(MediaItemFormatInfo formatInfo) {
        checkSigned();

        return mMediaItemManagerReal.getMpdStream(formatInfo);
    }

    @Override
    public List<String> getUrlList(MediaItemFormatInfo formatInfo) {
        checkSigned();

        return mMediaItemManagerReal.getUrlList(formatInfo);
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
