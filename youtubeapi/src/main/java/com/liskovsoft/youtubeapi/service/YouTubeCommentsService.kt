package com.liskovsoft.youtubeapi.service

import com.liskovsoft.mediaserviceinterfaces.yt.CommentsService
import com.liskovsoft.mediaserviceinterfaces.yt.data.CommentGroup
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.comments.CommentsServiceInt
import io.reactivex.Observable

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

    override fun getCommentsObserve(key: String?): Observable<CommentGroup> {
        return RxHelper.fromNullable { getComments(key) }
    }
}