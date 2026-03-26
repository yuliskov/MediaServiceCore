package com.liskovsoft.googlecommon.common.constants

import com.liskovsoft.googlecommon.common.constants.data.ConstantsResult
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal object ConstantsService {
    private val mConstantsApi = RetrofitHelper.create(ConstantsApi::class.java)
    private var mConstantsResult: ConstantsResult? = null

    @JvmStatic
    fun getConstants(): ConstantsResult? {
        if (mConstantsResult == null) {
            mConstantsResult = getConstantsInt()
        }

        return mConstantsResult
    }

    private fun getConstantsInt(): ConstantsResult? {
        return RetrofitHelper.get(mConstantsApi.getConstants())
    }
}