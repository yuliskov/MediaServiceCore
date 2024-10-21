package com.liskovsoft.mediaserviceinterfaces.yt;

import com.liskovsoft.mediaserviceinterfaces.yt.data.Account;
import io.reactivex.Observable;

import java.util.List;

public interface SignInService {
    interface OnAccountChange {
        void onAccountChanged(Account account);
    }
    void signOut();
    boolean isSigned();
    List<Account> getAccounts();
    Account getSelectedAccount();
    void selectAccount(Account account);
    void removeAccount(Account account);
    void addOnAccountChange(OnAccountChange listener);

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
