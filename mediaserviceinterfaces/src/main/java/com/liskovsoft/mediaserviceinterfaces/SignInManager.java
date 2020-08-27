package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

public interface SignInManager {
    /**
     * Trying to login using 2-factor authentication
     * @return user code, user should apply this code on service web page (e.g. https://www.youtube.com/activate)
     */
    String logIn();
    void logOut();

    // RxJava interfaces
    Observable<String> logInObserve();
    Observable<Void> logOutObserve();
}
