package com.liskovsoft.googleapi.service;

import androidx.annotation.Nullable;

import com.liskovsoft.googleapi.oauth2.impl.SignInServiceImpl;
import com.liskovsoft.mediaserviceinterfaces.google.SignInService;
import com.liskovsoft.mediaserviceinterfaces.google.data.Account;
import com.liskovsoft.mediaserviceinterfaces.google.data.SignInCode;

import java.util.List;

import io.reactivex.Observable;

public class GoogleSignInService implements SignInService {
    private static final String TAG = GoogleSignInService.class.getSimpleName();
    private static GoogleSignInService sInstance;
    private final SignInServiceImpl mSignInService;

    private GoogleSignInService() {
        mSignInService = SignInServiceImpl.instance();
    }

    public static GoogleSignInService instance() {
        if (sInstance == null) {
            sInstance = new GoogleSignInService();
        }

        return sInstance;
    }

    @Override
    public Observable<SignInCode> signInObserve() {
        return mSignInService.signInObserve();
    }

    @Override
    public void signOut() {
        mSignInService.signOut();
    }

    @Override
    public Observable<Void> signOutObserve() {
        return mSignInService.signOutObserve();
    }

    @Override
    public boolean isSigned() {
        return mSignInService.isSigned();
    }

    @Override
    public Observable<Boolean> isSignedObserve() {
        return mSignInService.isSignedObserve();
    }

    @Override
    public List<Account> getAccounts() {
        return mSignInService.getAccounts();
    }

    @Override
    public Observable<List<Account>> getAccountsObserve() {
        return mSignInService.getAccountsObserve();
    }

    @Nullable
    @Override
    public Account getSelectedAccount() {
        return mSignInService.getSelectedAccount();
    }

    @Override
    public void selectAccount(Account account) {
        mSignInService.selectAccount(account);
    }

    @Override
    public void removeAccount(Account account) {
        mSignInService.removeAccount(account);
    }

    @Override
    public void setOnChange(Runnable onChange) {
        mSignInService.setOnChange(onChange);
    }
}
