package com.liskovsoft.mediaserviceinterfaces.data;

import androidx.annotation.Nullable;

import java.util.List;

public interface CommentGroup {
    @Nullable
    List<CommentItem> getComments();
    @Nullable
    String getNextCommentsKey();
}
