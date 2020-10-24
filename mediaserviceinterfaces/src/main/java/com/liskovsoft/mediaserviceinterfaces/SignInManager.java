package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.Account;
import io.reactivex.Observable;

import java.util.List;

public interface SignInManager {
    /**
     * Trying to login using 2-factor authentication
     * @return user code, user should apply this code on service web page (e.g. https://www.youtube.com/activate)
     */
    String signIn();
    void signOut();
    boolean isSigned();
    List<Account> getAccounts();

    // RxJava interfaces
    Observable<String> signInObserve();
    Observable<Void> signOutObserve();
    Observable<Boolean> isSignedObserve();
    Observable<List<Account>> getAccountsObserve();
}
