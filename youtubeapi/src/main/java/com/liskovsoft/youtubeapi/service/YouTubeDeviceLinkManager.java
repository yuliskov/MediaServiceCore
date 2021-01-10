package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.DeviceLinkManager;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import io.reactivex.Observable;

public class YouTubeDeviceLinkManager implements DeviceLinkManager {
    private static final String TAG = YouTubeDeviceLinkManager.class.getSimpleName();
    private static final long TOKEN_REFRESH_PERIOD_MS = 30 * 60 * 1_000; // 30 minutes
    private static YouTubeDeviceLinkManager sInstance;
    private final LoungeService mLoungeService;

    private YouTubeDeviceLinkManager() {
        mLoungeService = LoungeService.instance();

        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeDeviceLinkManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeDeviceLinkManager();
        }

        return sInstance;
    }

    @Override
    public String getDeviceCode() {
        return mLoungeService.getPairingCode();
    }

    @Override
    public Observable<String> getDeviceCodeObserve() {
        return ObservableHelper.fromNullable(this::getDeviceCode);
    }
}
