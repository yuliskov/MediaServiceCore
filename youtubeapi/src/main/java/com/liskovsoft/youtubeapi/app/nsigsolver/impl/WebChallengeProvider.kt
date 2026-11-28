package com.liskovsoft.youtubeapi.app.nsigsolver.impl

internal object WebChallengeProvider: BaseChallengeProvider() {
    override val cacheSection: String
        get() = "web-challenge-solver"
}