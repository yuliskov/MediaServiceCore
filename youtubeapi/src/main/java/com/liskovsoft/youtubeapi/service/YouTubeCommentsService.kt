package com.liskovsoft.youtubeapi.service

import com.liskovsoft.mediaserviceinterfaces.CommentsService
import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.comments.CommentsServiceInt
import io.reactivex.Observable

internal object YouTubeCommentsService: CommentsService {
    override fun getComments(key: String?): CommentGroup? {
        return key?.let { CommentsServiceInt.getComments(key) }
    }

    override fun getCommentsObserve(key: String?): Observable<CommentGroup> {
        return RxHelper.fromNullable { getComments(key) }
    }
}