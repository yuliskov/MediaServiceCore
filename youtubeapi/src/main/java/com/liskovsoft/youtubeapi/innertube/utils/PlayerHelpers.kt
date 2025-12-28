package com.liskovsoft.youtubeapi.innertube.utils

import com.liskovsoft.youtubeapi.innertube.models.PlayerResult

internal fun PlayerResult.getVideoPlaybackUstreamerConfig() = playerConfig?.mediaCommonConfig?.mediaUstreamerRequestConfig?.videoPlaybackUstreamerConfig
internal fun PlayerResult.getServerAbrStreamingUrl() = streamingData?.serverAbrStreamingUrl