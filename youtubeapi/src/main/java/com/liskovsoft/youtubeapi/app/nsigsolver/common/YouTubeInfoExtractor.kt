package com.liskovsoft.youtubeapi.app.nsigsolver.common

import kotlinx.coroutines.runBlocking

internal object YouTubeInfoExtractor: InfoExtractor() {
    val cache: CacheService = CacheService

    fun loadPlayer(playerUrl: String): String = runBlocking {
        return@runBlocking downloadWebpage(playerUrl)
    }

    fun loadPlayerSilent(playerUrl: String): String? {
        return try {
            loadPlayer(playerUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun downloadWebpageWithRetries(url: String, errorMsg: String? = null): String = runBlocking {
        return@runBlocking downloadWebpage(url, tries = 3, errorMsg = errorMsg)
    }

    fun downloadWebpageSilent(url: String): String? {
        return try {
            downloadWebpageWithRetries(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}