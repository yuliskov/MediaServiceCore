package com.liskovsoft.googleapi.service

import android.net.Uri
import com.liskovsoft.googleapi.drive3.DriveServiceInt
import com.liskovsoft.sharedutils.rx.RxHelper
import io.reactivex.Observable
import java.io.File
import java.io.InputStream

object DriveService {
    @JvmStatic
    fun uploadFile(file: File, path: Uri): Observable<Void> {
        return RxHelper.fromRunnable { DriveServiceInt.uploadFile(file, path) }
    }

    @JvmStatic
    fun uploadFile(content: String, path: Uri): Observable<Void> {
        return RxHelper.fromRunnable { DriveServiceInt.uploadFile(content, path) }
    }

    @JvmStatic
    fun getFile(path: Uri): Observable<InputStream?> {
        return RxHelper.fromCallable { DriveServiceInt.getFile(path) }
    }

    @JvmStatic
    fun getFileList(path: Uri): Observable<List<String?>?> {
        return RxHelper.fromCallable { DriveServiceInt.getFileList(path) }
    }

    @JvmStatic
    fun getFolderList(path: Uri): Observable<List<String?>?> {
        return RxHelper.fromCallable { DriveServiceInt.getFolderList(path) }
    }
}