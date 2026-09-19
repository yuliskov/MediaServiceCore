package com.liskovsoft.youtubeapi.menu

import com.google.gson.JsonObject
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.WrapperMediaItem

object ContextMenuService {
    @JvmStatic
    fun resolve(item: MediaItem): MediaItem {
        val source = item as? WrapperMediaItem ?: return item
        val endpoint = source.contextMenuPanel ?: return item
        val panelId = endpoint.identifier?.tag ?: return item
        val params = endpoint.globalConfiguration?.params ?: return item
        if (panelId.isEmpty() || params.isEmpty()) return item
        val fields = JsonObject().apply {
            addProperty("panelId", panelId)
            addProperty("params", params)
        }
        val query = PostDataHelper.createQuery(source.contextMenuClient.browseTemplate, fields.toString().removeSurrounding("{", "}"))
        val response = RetrofitHelper.create(ContextMenuApi::class.java).getPanel(query).execute()
        if (!response.isSuccessful) throw IllegalStateException("Unable to load context menu")
        val menu = MenuPanelParser.parse(response.body())
        // Never apply another video's commands to the selected video.
        if (item.videoId != null && menu.videoId != null && item.videoId != menu.videoId) return item
        return ResolvedContextMenuItem(item, menu)
    }
}

private class ResolvedContextMenuItem(private val source: MediaItem, private val menu: MenuPanelParser.Result): MediaItem by source {
    override fun getVideoId(): String? = source.videoId ?: menu.videoId
    override fun getParams(): String? = if (source.videoId == null && menu.videoId != null) null else source.params
    override fun getChannelId(): String? = menu.channelId ?: source.channelId
    override fun getFeedbackToken(): String? = menu.notInterestedToken ?: source.feedbackToken
    override fun getFeedbackToken2(): String? = menu.notRecommendChannelToken ?: source.feedbackToken2
}
