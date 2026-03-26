package com.liskovsoft.googlecommon.common.constants

import com.liskovsoft.googlecommon.common.constants.data.ConstantsResult
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal object ConstantsService {
    private val mConstantsApi = RetrofitHelper.create(ConstantsApi::class.java)

    @JvmStatic
    val constants: ConstantsResult? by lazy { RetrofitHelper.get(mConstantsApi.getConstants()) }
}