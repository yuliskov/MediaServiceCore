package com.liskovsoft.googleapi.drive3

import android.net.Uri
import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.common.helpers.RetrofitHelper.ErrorAlreadyExists
import com.liskovsoft.googleapi.common.helpers.RetrofitHelper.ErrorNotExists
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
            val fileQuery = if (metadata?.id == null) "name = '${name}'" else "name = '${name}' and parents in '${metadata?.id}'"
            val thisMetadata = RetrofitHelper.get(mDriveApi.getList(fileQuery))?.files?.first()

            metadata = if (idx == segments.lastIndex) {
                createFileCatch(name, file, thisMetadata?.id, metadata?.id)
            } else {
                createFolderCatch(name, thisMetadata?.id, metadata?.id)
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

    private fun createFolderCatch(folderName: String, folderId: String?, parentFolderId: String?): FileMetadata? {
        return try {
            createFolder(folderName, folderId, parentFolderId)
        } catch (e: IllegalStateException) { // id not exist
            when (e.cause) {
                is ErrorNotExists -> createFolder(folderName, null, parentFolderId)
                else -> null
            }
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
            when (e.cause) {
                is ErrorNotExists -> createFile(fileName, contents, null, parentFolderId)
                is ErrorAlreadyExists -> updateFile(contents, fileId)
                else -> null
            }
        }
    }

    private fun createFile(fileName: String, contents: File, fileId: String?, parentFolderId: String?): FileMetadata? {
        val requestBody = RequestBody.create(MediaType.parse(FILE_MIME_TYPE), contents)
        val metadata = FileMetadata(id = fileId, name = fileName, mimeType = FILE_MIME_TYPE, parents = parentFolderId?.let { listOf(parentFolderId) })
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
            val fileQuery = if (metadata?.id == null) "name = '${name}'" else "name = '${name}' and parents in '${metadata?.id}'"
            metadata = RetrofitHelper.get(mDriveApi.getList(fileQuery))?.files?.first()

            if (idx == segments.lastIndex) {
                return metadata?.id
            }
        }

        return null
    }
}