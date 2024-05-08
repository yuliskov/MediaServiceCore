package com.liskovsoft.googleapi.oauth2.impl;

import androidx.annotation.Nullable;

import com.liskovsoft.googleapi.oauth2.manager.OAuth2AccountManager;
import com.liskovsoft.mediaserviceinterfaces.google.SignInService;
import com.liskovsoft.mediaserviceinterfaces.google.data.Account;
import com.liskovsoft.mediaserviceinterfaces.google.data.SignInCode;
import com.liskovsoft.sharedutils.rx.RxHelper;

import java.util.List;

import io.reactivex.Observable;

public class SignInServiceImpl implements SignInService {
    private static final String TAG = SignInServiceImpl.class.getSimpleName();
    private static SignInServiceImpl sInstance;
    private final OAuth2AccountManager mAccountManager;

    private SignInServiceImpl() {
        mAccountManager = OAuth2AccountManager.instance();
    }

    public static SignInServiceImpl instance() {
        if (sInstance == null) {
            sInstance = new SignInServiceImpl();
        }

        return sInstance;
    }

    @Override
    public Observable<SignInCode> signInObserve() {
        return RxHelper.createLong(emitter -> {
            SignInCode signInCode = mAccountManager.getSignInCode();

            if (signInCode == null) {
                RxHelper.onError(emitter, "User code result is empty");
                return;
            }

            emitter.onNext(signInCode);

            mAccountManager.waitUserCodeConfirmation();

            emitter.onComplete();
        });
    }

    @Override
    public void signOut() {
        // TODO: not implemented
    }

    @Override
    public Observable<Void> signOutObserve() {
        return RxHelper.create(emitter -> {
            signOut();
            emitter.onComplete();
        });
    }

    @Override
    public boolean isSigned() {
        // Condition created for the case when a device in offline mode.
        return mAccountManager.getSelectedAccount() != null;
    }

    @Override
    public Observable<Boolean> isSignedObserve() {
        return RxHelper.fromCallable(this::isSigned);
    }

    @Override
    public List<Account> getAccounts() {
        return mAccountManager.getAccounts();
    }

    @Override
    public Observable<List<Account>> getAccountsObserve() {
        return RxHelper.fromCallable(this::getAccounts);
    }

    @Nullable
    @Override
    public Account getSelectedAccount() {
        return mAccountManager.getSelectedAccount();
    }

    @Override
    public void selectAccount(Account account) {
        mAccountManager.selectAccount(account);
    }

    @Override
    public void removeAccount(Account account) {
        mAccountManager.removeAccount(account);
    }

    @Override
    public void setOnChange(Runnable onChange) {
        mAccountManager.setOnChange(onChange);
    }

    public void checkAuth() {
        mAccountManager.checkAuth();
    }
}
