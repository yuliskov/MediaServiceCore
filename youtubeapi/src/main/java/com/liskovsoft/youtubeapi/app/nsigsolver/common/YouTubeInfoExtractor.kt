package com.liskovsoft.youtubeapi.app.nsigsolver.common

import kotlinx.coroutines.runBlocking

internal object YouTubeInfoExtractor: InfoExtractor() {
    val cache: Cache = Cache()
    private var playerCache: Pair<String, String>? = null

    // TODO: implement caching to the local storage
    fun loadPlayer(playerUrl: String): String = runBlocking {
        playerCache?.let {
            if (it.first == playerUrl)
                return@runBlocking it.second
        }

        val webPage = downloadWebpage(playerUrl)

        playerCache = Pair(playerUrl, webPage)

        return@runBlocking webPage
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