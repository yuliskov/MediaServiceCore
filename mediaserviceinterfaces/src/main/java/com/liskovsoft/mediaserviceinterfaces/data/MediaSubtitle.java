package com.liskovsoft.mediaserviceinterfaces.data;

public interface MediaSubtitle {
    String getBaseUrl();
    boolean isTranslatable();
    String getLanguageCode();
    String getVssId();
    String getName();
    String getMimeType();
    String getCodecs();
    String getType();
}
