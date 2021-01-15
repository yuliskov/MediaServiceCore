package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.RemoteManager;
import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import com.liskovsoft.youtubeapi.service.data.YouTubeCommand;
import io.reactivex.Observable;

import java.io.InterruptedIOException;

public class YouTubeRemoteManager implements RemoteManager {
    private static final String TAG = YouTubeRemoteManager.class.getSimpleName();
    private static YouTubeRemoteManager sInstance;
    private final LoungeService mLoungeService;

    private YouTubeRemoteManager() {
        mLoungeService = LoungeService.instance();

        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeRemoteManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeRemoteManager();
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
            try {
                mLoungeService.startListening(
                        info -> emitter.onNext(YouTubeCommand.from(info))
                );

                emitter.onComplete();
            } catch (InterruptedIOException e) {
                // https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
                // UndeliverableException fix
                emitter.tryOnError(e);
            }
        });
    }

    @Override
    public Observable<Void> postPlayingObserve(String videoId, long positionMs, long lengthMs) {
        return ObservableHelper.fromVoidable(() -> mLoungeService.postPlaying(videoId, positionMs, lengthMs));
    }
}
