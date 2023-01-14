package com.liskovsoft.mediaserviceinterfaces.data;

/**
 * <a href="https://wiki.sponsor.ajay.app/w/Segment_Categories">Segment specs</a><br/>
 * <a href="https://wiki.sponsor.ajay.app/w/API_Docs">Segment API</a><br/>
 */
public interface SponsorSegment {
    String CATEGORY_SPONSOR = "sponsor";
    String CATEGORY_INTRO = "intro";
    String CATEGORY_OUTRO = "outro";
    String CATEGORY_INTERACTION = "interaction";
    String CATEGORY_SELF_PROMO = "selfpromo";
    String CATEGORY_MUSIC_OFF_TOPIC = "music_offtopic";
    String CATEGORY_PREVIEW_RECAP = "preview";
    String CATEGORY_POI_HIGHLIGHT = "poi_highlight";
    String CATEGORY_FILLER = "filler";
    String ACTION_SKIP = "skip";
    String ACTION_MUTE = "mute";

    long getStartMs();
    long getEndMs();
    String getCategory();
    String getAction();
}
