package com.liskovsoft.youtubeapi.panel

import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.helpers.QueryBuilder

internal object PanelApiHelper {
    fun getPanelQuery(panelId: String, params: String): String {
        return QueryBuilder(AppClient.TV)
            .setPanelId(panelId)
            .setParams(params)
            .build()
    }
}