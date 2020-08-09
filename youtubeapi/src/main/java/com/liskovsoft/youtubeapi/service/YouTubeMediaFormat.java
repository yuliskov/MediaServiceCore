package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;

import java.util.List;

/**
 * TODO: not implemented
 */
public class YouTubeMediaFormat implements MediaFormat {
    private String mIndex;
    private String mIndexRange;
    private String mUrl;
    private String mSignatureCipher;

    public static MediaFormat from(AdaptiveVideoFormat format) {
        YouTubeMediaFormat mediaFormat = new YouTubeMediaFormat();
        mediaFormat.mIndex = format.getIndex();
        mediaFormat.mIndexRange = format.getIndexRange().toString();

        // TODO: not implemented

        return mediaFormat;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String getSignatureCipher() {
        return mSignatureCipher;
    }

    @Override
    public void setSignatureCipher(String cipher) {
        mSignatureCipher = cipher;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public String getITag() {
        return null;
    }

    @Override
    public void setITag(String itag) {

    }

    @Override
    public String getClen() {
        return null;
    }

    @Override
    public void setClen(String clen) {

    }

    @Override
    public String getBitrate() {
        return null;
    }

    @Override
    public void setBitrate(String bitrate) {

    }

    @Override
    public String getProjectionType() {
        return null;
    }

    @Override
    public void setProjectionType(String projectionType) {

    }

    @Override
    public String getXtags() {
        return null;
    }

    @Override
    public void setXtags(String xtags) {

    }

    @Override
    public String getSize() {
        return null;
    }

    @Override
    public void setSize(String size) {

    }

    @Override
    public String getIndex() {
        return null;
    }

    @Override
    public void setIndex(String index) {

    }

    @Override
    public String getInit() {
        return null;
    }

    @Override
    public void setInit(String init) {

    }

    @Override
    public String getFps() {
        return null;
    }

    @Override
    public void setFps(String fps) {

    }

    @Override
    public String getLmt() {
        return null;
    }

    @Override
    public void setLmt(String lmt) {

    }

    @Override
    public String getQualityLabel() {
        return null;
    }

    @Override
    public void setQualityLabel(String qualityLabel) {

    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public boolean isOTF() {
        return false;
    }

    @Override
    public String getOtfInitUrl() {
        return null;
    }

    @Override
    public String getOtfTemplateUrl() {
        return null;
    }

    @Override
    public String getQuality() {
        return null;
    }

    @Override
    public void setQuality(String quality) {

    }

    @Override
    public boolean belongsToType(String type) {
        return false;
    }

    @Override
    public void setSignature(String signature) {

    }

    @Override
    public String getSignature() {
        return null;
    }

    @Override
    public void setAudioSamplingRate(String audioSamplingRate) {

    }

    @Override
    public String getAudioSamplingRate() {
        return null;
    }

    @Override
    public void setSourceURL(String sourceURL) {

    }

    @Override
    public String getSourceURL() {
        return null;
    }

    @Override
    public List<String> getSegmentUrlList() {
        return null;
    }

    @Override
    public void setSegmentUrlList(List<String> urls) {

    }

    @Override
    public List<String> getGlobalSegmentList() {
        return null;
    }

    @Override
    public void setGlobalSegmentList(List<String> segments) {

    }

    @Override
    public int compareTo(MediaFormat format) {
        return 0;
    }
}
