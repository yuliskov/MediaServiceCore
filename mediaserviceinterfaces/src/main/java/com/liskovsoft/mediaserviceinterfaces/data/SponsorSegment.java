package com.liskovsoft.mediaserviceinterfaces.data;

public interface SponsorSegment {
    String CATEGORY_SPONSOR = "sponsor";
    String CATEGORY_INTRO = "intro";
    String CATEGORY_OUTRO = "outro";
    String CATEGORY_INTERACTION = "interaction";
    String CATEGORY_SELF_PROMO = "selfpromo";
    String CATEGORY_MUSIC_OFF_TOPIC = "music_offtopic";

    long getStartMs();
    long getEndMs();
    String getCategory();
}
