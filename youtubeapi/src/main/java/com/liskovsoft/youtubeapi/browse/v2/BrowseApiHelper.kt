package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal object BrowseApiHelper {
    private const val CHANNEL = "\"browseId\":\"%s\""
    private const val CHANNEL_FULL = "\"browseId\":\"%s\",\"params\":\"%s\""
    private const val CHANNEL_HOME = "\"browseId\":\"%s\",\"params\":\"EghmZWF0dXJlZPIGBAoCMgA%%3D\""
    private const val CHANNEL_VIDEOS = "\"browseId\":\"%s\",\"params\":\"EgZ2aWRlb3PyBgQKAjoA\""
    private const val CHANNEL_SHORTS = "\"browseId\":\"%s\",\"params\":\"EgZzaG9ydHPyBgUKA5oBAA%%3D%%3D\""
    private const val CHANNEL_LIVE = "\"browseId\":\"%s\",\"params\":\"EgdzdHJlYW1z8gYECgJ6AA%%3D%%3D\""
    private const val CHANNEL_PLAYLISTS = "\"browseId\":\"%s\",\"params\":\"EglwbGF5bGlzdHPyBgQKAkIA\""
    private const val CHANNEL_RELEASES = "\"browseId\":\"%s\",\"params\":\"EghyZWxlYXNlc_IGBQoDsgEA\""
    private const val CHANNEL_COMMUNITY = "\"browseId\":\"%s\",\"params\":\"Egljb21tdW5pdHnyBgQKAkoA\""
    private const val CHANNEL_SEARCH = "\"browseId\":\"%s\",\"params\":\"EgZzZWFyY2jyBgQKAloA\",\"query\":\"%s\""
    private const val KIDS_HOME = "\"browseId\":\"FEkids_home\""
    private const val KIDS_HOME_PARAMS = "\"browseId\":\"FEkids_home\",\"params\":\"%s\""
    private const val WHAT_TO_WATCH = "\"browseId\":\"FEwhat_to_watch\""
    private const val TRENDING = "\"browseId\":\"FEtrending\",\"params\":\"6gQJRkVleHBsb3Jl\""
    private const val SUBSCRIPTIONS = "\"browseId\":\"FEsubscriptions\""
    private const val SPORTS = "\"browseId\":\"FEtopics_sports\""
    private const val MOVIES = "\"browseId\":\"FEtopics_movies\""
    private const val LIKED_MUSIC = "\"browseId\":\"VLLM\""
    private const val REEL = "\"disablePlayerResponse\":true,\"inputType\":\"REEL_WATCH_INPUT_TYPE_SEEDLESS\",\"params\":\"CA8%3D\""
    private const val REEL_DETAILS = "\"disablePlayerResponse\":true,\"params\":\"%s\",\"playerRequest\":{\"videoId\":\"%s\"}"
    private const val REEL_CONTINUATION = "\"sequenceParams\":\"%s\""
    private const val CONTINUATION = "\"continuation\":\"%s\""

    fun getHomeQueryWeb(): String {
        return ServiceHelper.createQueryWeb(WHAT_TO_WATCH)
    }

    fun getHomeQueryTV(): String {
        return ServiceHelper.createQueryTV(WHAT_TO_WATCH)
    }

    fun getHomeQueryMWEB(): String {
        return ServiceHelper.createQueryMWeb(WHAT_TO_WATCH)
    }

    fun getTrendingQueryWeb(): String {
        return ServiceHelper.createQueryWeb(TRENDING)
    }

    fun getChannelQueryWeb(channelId: String, params: String? = null): String {
        val channelTemplate =
            if (params != null) String.format(CHANNEL_FULL, channelId, params) else String.format(CHANNEL, channelId)
        return ServiceHelper.createQueryWeb(channelTemplate)
    }

    fun getChannelHomeQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_HOME, channelId))
    }

    fun getChannelVideosQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_VIDEOS, channelId))
    }

    fun getChannelShortsQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_SHORTS, channelId))
    }

    fun getChannelLiveQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_LIVE, channelId))
    }

    fun getChannelPlaylistsQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_PLAYLISTS, channelId))
    }

    fun getChannelReleasesQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_RELEASES, channelId))
    }

    fun getChannelCommunityQueryWeb(channelId: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_COMMUNITY, channelId))
    }

    fun getChannelSearchQueryWeb(channelId: String, query: String): String {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_SEARCH, channelId, query))
    }

    fun getKidsHomeQuery(): String {
        return ServiceHelper.createQueryKids(KIDS_HOME)
    }

    fun getKidsHomeQuery(params: String): String {
        return ServiceHelper.createQueryKids(String.format(KIDS_HOME_PARAMS, params))
    }

    fun getReelQuery(): String {
        return ServiceHelper.createQueryWeb(REEL)
    }

    fun getLikedMusicQuery(): String {
        return ServiceHelper.createQueryWeb(LIKED_MUSIC)
    }

    fun getReelDetailsQuery(videoId: String, params: String): String {
        val details = String.format(REEL_DETAILS, params, videoId)
        return ServiceHelper.createQueryWeb(details)
    }

    fun getReelContinuationQuery(sequenceParams: String): String {
        val continuation = String.format(REEL_CONTINUATION, sequenceParams)
        return ServiceHelper.createQueryWeb(continuation)
    }

    fun getReelContinuation2Query(nextPageKey: String): String {
        val continuation = String.format(CONTINUATION, nextPageKey)
        return ServiceHelper.createQueryWeb(continuation)
    }

    fun getSubscriptionsQueryWeb(): String {
        return ServiceHelper.createQueryWeb(SUBSCRIPTIONS)
    }

    fun getBrowseQueryWeb(browseId: String, browseParams: String?): String {
        return ServiceHelper.createQueryWeb(browseParams?.let { String.format(CHANNEL_FULL, browseId, browseParams) } ?: String.format(CHANNEL, browseId))
    }

    fun getSportsQueryTV(): String {
        return ServiceHelper.createQueryTV(SPORTS)
    }

    fun getMoviesQueryTV(): String {
        return ServiceHelper.createQueryTV(MOVIES)
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey [GridTab.getNextPageKey]
     * @return data param
     */
    fun getContinuationQueryWeb(nextPageKey: String): String {
        val continuation = String.format(CONTINUATION, nextPageKey)
        return ServiceHelper.createQueryWeb(continuation)
    }

    fun getContinuationQueryTV(nextPageKey: String): String {
        val continuation = String.format(CONTINUATION, nextPageKey)
        return ServiceHelper.createQueryTV(continuation)
    }
}