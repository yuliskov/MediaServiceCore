package com.liskovsoft.googleapi.drive3

import android.net.Uri
import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.drive3.data.FileMetadata
import com.liskovsoft.googleapi.oauth2.impl.GoogleSignInService
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream

internal object DriveServiceInt {
    private const val FILE_MIME_TYPE = "text/plain"
    private val mDriveApi = RetrofitHelper.withGson(DriveApi::class.java)
    private val mSignInService = GoogleSignInService.instance()

    @JvmStatic
    fun uploadFile(file: File, path: Uri) {
        mSignInService.checkAuth()

        val segments = path.pathSegments
        var metadata: FileMetadata? = null

        segments.forEachIndexed { idx, name ->
            val thisMetadata = findMetadata(name, metadata?.id)

            metadata = if (idx == segments.lastIndex) {
                createOrUpdateFile(name, file, thisMetadata?.id, metadata?.id)
            } else {
                createOrUpdateFolder(name, thisMetadata?.id, metadata?.id)
            }

            if (metadata == null) {
                metadata = thisMetadata
            }
        }
    }

    @JvmStatic
    fun getFile(path: Uri): InputStream? {
        mSignInService.checkAuth()

        val fileId = findFileId(path) ?: return null

        val file = RetrofitHelper.get(mDriveApi.getFile(fileId))

        return file?.byteStream()
    }

    @JvmStatic
    fun getList(path: Uri): List<String?>? {
        mSignInService.checkAuth()

        val folderId = findFileId(path) ?: return null

        // List folder contents
        val folderContentsQuery = "mimeType='$FILE_MIME_TYPE' and parents in '$folderId'"

        return RetrofitHelper.get(mDriveApi.getList(folderContentsQuery))?.files?.mapNotNull { it?.name }
    }

    private fun createOrUpdateFolder(folderName: String, folderId: String?, parentFolderId: String?): FileMetadata? {
        return if (folderId == null) {
            createFolder(folderName, parentFolderId)
        } else null // already exists
    }

    private fun createFolder(folderName: String, parentFolderId: String?): FileMetadata? {
        val metadata = FileMetadata(name = folderName,
            mimeType = DriveApiHelper.GOOGLE_FOLDER_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
        return RetrofitHelper.get(mDriveApi.createFolder(metadata))
    }

    private fun createOrUpdateFile(fileName: String, contents: File, fileId: String?, parentFolderId: String?): FileMetadata? {
        return if (fileId == null) {
            createFile(fileName, contents, parentFolderId)
        } else {
            updateFile(contents, fileId)
        }
    }

    private fun createFile(fileName: String, contents: File, parentFolderId: String?): FileMetadata? {
        val requestBody = RequestBody.create(MediaType.parse(FILE_MIME_TYPE), contents)
        val metadata = FileMetadata(name = fileName, mimeType = FILE_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
        return RetrofitHelper.get(mDriveApi.uploadFile(metadata, requestBody))
    }

    private fun updateFile(contents: File, fileId: String?): FileMetadata? {
        val requestBody = RequestBody.create(MediaType.parse(FILE_MIME_TYPE), contents)
        return fileId?.let { RetrofitHelper.get(mDriveApi.updateFile(fileId, requestBody)) }
    }

    private fun findFileId(path: Uri): String? {
        val segments = path.pathSegments
        var metadata: FileMetadata? = null

        segments.forEachIndexed { idx, name ->
            metadata = findMetadata(name, metadata?.id)

            if (idx == segments.lastIndex) {
                return metadata?.id
            }
        }

        return null
    }

    private fun findMetadata(name: String, parentId: String?): FileMetadata? {
        val fileQuery = if (parentId == null) "name = '${name}'" else "name = '${name}' and parents in '${parentId}'"
        return RetrofitHelper.get(mDriveApi.getList(fileQuery))?.files?.first()
    }
}