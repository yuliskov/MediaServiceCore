package com.liskovsoft.googleapi.drive3

import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googleapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.googleapi.drive3.data.FileMetadata
import com.liskovsoft.sharedutils.helpers.Helpers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class DriveApiTest {
    private lateinit var mService: DriveApi
    private val mTestRequestBody = RequestBody.create(MediaType.parse("text/plain"), TEST_FILE_CONTENTS)
    private val mTestRequestBody2 = RequestBody.create(MediaType.parse("text/plain"), TEST_FILE_CONTENTS2)

    companion object {
        private const val TEST_FOLDER_NAME = "testFolder"
        private const val TEST_FILE_NAME = "testFile"
        private const val TEST_FILE_CONTENTS = "Sample file contents"
        private const val TEST_FILE_CONTENTS2 = "Updated sample file contents"
    }

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")

        ShadowLog.stream = System.out // catch Log class output

        mService = RetrofitHelper.withGson(DriveApi::class.java)

        RetrofitOkHttpHelper.disableCompression = true
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
    }

    @Test
    fun testGenerateIds() {
        val generatedIds = RetrofitHelper.get(mService.generateIds())
        assertTrue("Has ids", (generatedIds?.ids?.size ?: 0) > 0)
    }

    @Test
    fun testCreateFolder() {
        val folderMetadata = createSampleFolder()

        assertMetadata(folderMetadata)

        cleanup(folderMetadata)
    }

    @Test
    fun testUploadFile() {
        val folderMetadata = createSampleFolder()

        val requestBody = mTestRequestBody
        val metadata = FileMetadata(name = TEST_FILE_NAME, mimeType = "text/plain", parents = listOf(folderMetadata?.id))
        val fileMetadata = RetrofitHelper.get(mService.uploadFile(metadata, requestBody))

        assertMetadata(fileMetadata)

        cleanup(folderMetadata, fileMetadata)
    }

    @Test
    fun testGetFile() {
        val fileMetadata = createSampleFile()

        val file = RetrofitHelper.get(mService.getFile(fileMetadata?.id!!))

        assertTrue("File has contents", Helpers.toString(file?.byteStream()) == TEST_FILE_CONTENTS)

        cleanup(fileMetadata)
    }

    @Test
    fun testUpdateFile() {
        val fileMetadata = createSampleFile()

        val metadata = RetrofitHelper.get(mService.updateFile(fileMetadata?.id!!, mTestRequestBody2))

        assertMetadata(metadata)

        cleanup(fileMetadata)
    }

    private fun createSampleFolder() =
        RetrofitHelper.get(mService.createFolder(FileMetadata(name = TEST_FOLDER_NAME, mimeType = DriveApiHelper.GOOGLE_FOLDER_MIME_TYPE)))

    private fun createSampleFile(): FileMetadata? {
        val requestBody = mTestRequestBody
        val fileMetadata = FileMetadata(name = TEST_FILE_NAME, mimeType = "text/plain")
        return RetrofitHelper.get(mService.uploadFile(fileMetadata, requestBody))
    }

    private fun cleanup(vararg items: FileMetadata?) {
        for (metadata in items) {
            RetrofitHelper.get(mService.deleteFile(metadata?.id!!))
        }
    }

    private fun assertMetadata(folderMetadata: FileMetadata?) {
        assertNotNull("File has kind", folderMetadata?.kind)
        assertNotNull("File has id", folderMetadata?.id)
    }
}