package com.liskovsoft.youtubeapi.innertube.models

internal data class SessionDataProcessed(
    val apiKey: String,
    val apiVersion: String,
    val configData: String,
    val context: ContextInfo
)