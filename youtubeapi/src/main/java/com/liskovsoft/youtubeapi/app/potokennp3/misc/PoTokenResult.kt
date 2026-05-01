package com.liskovsoft.youtubeapi.app.potokennp3.misc;

/**
 * The result of a supported/successful `poToken` extraction request by a
 * {@link PoTokenProvider}.
 */
internal class PoTokenResult(
    val videoId: String,
    /**
     * The visitor data associated with a `poToken`.
     */
    val visitorData: String,
    /**
     * The `poToken` of a player request, a Protobuf object encoded as a base 64 string.
     */
    val playerRequestPoToken: String,
    /**
     * The `poToken` to be appended to streaming URLs, a Protobuf object encoded as a base
     * 64 string.
     *
     * It may be required on some clients such as HTML5 ones and may also differ from the player
     * request `poToken`.
     */
    val streamingDataPoToken: String?)
