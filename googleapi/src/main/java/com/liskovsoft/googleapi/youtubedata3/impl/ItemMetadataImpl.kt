package com.liskovsoft.googleapi.youtubedata3.impl

import com.liskovsoft.googleapi.youtubedata3.data.SnippetWrapper

internal class ItemMetadataImpl(snippetWrapper: SnippetWrapper): ItemMetadata {
     override val title: String by lazy { snippetWrapper.id!! }
}