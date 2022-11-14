package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.RemoteService;
import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.rx.ObservableHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import com.liskovsoft.youtubeapi.service.data.YouTubeCommand;
import io.reactivex.Observable;

public class YouTubeRemoteService implements RemoteService {
    private static final String TAG = YouTubeRemoteService.class.getSimpleName();
    private static YouTubeRemoteService sInstance;
    private final LoungeService mLoungeService;

    private YouTubeRemoteService() {
        mLoungeService = LoungeService.instance();

        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeRemoteService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeRemoteService();
        }

        return sInstance;
    }

    @Override
    public String getPairingCode() {
        return mLoungeService.getPairingCode();
    }

    @Override
    public Observable<String> getPairingCodeObserve() {
        return ObservableHelper.fromNullable(this::getPairingCode);
    }

    @Override
    public Observable<Command> getCommandObserve() {
        return Observable.create(emitter -> {
            mLoungeService.startListening(
                    info -> emitter.onNext(YouTubeCommand.from(info))
            );

            emitter.onComplete();
        });
    }

    @Override
    public Observable<Void> postStartPlayingObserve(String videoId, long positionMs, long durationMs, boolean isPlaying) {
        return ObservableHelper.fromVoidable(() -> mLoungeService.postStartPlaying(videoId, positionMs, durationMs, isPlaying));
    }

    @Override
    public Observable<Void> postStateChangeObserve(long positionMs, long durationMs, boolean isPlaying) {
        return ObservableHelper.fromVoidable(() -> mLoungeService.postStateChange(positionMs, durationMs, isPlaying));
    }

    @Override
    public Observable<Void> postVolumeChangeObserve(int volume) {
        return ObservableHelper.fromVoidable(() -> mLoungeService.postVolumeChange(volume));
    }

    @Override
    public Observable<Void> resetDataObserve() {
        return ObservableHelper.fromVoidable(mLoungeService::resetData);
    }
}
