package com.liskovsoft.youtubeapi.app.potokennp3.misc;

/**
 * Interface to provide `poToken`s to YouTube player requests.
 *
 * On some major clients, YouTube requires that the integrity of the device passes some checks to
 * allow playback.
 *
 * These checks involve running codes to verify the integrity and using their result to generate
 * one or multiple `poToken`(s) (which stands for proof of origin token(s)).
 *
 * These tokens may have a role in triggering the sign in requirement.
 *
 * If an implementation does not want to return a `poToken` for a specific client, it **must
 * return `null`**.
 *
 * **Implementations of this interface are expected to be thread-safe, as they may be accessed by
 * multiple threads.**
 */
internal interface PoTokenProvider {

    /**
     * Get a [PoTokenResult] specific to the desktop website, a.k.a. the WEB InnerTube client.
     *
     * To be generated and valid, `poToken`s from this client must be generated using Google's
     * BotGuard machine, which requires a JavaScript engine with a good DOM implementation. They
     * must be added to adaptive/DASH streaming URLs with the {@code pot} parameter.
     *
     * Note that YouTube desktop website generates two `poToken`s:
     * - one for the player requests `poToken`s, using the videoId as the minter value;
     * - one for the streaming URLs, using a visitor data for logged-out users as the minter value.
     *
     * @return a [PoTokenResult]] specific to the WEB InnerTube client
     */
    fun getWebClientPoToken(videoId: String): PoTokenResult?

    val isWebPotExpired: Boolean

    val isWebPotSupported: Boolean
}
