package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup
import com.liskovsoft.youtubeapi.comments.impl.CommentGroupImpl
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.YouTubeSignInService

class CommentsServiceInt private constructor() {
    private val mApi = RetrofitHelper.withGson(CommentsApi::class.java)
    private val mSignInService = YouTubeSignInService.instance()

    fun getComments(key: String?): CommentGroup? {
        val commentsResult =
            RetrofitHelper.get(mApi.getComments(CommentsApiParams.getCommentsQuery(key), mSignInService.authorizationHeader))
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