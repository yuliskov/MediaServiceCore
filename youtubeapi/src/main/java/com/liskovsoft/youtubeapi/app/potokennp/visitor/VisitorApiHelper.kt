package com.liskovsoft.youtubeapi.app.potokennp.visitor

import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.helpers.PostDataBuilder
import com.liskovsoft.youtubeapi.common.helpers.PostDataType
import com.liskovsoft.youtubeapi.common.locale.LocaleManager

internal object VisitorApiHelper {
    fun getVisitorQuery(): String {
        val localeManager = LocaleManager.instance()
        return PostDataBuilder(AppClient.WEB)
            .setType(PostDataType.Default)
            .setLanguage(localeManager.language)
            .setCountry(localeManager.country)
            .setUtcOffsetMinutes(localeManager.utcOffsetMinutes)
            .build()
    }
}