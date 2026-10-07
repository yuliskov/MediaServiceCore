package com.liskovsoft.youtubeapi.app

/**
 * Holds the token from SABR `ReloadPlayerResponse` (`reload_playback_params`).
 *
 * The SABR server may stop sending media and ask the client to re-fetch the player response.
 * The next player request for the same video should include the token:
 * `playbackContext.reloadPlaybackContext.reloadPlaybackParams.token`
 */
internal object ReloadPlaybackGate {
    private class Entry(val videoId: String, val token: String)

    @Volatile
    private var mEntry: Entry? = null

    @JvmStatic
    fun setToken(videoId: String?, token: String?) {
        mEntry = if (videoId != null && token != null) Entry(videoId, token) else null
    }

    @JvmStatic
    fun getToken(videoId: String?): String? {
        val entry = mEntry
        return if (entry != null && entry.videoId == videoId) entry.token else null
    }

    /**
     * The token is single use. Don't touch the token of another video (e.g. preloading).
     */
    @JvmStatic
    fun reset(videoId: String?) {
        if (mEntry?.videoId == videoId) {
            mEntry = null
        }
    }
}