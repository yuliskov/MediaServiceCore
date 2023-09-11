package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal fun DislikesResult.getDislikeCount() = dislikes?.let { ServiceHelper.prettyCount(it) }
internal fun DislikesResult.getLikeCount() = likes?.let { ServiceHelper.prettyCount(it) }