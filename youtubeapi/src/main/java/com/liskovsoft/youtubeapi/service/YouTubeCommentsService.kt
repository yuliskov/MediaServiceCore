package com.liskovsoft.youtubeapi.service

import com.liskovsoft.mediaserviceinterfaces.CommentsService
import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup
import com.liskovsoft.youtubeapi.comments.CommentsServiceInt

class YouTubeCommentsService private constructor(): CommentsService {
    private val commentsServiceInt = CommentsServiceInt.instance()

    override fun getComments(key: String?): CommentGroup? {
        return commentsServiceInt?.getComments(key)
    }

    companion object {
        private val TAG = YouTubeCommentsService::class.simpleName
        private var sInstance: YouTubeCommentsService? = null

        @JvmStatic
        fun instance(): YouTubeCommentsService? {
            if (sInstance == null) {
                sInstance = YouTubeCommentsService()
            }
            return sInstance
        }

        @JvmStatic
        fun unhold() {
            sInstance = null
        }
    }
}