package com.liskovsoft.googleapi.drive3.impl;

import android.net.Uri;

import com.liskovsoft.googleapi.drive3.DriveServiceInt;
import com.liskovsoft.mediaserviceinterfaces.google.DriveService;
import com.liskovsoft.sharedutils.rx.RxHelper;

import java.io.File;
import java.io.InputStream;
import java.util.List;

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
    public Observable<Void> uploadFile(String content, Uri path) {
        return RxHelper.fromVoidable(() -> DriveServiceInt.uploadFile(content, path));
    }

    @Override
    public Observable<InputStream> getFile(Uri path) {
        return RxHelper.fromCallable(() -> DriveServiceInt.getFile(path));
    }

    @Override
    public Observable<List<String>> getList(Uri path) {
        return RxHelper.fromCallable(() -> DriveServiceInt.getList(path));
    }
}
