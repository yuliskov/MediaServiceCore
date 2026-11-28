package com.liskovsoft.youtubeapi.app.nsigsolver.impl

/**
 * Solves the n-parameter challenge for TCL-flavored players (tv-player-*-tcl.vflset), whose
 * n-function has a different call shape than the regular web/TV player and isn't recognized by
 * the generic (yt-dlp ejs) [WebChallengeProvider].
 *
 * Unlike the regular player, the TCL n-function is invoked with a full URL object (it mutates the
 * "n" query param in place) rather than the bare n-param string — [solveN] hides that behind the
 * same bare-nParam contract the rest of the codebase uses, by wrapping/unwrapping a synthetic
 * placeholder URL.
 */
internal object TclChallengeProvider: BaseChallengeProvider() {
    override val cacheSection: String
        get() = "tcl-challenge-solver"
}
