package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup;
import io.reactivex.Observable;

public interface CommentsService {
    Observable<CommentGroup> getCommentsObserve(String key);
    Observable<Void> toggleLikeObserve(String key);
    Observable<Void> toggleDislikeObserve(String key);
}
