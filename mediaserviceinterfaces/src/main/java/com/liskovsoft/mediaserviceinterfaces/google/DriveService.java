package com.liskovsoft.mediaserviceinterfaces.google;

import android.net.Uri;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;

public interface DriveService {
    Observable<Void> uploadFile(File file, Uri path);
    Observable<Void> uploadFile(String content, Uri path);
    Observable<InputStream> getFile(Uri path);
    Observable<List<String>> getList(Uri path);
}
