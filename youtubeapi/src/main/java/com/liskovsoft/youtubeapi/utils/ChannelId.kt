package com.liskovsoft.youtubeapi.utils

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp

internal class ChannelId {
    /**
     * <link rel="canonical" href="https://www.youtube.com/channel/UCsIEFXNO4bxh0hW3-_z2-0g">
     */
    @RegExp("<link rel=\"canonical\" href=\"https://www.youtube.com/channel/([\\w-]+)\">")
    var channelId: String? = null
        private set
}
