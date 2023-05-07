package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

fun DislikesResult.getDislikeCount() = dislikes?.let { ServiceHelper.prettyCount(it) }
fun DislikesResult.getLikeCount() = likes?.let { ServiceHelper.prettyCount(it) }