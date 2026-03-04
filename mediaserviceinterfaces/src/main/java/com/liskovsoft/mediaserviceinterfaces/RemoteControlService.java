package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import io.reactivex.Observable;

public interface RemoteControlService {
    int STATE_PLAYING = 1;
    int STATE_PAUSED = 2;
    int STATE_IDLE = 3;

    String getPairingCode();

    // RxJava interfaces
    Observable<String> getPairingCodeObserve();
    Observable<Command> getCommandObserve();
    Observable<Void> postStartPlayingObserve(String videoId, long positionMs, long durationMs, int state);
    Observable<Void> postStateChangeObserve(long positionMs, long durationMs, int state);
    Observable<Void> postVolumeChangeObserve(int volume);
    Observable<Void> postSubtitleChangeObserve(String vssId, String languageCodee);
    Observable<Void> resetDataObserve();
}
