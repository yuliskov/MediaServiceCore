package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.googlecommon.common.helpers.ServiceHelper

internal fun DislikesResult.getDislikeCount() = dislikes?.let { if (it > 0) ServiceHelper.prettyCount(it) else null }
internal fun DislikesResult.getLikeCount() = likes?.let { if (it > 0) ServiceHelper.prettyCount(it) else null }