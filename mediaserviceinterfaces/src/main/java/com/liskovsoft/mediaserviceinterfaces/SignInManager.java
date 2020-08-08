package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

public interface SignInManager {
    /**
     * This code user should enter on service web page (e.g. https://www.youtube.com/activate)
     */
    String getUserCode();
    Observable<String> getUserCodeObserve();
}
