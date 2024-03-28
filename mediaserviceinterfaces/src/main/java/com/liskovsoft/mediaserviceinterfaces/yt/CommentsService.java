package com.liskovsoft.mediaserviceinterfaces.yt;

import com.liskovsoft.mediaserviceinterfaces.yt.data.CommentGroup;
import io.reactivex.Observable;

public interface CommentsService {
    CommentGroup getComments(String key);
    Observable<CommentGroup> getCommentsObserve(String key);
}
