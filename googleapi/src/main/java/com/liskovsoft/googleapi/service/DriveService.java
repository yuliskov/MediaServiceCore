package com.liskovsoft.googleapi.service;

import android.net.Uri;

import com.liskovsoft.googleapi.drive3.DriveServiceInt;
import com.liskovsoft.sharedutils.rx.RxHelper;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;

public class DriveService {
    private static DriveService sInstance;

    private DriveService() {
        
    }

    public static DriveService instance() {
        if (sInstance == null) {
            sInstance = new DriveService();
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
