package com.liskovsoft.youtubeapi.common.models.gen

internal data class ContextMenuPanelEndpoint(val identifier: Identifier?, val globalConfiguration: Configuration?) {
    data class Identifier(val tag: String?)
    data class Configuration(val params: String?)
}
