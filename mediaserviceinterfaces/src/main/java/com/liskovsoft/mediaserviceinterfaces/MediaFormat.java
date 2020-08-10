package com.liskovsoft.mediaserviceinterfaces;

import java.util.List;

public interface MediaFormat extends Comparable<MediaFormat> {
    // Common
    String getUrl();
    void setUrl(String url);
    String getSignatureCipher();
    void setSignatureCipher(String s);
    String getType();
    void setType(String type);
    String getITag();
    void setITag(String itag);

    // DASH
    String getClen();
    void setClen(String clen);
    String getBitrate();
    void setBitrate(String bitrate);
    String getProjectionType();
    void setProjectionType(String projectionType);
    String getXtags();
    void setXtags(String xtags);
    String getSize();
    void setSize(String size);
    String getIndex();
    void setIndex(String index);
    String getInit();
    void setInit(String init);
    String getFps();
    void setFps(String fps);
    String getLmt();
    void setLmt(String lmt);
    String getQualityLabel();
    void setQualityLabel(String qualityLabel);
    String getFormat();
    boolean isOtf();
    String getOtfInitUrl();
    String getOtfTemplateUrl();

    // Other/Regular
    String getQuality();
    void setQuality(String quality);
    boolean belongsToType(String type);
    void setSignature(String signature);
    String getSignature();
    void setAudioSamplingRate(String audioSamplingRate);
    String getAudioSamplingRate();
    void setSourceUrl(String sourceURL);
    String getSourceUrl();
    List<String> getSegmentUrlList();
    void setSegmentUrlList(List<String> urls);
    List<String> getGlobalSegmentList();
    void setGlobalSegmentList(List<String> segments);
}
