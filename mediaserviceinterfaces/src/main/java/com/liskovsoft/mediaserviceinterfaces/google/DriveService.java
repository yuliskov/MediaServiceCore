package com.liskovsoft.mediaserviceinterfaces.google;

import android.net.Uri;

import java.io.File;

import io.reactivex.Observable;

public interface DriveService {
    Observable<Void> uploadFile(File file, Uri path);
    Observable<File> getFile(Uri path);
}
