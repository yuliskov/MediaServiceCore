package com.liskovsoft.youtubeapi.service;

import androidx.annotation.NonNull;

import com.liskovsoft.mediaserviceinterfaces.RemoteControlService;
import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import com.liskovsoft.youtubeapi.service.data.YouTubeCommand;
import io.reactivex.Observable;

class YouTubeRemoteControlService implements RemoteControlService {
    private static final String TAG = YouTubeRemoteControlService.class.getSimpleName();
    private static YouTubeRemoteControlService sInstance;
    private LoungeService mLoungeService;

    private YouTubeRemoteControlService() {
    }

    public static YouTubeRemoteControlService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeRemoteControlService();
        }

        return sInstance;
    }

    @Override
    public String getPairingCode() {
        return getLoungeService().getPairingCode();
    }

    @Override
    public boolean registerPairingCode(String pairingCode) {
        return getLoungeService().registerPairingCode(pairingCode);
    }

    @Override
    public Observable<String> getPairingCodeObserve() {
        return RxHelper.fromCallable(this::getPairingCode);
    }

    @Override
    public Observable<Command> getCommandObserve() {
        return RxHelper.createLong(emitter -> {
            getLoungeService().startListening(
                    info -> emitter.onNext(YouTubeCommand.from(info))
            );

            emitter.onComplete();
        });
    }

    @Override
    public Observable<Void> postStartPlayingObserve(String videoId, long positionMs, long durationMs, int state) {
        return RxHelper.fromRunnable(() -> getLoungeService().postStartPlaying(videoId, positionMs, durationMs, state));
    }

    @Override
    public Observable<Void> postStateChangeObserve(long positionMs, long durationMs, int state) {
        return RxHelper.fromRunnable(() -> getLoungeService().postStateChange(positionMs, durationMs, state));
    }

    @Override
    public Observable<Void> postVolumeChangeObserve(int volume) {
        return RxHelper.fromRunnable(() -> getLoungeService().postVolumeChange(volume));
    }

    @Override
    public Observable<Void> postSubtitleChangeObserve(String vssId, String languageCodee) {
        return RxHelper.fromRunnable(() -> getLoungeService().postSubtitleChange(vssId, languageCodee));
    }

    @Override
    public Observable<Void> resetDataObserve() {
        return RxHelper.fromRunnable(getLoungeService()::resetData);
    }

    /**
     * Lazy init, called from a bg thread instead of at instance creation. <br/>
     * Fixes NetworkOnMainThreadException that happened inside proxy init (OkHttpCommons.java:395)
     */
    @NonNull
    private LoungeService getLoungeService() {
        if (mLoungeService == null) {
            mLoungeService = LoungeService.instance();
        }

        return mLoungeService;
    }
}
