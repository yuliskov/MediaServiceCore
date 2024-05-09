package com.liskovsoft.googleapi.drive3.data

internal data class ListResult(
    val nextPageToken: String?,
    val kind: String?,
    val incompleteSearch: Boolean?,
    val files: List<FileMetadata?>?
)
