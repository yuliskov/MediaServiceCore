package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup;

public interface CommentsService {
    CommentGroup getComments(String key);
}
