package com.liskovsoft.mediaserviceinterfaces.data;

import java.util.List;

public interface MediaFormat extends Comparable<MediaFormat> {
    int FORMAT_TYPE_DASH = 0;
    int FORMAT_TYPE_REGULAR = 1;
    // Common
    String getUrl();
    void setUrl(String url);
    String getSignatureCipher();
    void setSignatureCipher(String s);
    String getMimeType();
    void setMimeType(String mimeType);
    String getITag();
    void setITag(String itag);
    boolean isDrc();
    void setIsDrc(boolean isDrc);

    // DASH
    String getClen();
    void setClen(String clen);
    String getBitrate();
    void setBitrate(String bitrate);
    String getProjectionType();
    void setProjectionType(String projectionType);
    String getXtags();
    void setXtags(String xtags);
    int getWidth();
    void setWidth(int width);
    int getHeight();
    void setHeight(int height);
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
    String getLanguage();
    // DASH LIVE
    String getTargetDurationSec();
    String getLastModified();
    String getMaxDvrDurationSec();

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
