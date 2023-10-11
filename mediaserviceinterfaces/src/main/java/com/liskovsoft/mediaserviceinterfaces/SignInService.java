package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.Account;
import io.reactivex.Observable;

import java.util.List;

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
    Observable<String> signInObserve();
    Observable<Void> signOutObserve();
    Observable<Boolean> isSignedObserve();
    Observable<List<Account>> getAccountsObserve();
}
