package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import io.reactivex.Observable;

public interface RemoteManager {
    String getPairingCode();

    // RxJava interfaces
    Observable<String> getPairingCodeObserve();
    Observable<Command> getCommandObserve();
    Observable<Void> postPlayingObserve(String videoId, long positionMs, long lengthMs);
}
