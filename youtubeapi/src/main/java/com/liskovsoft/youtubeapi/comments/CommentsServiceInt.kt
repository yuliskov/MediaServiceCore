package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup
import com.liskovsoft.youtubeapi.comments.impl.CommentGroupImpl
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

class CommentsServiceInt private constructor() {
    private val mApi = RetrofitHelper.withGson(CommentsApi::class.java)

    fun getComments(key: String?): CommentGroup? {
        val commentsResult =
            RetrofitHelper.get(mApi.getComments(CommentsApiParams.getCommentsQuery(key)))
        return commentsResult?.let { CommentGroupImpl(it) }
    }

    companion object {
        private val TAG = CommentsServiceInt::class.simpleName
        private var sInstance: CommentsServiceInt? = null
        @JvmStatic
        fun instance(): CommentsServiceInt? {
            if (sInstance == null) {
                sInstance = CommentsServiceInt()
            }
            return sInstance
        }

        @JvmStatic
        fun unhold() {
            sInstance = null
        }
    }
}