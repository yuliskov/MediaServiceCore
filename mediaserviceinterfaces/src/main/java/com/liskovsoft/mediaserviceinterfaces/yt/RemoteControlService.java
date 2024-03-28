package com.liskovsoft.mediaserviceinterfaces.yt;

import com.liskovsoft.mediaserviceinterfaces.yt.data.Command;
import io.reactivex.Observable;

public interface RemoteControlService {
    String getPairingCode();

    // RxJava interfaces
    Observable<String> getPairingCodeObserve();
    Observable<Command> getCommandObserve();
    Observable<Void> postStartPlayingObserve(String videoId, long positionMs, long durationMs, boolean isPlaying);
    Observable<Void> postStateChangeObserve(long positionMs, long durationMs, boolean isPlaying);
    Observable<Void> postVolumeChangeObserve(int volume);
    Observable<Void> resetDataObserve();
}
