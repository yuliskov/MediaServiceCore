package com.liskovsoft.mediaserviceinterfaces.data;

import androidx.annotation.Nullable;

public interface CommentItem {
    @Nullable
    String getId();
    @Nullable
    String getMessage();
    @Nullable
    String getAuthorName();
    @Nullable
    String getAuthorPhoto();
    @Nullable
    String getNestedCommentsKey();
    boolean isLiked();
    @Nullable
    String getLikesCount();
}
