package com.liskovsoft.mediaserviceinterfaces.oauth;

import java.util.List;

import io.reactivex.Observable;

public interface SignInService {
    void signOut();
    boolean isSigned();
    List<Account> getAccounts();
    Account getSelectedAccount();
    void selectAccount(Account account);
    void removeAccount(Account account);
    void setOnChange(Runnable onChange);

    // RxJava interfaces
    /**
     * Trying to login using 2-factor authentication
     * @return user code, user should apply this code on service web page (e.g. https://www.youtube.com/activate)
     */
    Observable<SignInCode> signInObserve();
    Observable<Void> signOutObserve();
    Observable<Boolean> isSignedObserve();
    Observable<List<Account>> getAccountsObserve();
}
