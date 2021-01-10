package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

public interface DeviceLinkManager {
    String getDeviceCode();

    // RxJava interfaces
    Observable<String> getDeviceCodeObserve();
}
