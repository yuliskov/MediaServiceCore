package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

public interface SignInManager {
    String getUserCode();
    Observable<String> getUserCodeObserve();
}
