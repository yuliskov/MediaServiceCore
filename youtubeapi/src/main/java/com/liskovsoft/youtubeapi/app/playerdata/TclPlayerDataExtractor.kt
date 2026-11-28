package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.youtubeapi.app.nsigsolver.impl.TclChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.impl.WebChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProvider
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

private const val TCL_URL = "/tv-player-es6-tcl.vflset/tv-player-es6-tcl.js"

/**
 * [playerUrl] is the TCL-flavored player (from tv_config).
 * Its n-function has a different call shape than the regular
 * web/TV player, so n-param extraction is routed to [TclChallengeProvider] instead of the
 * generic (yt-dlp ejs) [WebChallengeProvider]. The TCL player isn't required decipher/sig
 * function, so signature extraction is never supported by it.
 */
internal class TclPlayerDataExtractor(playerUrl: String): BasePlayerDataExtractor(playerUrl) {
    private val tag = TclPlayerDataExtractor::class.java.simpleName
    private val data
        get() = MediaServiceData.instance()

    override fun fixPlayerUrl(playerUrl: String): String {
        // The hash must come from tv_config: the n-params are bound to that exact player build.
        // Don't substitute the path of the regular player (its hash differs).
        // See https://github.com/yt-dlp/yt-dlp/issues/12398
        // tv url: https://www.youtube.com/s/player/69b31e11/tv-player-es6-tce.vflset/tv-player-es6-tce.js
        // web url: https://www.youtube.com/s/player/e12fbea4/player_ias_tce.vflset/en_US/base.js
        return playerUrl
            .replace("/tv-player-es6.vflset/tv-player-es6.js", TCL_URL)
            .replace("/tv-player-ias.vflset/tv-player-ias.js", TCL_URL)
            .replace("/player_es6.vflset/en_US/base.js", TCL_URL)
            .replace("/player_ias.vflset/en_US/base.js", TCL_URL)
    }

    override val challengeProvider: JsChallengeProvider
        get() = TclChallengeProvider

    override var playerExtractorCache: PlayerExtractorCache?
        get() = data.tclPlayerExtractorCache
        set(cache) { data.tclPlayerExtractorCache = cache }
}