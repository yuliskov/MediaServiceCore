package com.liskovsoft.mediaserviceinterfaces.oauth;

public interface Account {
    int getId();
    String getName();
    String getEmail();
    String getAvatarImageUrl();
    boolean isSelected();
    boolean isEmpty();

    /** Stable, non-secret profile identity, or null when the provider cannot supply one. */
    default String getProfileId() {
        return null;
    }
}
