package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

/**
 * Based on:
 *
 * browse_subs_chrome_12.02.2023.json
 */
data class BrowseResult(
    val contents: Contents?
) {
    data class Contents(
        val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?
    ) {
        data class TwoColumnBrowseResultsRenderer(
            val tabs: List<Tab?>?
        ) {
            data class Tab(
             val tabRenderer: TabRenderer?
            ) {
                data class TabRenderer(
                    val content: Content?
                ) {
                   data class Content(
                       val sectionListRenderer: SectionListRenderer?,
                       val richGridRenderer: RichGridRenderer?
                   ) {
                       data class SectionListRenderer(
                           val contents: List<Section?>?
                       )
                       data class RichGridRenderer(
                           val contents: List<RichContent?>?
                       )
                   }
                }
            }
        }
    }
}
