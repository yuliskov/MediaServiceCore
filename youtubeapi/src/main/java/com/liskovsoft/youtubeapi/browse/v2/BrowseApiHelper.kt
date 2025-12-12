package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper

internal object BrowseApiHelper {
    const val WATCH_LATER_CHANNEL_ID = "VLWL"
    const val LIKED_CHANNEL_ID = "VLLL"
    const val FAVORITES_CHANNEL_ID = "VLFL" // NOTE: it's only the beginning part
    const val WATCH_LATER_PLAYLIST = "WL"
    const val LIKED_PLAYLIST = "LL"

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
    private const val HOME_TV = "\"browseId\":\"default\""
    private const val TRENDING = "\"browseId\":\"FEtrending\",\"params\":\"6gQJRkVleHBsb3Jl\""
    private const val HYPE = "\"browseId\":\"FEhype_leaderboard\""
    private const val SUBSCRIPTIONS = "\"browseId\":\"FEsubscriptions\""
    private const val SPORTS = "\"browseId\":\"FEtopics_sports\""
    private const val LIVE = "\"browseId\":\"FEtopics_live\""
    private const val MY_VIDEOS = "\"browseId\":\"FEmy_videos\""
    private const val MOVIES = "\"browseId\":\"FEtopics_movies\""
    private const val MUSIC: String = "\"browseId\":\"FEtopics_music\""
    private const val LIKED_MUSIC = "\"browseId\":\"VLLM\""
    private const val LIKED_MUSIC_CONTINUATION = "4qmFsgIWEhRGRW11c2ljX2xpa2VkX3ZpZGVvcw%3D%3D"
    private const val NEW_MUSIC_VIDEOS = "\"browseId\":\"FEmusic_new_releases_videos\""
    private const val NEW_MUSIC_ALBUMS = "\"browseId\":\"FEmusic_new_releases_albums\""
    private const val MY_PLAYLISTS = "\"browseId\":\"FEplaylist_aggregation\""
    private const val MY_LIBRARY = "\"browseId\":\"FEmy_youtube\""
    private const val MY_LIBRARY2 = "\"browseId\":\"FElibrary\""
    private const val MY_WATCH_LATER = "\"browseId\":\"FEmy_youtube\",\"params\":\"cAc%3D\""
    private const val MY_HISTORY = "\"browseId\":\"FEhistory\""
    private const val GAMING = "\"browseId\":\"FEtopics_gaming\""
    private const val NEWS = "\"browseId\":\"FEtopics_news\""
    private const val REEL = "\"disablePlayerResponse\":true,\"inputType\":\"REEL_WATCH_INPUT_TYPE_SEEDLESS\",\"params\":\"CA8%3D\""
    private const val REEL_DETAILS = "\"disablePlayerResponse\":true,\"params\":\"%s\",\"playerRequest\":{\"videoId\":\"%s\"}"
    private const val REEL_CONTINUATION = "\"sequenceParams\":\"%s\""
    private const val CONTINUATION = "\"continuation\":\"%s\""

    fun getHomeQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, HOME_TV)
    }

    fun getTrendingQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, TRENDING)
    }

    fun getHypeQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, HYPE)
    }

    fun getChannelQuery(client: AppClient, channelId: String, params: String? = null): String {
        val channelTemplate =
            if (params != null) String.format(CHANNEL_FULL, channelId, params) else String.format(CHANNEL, channelId)
        return PostDataHelper.createQuery(client.browseTemplate, channelTemplate)
    }

    fun getChannelHomeQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_HOME, channelId))
    }

    fun getChannelVideosQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_VIDEOS, channelId))
    }

    fun getChannelShortsQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_SHORTS, channelId))
    }

    fun getChannelLiveQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_LIVE, channelId))
    }

    fun getChannelPlaylistsQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_PLAYLISTS, channelId))
    }

    fun getChannelReleasesQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_RELEASES, channelId))
    }

    fun getChannelCommunityQuery(client: AppClient, channelId: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_COMMUNITY, channelId))
    }

    fun getChannelSearchQuery(client: AppClient, channelId: String, query: String): String {
        return PostDataHelper.createQuery(client.browseTemplate, String.format(CHANNEL_SEARCH, channelId, query))
    }

    fun getKidsHomeQuery(): String {
        return PostDataHelper.createQueryKids(KIDS_HOME)
    }

    fun getKidsHomeQuery(params: String): String {
        return PostDataHelper.createQueryKids(String.format(KIDS_HOME_PARAMS, params))
    }

    fun getReelQuery(): String {
        return PostDataHelper.createQueryWeb(REEL)
    }

    fun getMusicQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MUSIC)
    }

    fun getLikedMusicQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, LIKED_MUSIC)
    }

    fun getLikedMusicContinuation(client: AppClient): String {
        return getContinuationQuery(client, LIKED_MUSIC_CONTINUATION)
    }

    fun getNewMusicAlbumsQuery(): String {
        return PostDataHelper.createQueryRemix(NEW_MUSIC_ALBUMS)
    }

    fun getNewMusicVideosQuery(): String {
        return PostDataHelper.createQueryRemix(NEW_MUSIC_VIDEOS)
    }

    fun getNewsQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, NEWS)
    }

    fun getGamingQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, GAMING)
    }

    fun getMyLibraryQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MY_LIBRARY2)
    }

    fun getMyHistoryQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MY_HISTORY)
    }

    fun getMyWatchLaterQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MY_WATCH_LATER)
    }

    fun getReelDetailsQuery(client: AppClient, videoId: String, params: String): String {
        val details = String.format(REEL_DETAILS, params, videoId)
        return PostDataHelper.createQuery(client.browseTemplate, details)
    }

    fun getReelContinuationQuery(client: AppClient, sequenceParams: String): String {
        val continuation = String.format(REEL_CONTINUATION, sequenceParams)
        return PostDataHelper.createQuery(client.browseTemplate, continuation)
    }

    fun getReelContinuation2Query(client: AppClient, nextPageKey: String): String {
        val continuation = String.format(CONTINUATION, nextPageKey)
        return PostDataHelper.createQuery(client.browseTemplate, continuation)
    }

    fun getSubscriptionsQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, SUBSCRIPTIONS)
    }

    fun getSportsQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, SPORTS)
    }

    fun getLiveQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, LIVE)
    }

    fun getMyVideosQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MY_VIDEOS)
    }

    fun getMoviesQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MOVIES)
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey [GridTab.getNextPageKey]
     * @return data param
     */
    fun getContinuationQuery(client: AppClient, nextPageKey: String): String {
        val continuation = String.format(CONTINUATION, nextPageKey)
        return PostDataHelper.createQuery(client.browseTemplate, continuation)
    }

    fun getMyPlaylistQuery(client: AppClient): String {
        return PostDataHelper.createQuery(client.browseTemplate, MY_PLAYLISTS)
    }
}