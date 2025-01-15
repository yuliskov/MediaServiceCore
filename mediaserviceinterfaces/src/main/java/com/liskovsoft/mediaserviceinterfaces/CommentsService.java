package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup;
import io.reactivex.Observable;

public interface CommentsService {
    CommentGroup getComments(String key);
    Observable<CommentGroup> getCommentsObserve(String key);
}
