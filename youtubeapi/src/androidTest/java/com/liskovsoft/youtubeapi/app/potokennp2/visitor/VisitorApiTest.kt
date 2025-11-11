package com.liskovsoft.youtubeapi.app.potokennp2.visitor

import com.liskovsoft.youtubeapi.app.potokennp2.visitor.data.getVisitorData
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VisitorApiTest {
    private lateinit var mApi: VisitorApi

    @Before
    fun setUp() {
        mApi = RetrofitHelper.create(VisitorApi::class.java)
    }

    @Test
    fun testThatVisitorDataNotNull() {
        val visitorResult = RetrofitHelper.get(mApi.getVisitorId(VisitorApiHelper.getVisitorQuery()), false)

        Assert.assertNotNull("Visitor not empty", visitorResult?.getVisitorData())
    }
}