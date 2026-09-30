package com.liskovsoft.youtubeapi.app.playerdata

internal interface PlayerDataExtractor {
    fun extractNSig(nParam: String): String?
    fun extractSig(sParams: List<String?>): List<String?>?
    fun bulkSigExtract(nParams: List<String?>?, sParams: List<String?>?): Pair<List<String?>?, List<String?>?>
    fun createClientPlaybackNonce(): String?
    fun getSignatureTimestamp(): String?
    fun setSignatureTimestamp(timestamp: String)
    fun validate(): Boolean
    val playerUrl: String
}