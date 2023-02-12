package com.liskovsoft.youtubeapi.actions.models

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp

class HideVideoTokenResult {
    @RegExp("Sam Smith at The BRIT Awards 2023\\. #samsmith.*?\"feedbackToken\":\"(.*?)\"")
    var feedbackToken: String? = null
}
