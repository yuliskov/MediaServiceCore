package com.liskovsoft.googleapi.drive3.data

internal data class FileMetadata(
    val id: String? = null,
    val name: String? = null,
    val parents: List<String?>? = null,
    val mimeType: String? = null,
    val kind: String? = null // always drive#file
)