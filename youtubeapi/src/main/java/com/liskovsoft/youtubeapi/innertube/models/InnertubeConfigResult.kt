package com.liskovsoft.youtubeapi.innertube.models

import com.liskovsoft.youtubeapi.common.models.gen.ResponseContext

internal data class InnertubeConfigResult(
    val responseContext: ResponseContext?,
    val configData: String?
)
