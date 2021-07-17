package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import android.util.Xml;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle;
import com.liskovsoft.sharedutils.helpers.FileHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.formatbuilders.utils.ITagUtils;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.YouTubeOtfSegmentParser.OtfSegment;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Demos: https://github.com/Dash-Industry-Forum/dash-live-source-simulator/wiki/Test-URLs
 */
public class YouTubeMPDBuilder implements MPDBuilder {
    private static final String MIME_WEBM_AUDIO = "audio/webm";
    private static final String MIME_WEBM_VIDEO = "video/webm";
    private static final String MIME_MP4_AUDIO = "audio/mp4";
    private static final String MIME_MP4_VIDEO = "video/mp4";
    private static final String NULL_INDEX_RANGE = "0-0";
    private static final String NULL_CONTENT_LENGTH = "0";
    private static final String TAG = YouTubeMPDBuilder.class.getSimpleName();
    private static final Pattern CODECS_PATTERN = Pattern.compile(".*codecs=\\\"(.*)\\\"");
    private final MediaItemFormatInfo mInfo;
    private XmlSerializer mXmlSerializer;
    private StringWriter mWriter;
    private int mId;
    private final Set<MediaFormat> mMP4Audios;
    private final Set<MediaFormat> mMP4Videos;
    private final Set<MediaFormat> mWEBMAudios;
    private final Set<MediaFormat> mWEBMVideos;
    private final List<MediaSubtitle> mSubs;
    private final YouTubeOtfSegmentParser mSegmentParser;
    private String mLimitVideoCodec;
    private String mLimitAudioCodec;

    public YouTubeMPDBuilder(MediaItemFormatInfo info) {
        mInfo = info;
        MediaFormatComparator comp = new MediaFormatComparator();
        mMP4Audios = new TreeSet<>(comp);
        mMP4Videos = new TreeSet<>(comp);
        mWEBMAudios = new TreeSet<>(comp);
        mWEBMVideos = new TreeSet<>(comp);
        mSubs = new ArrayList<>();
        mSegmentParser = new YouTubeOtfSegmentParser(true);

        initXmlSerializer();
    }

    public static MPDBuilder from(MediaItemFormatInfo formatInfo) {
        MPDBuilder builder = new YouTubeMPDBuilder(formatInfo);

        if (formatInfo.containsDashInfo()) {
            for (MediaFormat format : formatInfo.getAdaptiveFormats()) {
                builder.append(format);
            }

            if (formatInfo.getSubtitles() != null) {
                builder.append(formatInfo.getSubtitles());
            }
        }

        return builder;
    }

    private void initXmlSerializer() {
        mXmlSerializer = Xml.newSerializer();
        mWriter = new StringWriter();

        setOutput(mXmlSerializer, mWriter);

        startDocument(mXmlSerializer);
        mXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
    }

    private void writePrologue() {
        String duration = mInfo.getLengthSeconds();
        String durationParam = String.format("PT%sS", duration);

        startTag("", "MPD");
        attribute("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        attribute("", "xmlns", "urn:mpeg:DASH:schema:MPD:2011");
        attribute("", "xmlns:yt", "http://youtube.com/yt/2012/10/10");
        attribute("", "xsi:schemaLocation", "urn:mpeg:DASH:schema:MPD:2011 DASH-MPD.xsd");
        attribute("", "minBufferTime", "PT1.500S");

        if (isLive()) {
            attribute("", "profiles", "urn:mpeg:dash:profile:isoff-main:2011");
            attribute("", "type", "dynamic");

            attribute("", "minBufferTime", "PT1.500S");
            attribute("", "timeShiftBufferDepth", "PT14400.000S");
            attribute("", "minimumUpdatePeriod", "PT5.000S");
            // availabilityStartTime="2019-01-06T17:04:49"
        } else {
            attribute("", "profiles", "urn:mpeg:dash:profile:isoff-on-demand:2011");
            attribute("", "type", "static");
            attribute("", "mediaPresentationDuration", durationParam);
        }


        startTag("", "Period");

        if (isLive()) {
            // yt:segmentIngestTime="2019-01-06T17:55:24.836"
            attribute("", "start", "PT3050.000S");
        } else {
            attribute("", "duration", durationParam);
        }
    }

    private void writeEpilogue() {
        endTag("", "Period");
        endTag("", "MPD");
        endDocument();
    }

    private void writeMediaTags() {
        if (isLive()) {
            writeLiveHeaderSegmentList();
        }

        // MXPlayer fix: write high quality formats first
        writeMediaTagsForGroup(mWEBMVideos);
        writeMediaTagsForGroup(mWEBMAudios);
        writeMediaTagsForGroup(mMP4Videos);
        writeMediaTagsForGroup(mMP4Audios);
        writeMediaTagsForGroup(mSubs);
    }

    private void writeLiveHeaderSegmentList() {
        startTag("", "SegmentList");

        attribute("", "presentationTimeOffset", "3050000");
        attribute("", "startNumber", "610");
        attribute("", "timescale", "1000");

        startTag("", "SegmentTimeline");

        for (int i = 0; i < 3; i++) {
            startTag("", "S");

            attribute("", "d", "5000");

            endTag("", "S");
        }

        endTag("", "SegmentTimeline");

        endTag("", "SegmentList");
    }

    private void writeMediaTagsForGroup(List<MediaSubtitle> subs) {
        if (subs.size() == 0) {
            return;
        }

        for (MediaSubtitle sub : subs) {
            writeMediaListPrologue(sub);

            writeMediaFormatTag(sub);

            writeMediaListEpilogue();
        }
    }

    private void writeMediaTagsForGroup(Set<MediaFormat> items) {
        if (items.size() == 0) {
            return;
        }

        List<MediaFormat> filtered = filterOtfItems(items);

        if (filtered.size() == 0) {
            return;
        }

        MediaFormat firstItem = null;
        for (MediaFormat item : filtered) {
            firstItem = item;
            break;
        }

        writeMediaListPrologue(String.valueOf(mId++), extractMimeType(firstItem));

        // Representation
        for (MediaFormat item : filtered) {
            if (mLimitVideoCodec != null && isVideo(item) && !item.getMimeType().contains(mLimitVideoCodec)) {
                continue;
            }

            if (mLimitAudioCodec != null && isAudio(item) && !item.getMimeType().contains(mLimitAudioCodec)) {
                continue;
            }

            if (item.getGlobalSegmentList() != null) {
                writeGlobalSegmentList(item);
                continue;
            }

            writeMediaFormatTag(item);
        }

        writeMediaListEpilogue();
    }

    private void writeGlobalSegmentList(MediaFormat format) {
        startTag("", "SegmentList");

        attribute("", "startNumber", "0");
        attribute("", "timescale", "1000");

        startTag("", "SegmentTimeline");

        // SegmentURL tag
        for (String segment : format.getGlobalSegmentList()) {
            startTag("", "S");
            attribute("", "d", segment);
            endTag("", "S");
        }

        endTag("", "SegmentTimeline");

        endTag("", "SegmentList");
    }

    private XmlSerializer attribute(String namespace, String name, String value) {
        if (value == null) {
            return mXmlSerializer;
        }
        try {
            return mXmlSerializer.attribute(namespace, name, value);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private XmlSerializer startTag(String namespace, String name) {
        try {
            return mXmlSerializer.startTag(namespace, name);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setOutput(XmlSerializer xmlSerializer, StringWriter writer) {
        try {
            xmlSerializer.setOutput(writer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void startDocument(XmlSerializer xmlSerializer) {
        try {
            xmlSerializer.startDocument("UTF-8", true);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void endDocument() {
        try {
            mXmlSerializer.endDocument();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void endTag(String namespace, String name) {
        try {
            mXmlSerializer.endTag(namespace, name);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void writeMediaListPrologue(String id, String mimeType) {
        startTag("", "AdaptationSet");
        attribute("", "id", id);
        attribute("", "mimeType", mimeType);
        attribute("", "subsegmentAlignment", "true");

        startTag("", "Role");
        attribute("", "schemeIdUri", "urn:mpeg:DASH:role:2011");
        attribute("", "value", "main");
        endTag("", "Role");
    }

    private void writeMediaListPrologue(MediaSubtitle sub) {
        String id = String.valueOf(mId++);

        startTag("", "AdaptationSet");
        attribute("", "id", id);
        attribute("", "mimeType", sub.getMimeType());
        attribute("", "lang", sub.getName() == null ? sub.getLanguageCode() : sub.getName());

        startTag("", "Role");
        attribute("", "schemeIdUri", "urn:mpeg:DASH:role:2011");
        attribute("", "value", "subtitle");
        endTag("", "Role");
    }

    private void writeMediaListEpilogue() {
        endTag("", "AdaptationSet");
    }

    @Override
    public void append(MediaFormat mediaItem) {
        if (!MediaFormatUtils.checkMediaUrl(mediaItem)) {
            Log.e(TAG, "Media item doesn't contain required url field!");
            return;
        }

        // NOTE: FORMAT_STREAM_TYPE_OTF not supported
        if (!MediaFormatUtils.isDash(mediaItem)) {
            return;
        }

        //fixOTF(mediaItem);

        Set<MediaFormat> placeholder = null;
        String mimeType = extractMimeType(mediaItem);
        if (mimeType != null) {
            switch (mimeType) {
                case MIME_WEBM_AUDIO:
                    placeholder = mWEBMAudios;
                    break;
                case MIME_WEBM_VIDEO:
                    placeholder = mWEBMVideos;
                    break;
                case MIME_MP4_AUDIO:
                    placeholder = mMP4Audios;
                    break;
                case MIME_MP4_VIDEO:
                    placeholder = mMP4Videos;
                    break;
            }
        }

        if (placeholder != null) {
            placeholder.add(mediaItem); // NOTE: reverse order
        }
    }

    @Override
    public void append(List<MediaSubtitle> subs) {
        mSubs.addAll(subs);
    }

    @Override
    public void append(MediaSubtitle sub) {
        mSubs.add(sub);
    }

    private String extractMimeType(MediaFormat item) {
        if (item.getGlobalSegmentList() != null) {
            return item.getMimeType();
        }

        String codecs = extractCodecs(item);

        if (codecs.startsWith("vorbis") ||
                codecs.startsWith("opus")) {
            return MIME_WEBM_AUDIO;
        }

        if (codecs.startsWith("vp9")) {
            return MIME_WEBM_VIDEO;
        }

        if (codecs.startsWith("mp4a")) {
            return MIME_MP4_AUDIO;
        }

        if (codecs.startsWith("avc") ||
                codecs.startsWith("av01")) {
            return MIME_MP4_VIDEO;
        }

        return null;
    }

    private void writeMediaFormatTag(MediaFormat format) {
        startTag("", "Representation");

        attribute("", "id", format.getITag());
        attribute("", "codecs", extractCodecs(format));
        attribute("", "startWithSAP", "1");
        attribute("", "bandwidth", format.getBitrate());

        if (isVideo(format)) {
            // video attrs
            attribute("", "width", MediaFormatUtils.getWidth(format));
            attribute("", "height", MediaFormatUtils.getHeight(format));
            attribute("", "maxPlayoutRate", "1");
            attribute("", "frameRate", format.getFps());
        } else {
            // audio attrs
            attribute("", "audioSamplingRate", ITagUtils.getAudioRateByTag(format.getITag()));
        }

        if (format.isOtf()) {
            writeOtfSegmentTemplate(format);
        } else {
            startTag("", "BaseURL");

            if (format.getClen() != null && !format.getClen().equals(NULL_CONTENT_LENGTH)) {
                attribute("", "yt:contentLength", format.getClen());
            }

            text(format.getUrl());

            endTag("", "BaseURL");
        }

        // SegmentList tag
        if (isLive()) {
            writeLiveMediaSegmentList();
        } else if (format.getSegmentUrlList() != null) {
            writeSegmentList(format);
        } else if (format.getIndex() != null &&
                !format.getIndex().equals(NULL_INDEX_RANGE)) { // json format fix: index is null
            writeSegmentBase(format);
        }

        endTag("", "Representation");
    }

    private void writeSegmentBase(MediaFormat item) {
        // SegmentBase
        startTag("", "SegmentBase");

        if (item.getIndex() != null) {
            attribute("", "indexRange", item.getIndex());
            attribute("", "indexRangeExact", "true");
        }

        startTag("", "Initialization");

        attribute("", "range", item.getInit());

        endTag("", "Initialization");

        endTag("", "SegmentBase");
    }

    private void writeSegmentList(MediaFormat item) {
        startTag("", "SegmentList");

        // Initialization tag
        if (item.getSourceUrl() != null) {
            startTag("", "Initialization");
            attribute("", "sourceURL", item.getSourceUrl());
            endTag("", "Initialization");
        }

        // SegmentURL tag
        for (String url : item.getSegmentUrlList()) {
            startTag("", "SegmentURL");
            attribute("", "media", url);
            endTag("", "SegmentURL");
        }

        endTag("", "SegmentList");
    }

    private void writeLiveMediaSegmentList() {
        startTag("", "SegmentList");

        for (String mediaDesc : new String[]{
                "sq/610/lmt/1546797364563137",
                "sq/611/lmt/1546797365000899",
                "sq/612/lmt/1546797369574434"}) {
            startTag("", "SegmentURL");

            attribute("", "media", mediaDesc);

            endTag("", "SegmentURL");
        }

        endTag("", "SegmentList");
    }

    private void writeMediaFormatTag(MediaSubtitle sub) {
        String bandwidth = "268";

        startTag("", "Representation");

        attribute("", "id", String.valueOf(mId));

        attribute("", "bandwidth", bandwidth);

        attribute("", "codecs", sub.getCodecs());

        startTag("", "BaseURL");

        text(sub.getBaseUrl());

        endTag("", "BaseURL");

        endTag("", "Representation");
    }

    private boolean isVideo(MediaFormat item) {
        return item.getSize() != null;
    }

    private boolean isAudio(MediaFormat item) {
        return item.getMimeType() != null && item.getMimeType().contains("audio");
    }

    private XmlSerializer text(String url) {
        try {
            return mXmlSerializer.text(url);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String extractCodecs(MediaFormat item) {
        // input example: video/mp4;+codecs="avc1.640033"
        Matcher matcher = CODECS_PATTERN.matcher(item.getMimeType());
        matcher.find();
        return matcher.group(1);
    }

    /**
     * Extracts time from video url (if present).
     * Url examples:
     * <br/>
     * "http://example.com?dur=544.99&key=val&key2=val2"
     * <br/>
     * "http://example.com/dur/544.99/key/val/key2/val2"
     *
     * @return duration as string
     */
    private String extractDurationFromTrack() {
        String url = null;
        for (MediaFormat item : mMP4Videos) {
            url = item.getUrl();
            break; // get first item
        }
        String res = Helpers.runMultiMatcher(url, "dur=([^&]*)", "/dur/([^/]*)");
        return res;
    }

    /**
     * Ensures that required fields are set. If no, initialize them or throw an exception.
     * <br/>
     * Required fields are:
     * <br/>
     * {@link MediaItemFormatInfo#getLengthSeconds() MediaItemDetails#getLengthSeconds()}
     */
    private boolean ensureRequiredFieldsAreSet() {
        return ensureLengthIsSet();
    }

    /**
     * MPD file is not valid without duration
     */
    private boolean ensureLengthIsSet() {
        if (mInfo == null) {
            //throw new IllegalStateException("MediaItemDetails not initialized");
            Log.e(TAG, "MediaItemDetails not initialized");
            return false;
        }

        if (mInfo.getLengthSeconds() != null) {
            return true;
        }

        // try to get duration from video url
        String len = extractDurationFromTrack();

        if (len == null) {
            //throw new IllegalStateException("Videos in the list doesn't have a duration. Content: " + mMP4Videos);
            Log.e(TAG, "Videos in the list doesn't have a duration. Content: " + mMP4Videos);
            return false;
        }

        mInfo.setLengthSeconds(len);
        return true;
    }

    @Override
    public InputStream build() {
        if (!mInfo.containsDashInfo()) {
            return null;
        }

        if (ensureRequiredFieldsAreSet()) {
            writePrologue();

            writeMediaTags();

            writeEpilogue();

            return FileHelpers.toStream(mWriter.toString());
        }

        return null;
    }

    @Override
    public boolean isEmpty() {
        return (mMP4Videos.size() == 0 && mWEBMVideos.size() == 0
                && mMP4Audios.size() == 0 && mWEBMAudios.size() == 0) || !ensureRequiredFieldsAreSet();
    }

    private boolean isLive() {
        for (MediaFormat item : mMP4Videos) {
            return isLiveMedia(item);
        }

        for (MediaFormat item : mWEBMVideos) {
            return isLiveMedia(item);
        }

        return false;
    }

    private boolean isLiveMedia(MediaFormat item) {
        boolean isLive =
                item.getUrl().contains("live=1") ||
                        item.getUrl().contains("yt_live_broadcast");

        return isLive;
    }

    private void fixOTF(MediaFormat mediaItem) {
        if (mediaItem.isOtf()) {
            if (mediaItem.getUrl() != null) {
                // exo: fix 404 code
                mediaItem.setUrl(mediaItem.getUrl() + "&sq=7");
                //mediaItem.setInit("0-740");
                //mediaItem.setIndex("741-2296");
                //mediaItem.setClen("105557711");
            }
        }
    }

    /**
     * TODO: improve segment calculation
     */
    private void writeOtfSegmentTemplateOld(MediaFormat item) {
        //<SegmentTemplate timescale="90000" media="&sq=$Number$" startNumber="0">
        //  <SegmentTimeline>
        //    <S t="0" d="180000" r="394"/>
        //    <S t="71100000" d="46800" r="0"/>
        //  </SegmentTimeline>
        //</SegmentTemplate>

        startTag("", "SegmentTemplate");

        attribute("", "timescale", "1000"); // units per second
        attribute("", "duration", "5100"); // segment duration (units)
        attribute("", "media", item.getUrl() + "&sq=$Number$");
        attribute("", "initialization", item.getUrl() + "&sq=0"); // segments list and durations (required for stream switch!!!)
        attribute("", "startNumber", "1");

        endTag("", "SegmentTemplate");
    }

    private void writeOtfSegmentTemplate(MediaFormat item) {
        //<SegmentTemplate timescale="90000" media="&sq=$Number$" startNumber="0">
        //  <SegmentTimeline>
        //    <S t="0" d="180000" r="394"/>
        //    <S t="71100000" d="46800" r="0"/>
        //  </SegmentTimeline>
        //</SegmentTemplate>

        List<OtfSegment> segments = mSegmentParser.parse(item.getOtfInitUrl());

        writeOtfSegmentTemplate(item.getOtfTemplateUrl(), item.getOtfInitUrl(), "1", segments);
    }

    private void writeOtfSegmentTemplate(String mediaUrl, String initUrl, String startNumber, List<OtfSegment> segments) {
        if (segments != null && segments.size() > 0) {
            startTag("", "SegmentTemplate");

            attribute("", "timescale", "1000"); // units per second
            attribute("", "media", mediaUrl);
            attribute("", "initialization", initUrl);
            attribute("", "startNumber", startNumber);

            writeOtfSegmentTimeline(segments);

            endTag("", "SegmentTemplate");
        }
    }

    private void writeOtfSegmentTimeline(List<OtfSegment> segments) {
        if (segments != null && segments.size() > 0) {
            startTag("", "SegmentTimeline");

            int totalTime = 0;

            for (OtfSegment segment : segments) {
                startTag("", "S"); // segment set

                attribute("", "t", String.valueOf(totalTime)); // start time (units)
                attribute("", "d", segment.getDuration()); // duration (units)

                attribute("", "r", segment.getRepeatCount()); // repeat counts

                endTag("", "S");

                int segmentDuration = Integer.parseInt(segment.getDuration());
                int segmentRepeatCount = Integer.parseInt(segment.getRepeatCount()) + 1; // index zero based

                totalTime = totalTime + (segmentRepeatCount * segmentDuration);
            }

            endTag("", "SegmentTimeline");
        }
    }

    /**
     * Filter unplayable videos (init block is unavailable - youtube bug)
     */
    private List<MediaFormat> filterOtfItems(Set<MediaFormat> items) {
        List<MediaFormat> result = new ArrayList<>();

        for (MediaFormat item : items) {
            if (item.isOtf() && mSegmentParser.parse(item.getOtfInitUrl()) == null) {
                continue;
            }

            result.add(item);
        }

        return result;
    }

    @Override
    public boolean isDynamic() {
        return isLive();
    }

    @Override
    public void limitVideoCodec(String codec) {
        mLimitVideoCodec = codec;
    }

    @Override
    public void limitAudioCodec(String codec) {
        mLimitAudioCodec = codec;
    }
}
