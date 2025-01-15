package com.liskovsoft.mediaserviceinterfaces.oauth.data;

public interface Account {
    int getId();
    String getName();
    String getEmail();
    String getAvatarImageUrl();
    boolean isSelected();
    boolean isEmpty();
}
