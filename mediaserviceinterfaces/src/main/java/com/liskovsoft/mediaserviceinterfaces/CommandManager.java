package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import io.reactivex.Observable;

public interface CommandManager {
    String getDeviceCode();

    // RxJava interfaces
    Observable<String> getDeviceCodeObserve();
    Observable<Command> getDeviceCommandObserve();
    Observable<Void> postPlayingObserve(String videoId, long positionMs, long lengthMs);
}
