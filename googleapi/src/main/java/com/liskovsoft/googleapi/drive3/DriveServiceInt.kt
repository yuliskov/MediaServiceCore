package com.liskovsoft.googleapi.drive3

import android.net.Uri
import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.drive3.data.FileMetadata
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

internal object DriveServiceInt {
    private val mDriveApi = RetrofitHelper.withGson(DriveApi::class.java)
    private const val FILE_MIME_TYPE = "text/plain"

    @JvmStatic
    fun uploadFile(file: File, path: Uri) {
        val segments = path.pathSegments
        var metadata: FileMetadata? = null

        segments.forEachIndexed { idx, name ->
            if (idx == segments.lastIndex) {
                createFile(name, file, metadata?.id)
            } else {
                metadata = createFolder(name, metadata?.id)
            }
        }
    }

    @JvmStatic
    fun getFile(path: Uri): File? {
        // get id form path to id mapping
        // get the file by id or null
        return null
    }

    private fun createFolder(folderName: String, parentFolderId: String?): FileMetadata? {
        val metadata = FileMetadata(name = folderName, mimeType = DriveApiHelper.GOOGLE_FOLDER_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
        return RetrofitHelper.get(mDriveApi.createFolder(metadata))
    }

    private fun createFile(fileName: String, contents: File, parentFolderId: String?): FileMetadata? {
        val requestBody = RequestBody.create(MediaType.parse(FILE_MIME_TYPE), contents)
        val metadata = FileMetadata(name = fileName, mimeType = FILE_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
        return RetrofitHelper.get(mDriveApi.uploadFile(metadata, requestBody))
    }
}