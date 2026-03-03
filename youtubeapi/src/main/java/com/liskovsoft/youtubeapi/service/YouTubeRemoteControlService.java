package com.liskovsoft.youtubeapi.service;

import androidx.annotation.Nullable;

import com.liskovsoft.mediaserviceinterfaces.RemoteControlService;
import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import com.liskovsoft.youtubeapi.service.data.YouTubeCommand;
import io.reactivex.Observable;

class YouTubeRemoteControlService implements RemoteControlService {
    private static final String TAG = YouTubeRemoteControlService.class.getSimpleName();
    private static YouTubeRemoteControlService sInstance;
    private final LoungeService mLoungeService;

    private YouTubeRemoteControlService() {
        mLoungeService = LoungeService.instance();

        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeRemoteControlService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeRemoteControlService();
        }

        return sInstance;
    }

    @Override
    public String getPairingCode() {
        return mLoungeService.getPairingCode();
    }

    @Override
    public Observable<String> getPairingCodeObserve() {
        return RxHelper.fromCallable(this::getPairingCode);
    }

    @Override
    public Observable<Command> getCommandObserve() {
        return RxHelper.createLong(emitter -> {
            mLoungeService.startListening(
                    info -> emitter.onNext(YouTubeCommand.from(info))
            );

            emitter.onComplete();
        });
    }

    @Override
    public Observable<Void> postStateChangeObserve(String videoId, String state, long positionMs, long durationMs) {
        return RxHelper.fromRunnable(() -> mLoungeService.postStateChange(videoId, state, positionMs, durationMs));
    }

    @Override
    public Observable<Void> postVolumeChangeObserve(int volume) {
        return RxHelper.fromRunnable(() -> mLoungeService.postVolumeChange(volume));
    }

    @Override
    public Observable<Void> postSubtitleChangeObserve(@Nullable String vssId, String languageCode) {
        return RxHelper.fromRunnable(() -> mLoungeService.postSubtitleChange(vssId, languageCode));
    }

    @Override
    public Observable<Void> resetDataObserve() {
        return RxHelper.fromRunnable(mLoungeService::resetData);
    }
}
