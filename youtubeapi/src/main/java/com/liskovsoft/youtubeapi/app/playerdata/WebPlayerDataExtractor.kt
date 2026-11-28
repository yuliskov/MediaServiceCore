package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.youtubeapi.app.nsigsolver.impl.WebChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProvider
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class WebPlayerDataExtractor(playerUrl: String): BasePlayerDataExtractor(playerUrl) {
    private val tag = WebPlayerDataExtractor::class.java.simpleName
    private val data
        get() = MediaServiceData.instance()

    override fun fixPlayerUrl(playerUrl: String): String {
        // Those are implements global helper functions. No fix. Fallback to regular.
        // See https://github.com/yt-dlp/yt-dlp/issues/12398
        // tv url: https://www.youtube.com/s/player/69b31e11/tv-player-es6-tce.vflset/tv-player-es6-tce.js
        // web url: https://www.youtube.com/s/player/e12fbea4/player_ias_tce.vflset/en_US/base.js
        return playerUrl
            .replace("-tcl", "") // (403 fix, incompatible nParam, e.g. /tv-player-es6-tcl.vflset/tv-player-es6-tcl.js)
    }

    override val challengeProvider: JsChallengeProvider
        get() = WebChallengeProvider
    override var playerExtractorCache: PlayerExtractorCache?
        get() = data.playerExtractorCache
        set(cache) { data.playerExtractorCache = cache }
}