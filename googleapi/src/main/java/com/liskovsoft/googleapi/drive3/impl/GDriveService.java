package com.liskovsoft.googleapi.drive3.impl;

import android.net.Uri;

import com.liskovsoft.googleapi.drive3.DriveServiceInt;
import com.liskovsoft.sharedutils.rx.RxHelper;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;

public class GDriveService {
    private static GDriveService sInstance;

    private GDriveService() {
        
    }

    public static GDriveService instance() {
        if (sInstance == null) {
            sInstance = new GDriveService();
        }

        return sInstance;
    }

    public Observable<Void> uploadFile(File file, Uri path) {
        return RxHelper.fromRunnable(() -> DriveServiceInt.uploadFile(file, path));
    }

    public Observable<Void> uploadFile(String content, Uri path) {
        return RxHelper.fromRunnable(() -> DriveServiceInt.uploadFile(content, path));
    }

    public Observable<InputStream> getFile(Uri path) {
        return RxHelper.fromCallable(() -> DriveServiceInt.getFile(path));
    }

    public Observable<List<String>> getList(Uri path) {
        return RxHelper.fromCallable(() -> DriveServiceInt.getList(path));
    }
}
