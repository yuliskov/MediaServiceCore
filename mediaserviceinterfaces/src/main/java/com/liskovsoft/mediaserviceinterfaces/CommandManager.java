package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

public interface CommandManager {
    String getDeviceCode();

    // RxJava interfaces
    Observable<String> getDeviceCodeObserve();
}
