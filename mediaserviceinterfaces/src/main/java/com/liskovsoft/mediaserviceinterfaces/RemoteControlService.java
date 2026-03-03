package com.liskovsoft.mediaserviceinterfaces;

import androidx.annotation.Nullable;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import io.reactivex.Observable;

public interface RemoteControlService {
    String STATE_PLAYING = "1";
    String STATE_PAUSED = "2";
    String STATE_IDLE = "3";

    String getPairingCode();

    // RxJava interfaces
    Observable<String> getPairingCodeObserve();
    Observable<Command> getCommandObserve();
    Observable<Void> postStateChangeObserve(String videoId, String state, long positionMs, long durationMs);
    Observable<Void> postVolumeChangeObserve(int volume);
    Observable<Void> postSubtitleChangeObserve(@Nullable String vssId, String languageCodee);
    Observable<Void> resetDataObserve();
}
