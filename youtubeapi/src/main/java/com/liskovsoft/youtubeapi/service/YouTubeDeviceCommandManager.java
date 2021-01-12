package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.CommandManager;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import io.reactivex.Observable;

public class YouTubeDeviceCommandManager implements CommandManager {
    private static final String TAG = YouTubeDeviceCommandManager.class.getSimpleName();
    private static final long TOKEN_REFRESH_PERIOD_MS = 30 * 60 * 1_000; // 30 minutes
    private static YouTubeDeviceCommandManager sInstance;
    private final LoungeService mLoungeService;

    private YouTubeDeviceCommandManager() {
        mLoungeService = LoungeService.instance();

        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeDeviceCommandManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeDeviceCommandManager();
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
