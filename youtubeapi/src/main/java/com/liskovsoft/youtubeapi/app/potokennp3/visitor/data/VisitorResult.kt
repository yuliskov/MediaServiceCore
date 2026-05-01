package com.liskovsoft.youtubeapi.app.potokennp3.visitor.data

internal data class VisitorResult(val responseContext: ResponseContext?) {
    data class ResponseContext(val visitorData: String?)
}
