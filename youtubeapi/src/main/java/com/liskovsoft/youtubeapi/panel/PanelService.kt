package com.liskovsoft.youtubeapi.panel

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.mediaserviceinterfaces.data.FeedbackEndpoint
import com.liskovsoft.youtubeapi.browse.v2.gen.getFeedbackToken
import com.liskovsoft.youtubeapi.next.v2.gen.getMenuItems

internal object PanelService {
    private val mPanelApi = RetrofitHelper.create(PanelApi::class.java)
    private var mCachedToken: CachedToken? = null

    private data class CachedToken(
        val panelId: String, val params: String, val tokens: List<String>?
    )

    fun getFeedbackTokens(endpoint: FeedbackEndpoint): List<String>? {
        return getFeedbackTokens(endpoint.panelId, endpoint.params)
    }

    private fun getFeedbackTokens(panelId: String, params: String): List<String>? {
        if (mCachedToken?.panelId == panelId && mCachedToken?.params == params) {
            return mCachedToken?.tokens
        }

        val panelResultWrapper = mPanelApi.getPanel(
            PanelApiHelper.getPanelQuery(panelId, params))

        val panelResult = RetrofitHelper.get(panelResultWrapper)

        val tokens = panelResult?.content?.getMenuItems()?.mapNotNull { it?.getFeedbackToken() }

        mCachedToken = CachedToken(panelId, params, tokens)

        return tokens
    }
}