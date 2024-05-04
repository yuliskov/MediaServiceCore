package com.liskovsoft.googleapi.service;

import android.net.Uri;

import com.liskovsoft.googleapi.drive3.DriveServiceInt;
import com.liskovsoft.mediaserviceinterfaces.google.DriveService;
import com.liskovsoft.sharedutils.rx.RxHelper;

import java.io.File;

import io.reactivex.Observable;

public class GDriveService implements DriveService {
    private static GDriveService sInstance;

    private GDriveService() {
    }

    public static GDriveService instance() {
        if (sInstance == null) {
            sInstance = new GDriveService();
        }

        return sInstance;
    }

    @Override
    public Observable<Void> uploadFile(File file, Uri path) {
        return RxHelper.fromVoidable(() -> DriveServiceInt.uploadFile(file, path));
    }

    @Override
    public Observable<File> getFile(Uri path) {
        return RxHelper.fromCallable(() -> DriveServiceInt.getFile(path));
    }
}
