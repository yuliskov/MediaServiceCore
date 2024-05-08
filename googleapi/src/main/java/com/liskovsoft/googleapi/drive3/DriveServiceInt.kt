package com.liskovsoft.googleapi.drive3

import android.net.Uri
import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.drive3.data.FileMetadata
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream

internal object DriveServiceInt {
    private const val FILE_MIME_TYPE = "text/plain"
    private val mDriveApi = RetrofitHelper.withGson(DriveApi::class.java)
    private val mPathMapping = restoreMapping()

    @JvmStatic
    fun uploadFile(file: File, path: Uri) {
        val segments = path.pathSegments
        var metadata: FileMetadata? = null
        var lastPath = ""

        segments.forEachIndexed { idx, name ->
            val currentPath = "$lastPath/$name"
            metadata = if (idx == segments.lastIndex) {
                createFileCatch(name, file, mPathMapping[currentPath], mPathMapping[lastPath] ?: metadata?.id)
            } else {
                createFolderCatch(name, mPathMapping[currentPath], mPathMapping[lastPath] ?: metadata?.id)
            }
            lastPath = currentPath
            metadata?.id?.let { mPathMapping[lastPath] = it }
        }

        persistMapping()
    }

    @JvmStatic
    fun getFile(path: Uri): InputStream? {
        // Fix missing slash at the beginning
        var properPath = path.toString()
        if (!properPath.startsWith("/")) {
            properPath = "/$properPath"
        }

        val fileId = mPathMapping[properPath] ?: return null

        val file = RetrofitHelper.get(mDriveApi.getFile(fileId))

        //Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);

        return file?.byteStream()
    }

    private fun createFolderCatch(folderName: String, folderId: String?, parentFolderId: String?): FileMetadata? {
        return try {
            createFolder(folderName, folderId, parentFolderId)
        } catch (e: IllegalStateException) { // id not exist
            createFolder(folderName, null, parentFolderId)
        }
    }

    private fun createFolder(folderName: String, folderId: String?, parentFolderId: String?): FileMetadata? {
        val metadata = FileMetadata(id = folderId, name = folderName,
            mimeType = DriveApiHelper.GOOGLE_FOLDER_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
        return RetrofitHelper.get(mDriveApi.createFolder(metadata))
    }

    private fun createFileCatch(fileName: String, contents: File, fileId: String?, parentFolderId: String?): FileMetadata? {
        return try {
            createFile(fileName, contents, fileId, parentFolderId)
        } catch (e: IllegalStateException) { // id not exist
            createFile(fileName, contents, null, parentFolderId)
        }
    }

    private fun createFile(fileName: String, contents: File, fileId: String?, parentFolderId: String?): FileMetadata? {
        val requestBody = RequestBody.create(MediaType.parse(FILE_MIME_TYPE), contents)
        val metadata = FileMetadata(id = fileId, name = fileName, mimeType = FILE_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
        return RetrofitHelper.get(mDriveApi.uploadFile(metadata, requestBody))
    }

    private fun persistMapping() {
        GlobalPreferences.sInstance?.driveMappingData = Helpers.mergeMap(mPathMapping)
    }

    private fun restoreMapping(): MutableMap<String, String> {
        return GlobalPreferences.sInstance?.driveMappingData?.let { Helpers.parseMap(it, String::toString, String::toString) } ?: mutableMapOf()
    }
}