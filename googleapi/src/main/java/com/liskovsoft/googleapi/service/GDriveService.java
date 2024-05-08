package com.liskovsoft.googleapi.service;

import android.net.Uri;

import com.liskovsoft.googleapi.drive3.DriveServiceInt;
import com.liskovsoft.mediaserviceinterfaces.google.DriveService;
import com.liskovsoft.sharedutils.rx.RxHelper;

import java.io.File;
import java.io.InputStream;

import io.reactivex.Observable;

public class GDriveService implements DriveService {
    private static GDriveService sInstance;
    private final GoogleSignInService mSignInService;

    private GDriveService() {
        mSignInService = GoogleSignInService.instance();
    }

    public static GDriveService instance() {
        if (sInstance == null) {
            sInstance = new GDriveService();
        }

        return sInstance;
    }

    @Override
    public Observable<Void> uploadFile(File file, Uri path) {
        mSignInService.checkAuth();
        return RxHelper.fromVoidable(() -> DriveServiceInt.uploadFile(file, path));
    }

    @Override
    public Observable<InputStream> getFile(Uri path) {
        mSignInService.checkAuth();
        return RxHelper.fromCallable(() -> DriveServiceInt.getFile(path));
    }
}
