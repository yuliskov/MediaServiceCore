package com.liskovsoft.mediaserviceinterfaces.data;

public interface MediaSubtitle {
    String getBaseUrl();
    void setBaseUrl(String baseUrl);
    boolean isTranslatable();
    void setTranslatable(boolean translatable);
    String getLanguageCode();
    void setLanguageCode(String languageCode);
    String getVssId();
    void setVssId(String vssId);
    String getName();
    void setName(String name);
    String getMimeType();
    void setMimeType(String mimeType);
    String getCodecs();
    void setCodecs(String codecs);
    String getType();
    void setType(String type);
}
