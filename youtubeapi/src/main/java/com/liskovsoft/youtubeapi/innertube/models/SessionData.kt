package com.liskovsoft.youtubeapi.innertube.models

internal data class SessionData(
    val apiKey: String,
    val apiVersion: String,
    val configData: String,
    val context: InnertubeContext,
    val userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 OPR/121.0.0.0", // TODO: replace with the real values
    val accountIndex: Int = 0
)