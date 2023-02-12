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
                       val sectionListRenderer: SectionListRenderer?
                   ) {
                       data class SectionListRenderer(
                           val contents: List<Section?>?
                       ) {
                           data class Section(
                               val itemSectionRenderer: ItemSectionRenderer?
                           ) {
                               data class ItemSectionRenderer(
                                   val contents: List<Shelf?>?
                               ) {
                                   data class Shelf(
                                       val shelfRenderer: ShelfRenderer?
                                   ) {
                                       data class ShelfRenderer(
                                           val content: Content?
                                       ) {
                                           data class Content(
                                               val gridRenderer: GridRenderer?
                                           ) {
                                               data class GridRenderer(
                                                   val items: List<ItemWrapper?>?
                                               )
                                           }
                                       }
                                   }
                               }
                           }
                       }
                   }
                }
            }
        }
    }
}
