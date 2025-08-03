package com.liskovsoft.youtubeapi.app.potokennp.visitor

import com.liskovsoft.youtubeapi.app.potokennp.visitor.data.getVisitorData
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal object VisitorService {
    private val mApi = RetrofitHelper.create(VisitorApi::class.java)
    fun getVisitorData(): String? {
        val visitorResult = RetrofitHelper.get(mApi.getVisitorId(), true)

        return visitorResult?.getVisitorData()
    }
}