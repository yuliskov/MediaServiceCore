package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import android.util.Xml;
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle;
import com.liskovsoft.sharedutils.helpers.FileHelpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.YouTubeOtfSegmentParser.OtfSegment;
import com.liskovsoft.youtubeapi.formatbuilders.utils.ITagUtils;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Demos: https://github.com/Dash-Industry-Forum/dash-live-source-simulator/wiki/Test-URLs
 */
public class YouTubeMPDBuilder implements MPDBuilder {
    private static final String NULL_INDEX_RANGE = "0-0";
    private static final String NULL_CONTENT_LENGTH = "0";
    private static final String TAG = YouTubeMPDBuilder.class.getSimpleName();
    private static final int MAX_DURATION_SEC = 48 * 60 * 60;
    private final MediaItemFormatInfo mInfo;
    private XmlSerializer mXmlSerializer;
    private StringWriter mWriter;
    private int mId;
    private final Set<MediaFormat> mMP4Videos;
    private final Set<MediaFormat> mWEBMVideos;
    private final Map<String, Set<MediaFormat>> mMP4Audios;
    private final Map<String, Set<MediaFormat>> mWEBMAudios;
    private final List<MediaSubtitle> mSubs;
    private final YouTubeOtfSegmentParser mSegmentParser;
    private String mLimitVideoCodec;
    private String mLimitAudioCodec;

    private YouTubeMPDBuilder(MediaItemFormatInfo info) {
        mInfo = info;
        MediaFormatComparator comp = new MediaFormatComparator();
        mMP4Videos = new TreeSet<>(comp);
        mWEBMVideos = new TreeSet<>(comp);
        mMP4Audios = new HashMap<>();
        mWEBMAudios = new HashMap<>();
        mSubs = new ArrayList<>();
        mSegmentParser = new YouTubeOtfSegmentParser(true);

        initXmlSerializer();
    }

    public static MPDBuilder from(MediaItemFormatInfo formatInfo) {
        MPDBuilder builder = new YouTubeMPDBuilder(formatInfo);

        if (formatInfo.containsDashFormats()) {
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

        // https://docs.unified-streaming.com/documentation/live/configure-dynamic-mpd.html
        if (isLive()) {
            attribute("", "profiles", "urn:mpeg:dash:profile:isoff-on-demand:2011"); // Better caching. Default is isoff-main.
            attribute("", "type", "dynamic");
            attribute("", "minimumUpdatePeriod", "P100Y"); // no refresh (there is no dash url)

            // TESTING
            //attribute("", "minimumBufferTime", "PT30S"); // no effect?
            //attribute("", "profiles", "urn:mpeg:dash:profile:isoff-live:2011");
            //attribute("", "profiles", "urn:mpeg:dash:profile:isoff-main:2011");
            //attribute("", "minimumUpdatePeriod", "PT5.000S");
            // availabilityStartTime="2019-01-06T17:04:49"
            //attribute("", "publishTime", "2022-08-25T00:00:00Z");
            //attribute("", "availabilityStartTime", "2022-08-25T00:00:00Z");
            //attribute("", "timeShiftBufferDepth", "PT120M");
            //attribute("", "maxSegmentDuration", "PT2S");

            // TESTING (live position)
            //attribute("", "timeShiftBufferDepth", "PT43200.000S");
            //attribute("", "publishTime", "2022-09-04T10:00:00");
            //attribute("", "profiles", "urn:mpeg:dash:profile:isoff-main:2011");
            //attribute("", "minimumUpdatePeriod", "PT2.000S");
            //attribute("", "timeShiftBufferDepth", "PT7200.000S");
            //attribute("", "minBufferTime", "PT1.500S");
            //attribute("", "availabilityStartTime", "2022-09-20T23:15:31");
        } else {
            attribute("", "profiles", "urn:mpeg:dash:profile:isoff-on-demand:2011");
            attribute("", "type", "static");
            attribute("", "mediaPresentationDuration", durationParam);
        }


        startTag("", "Period");

        if (isLive()) {
            // yt:segmentIngestTime="2019-01-06T17:55:24.836"
            attribute("", "start", "PT0S"); // mandatory attr
            // TESTING
            //attribute("", "start", "PT3050.000S");
            //attribute("", "duration", "PT3050.000S");
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
        writeMediaTagsForGroup(mMP4Videos);
        writeMediaTagsForGroup(mWEBMVideos);

        for (Set<MediaFormat> formats : mMP4Audios.values()) {
            writeMediaTagsForGroup(formats);
        }

        for (Set<MediaFormat> formats : mWEBMAudios.values()) {
            writeMediaTagsForGroup(formats);
        }
        
        writeMediaTagsForGroup(mSubs);
    }

    private void writeLiveHeaderSegmentList() {
        //startTag("", "SegmentList");
        //
        //attribute("", "presentationTimeOffset", "3050000");
        //attribute("", "startNumber", "610");
        //attribute("", "timescale", "1000");
        //
        //startTag("", "SegmentTimeline");
        //
        //for (int i = 0; i < 3; i++) {
        //    startTag("", "S");
        //
        //    attribute("", "d", "5000");
        //
        //    endTag("", "S");
        //}
        //
        //endTag("", "SegmentTimeline");
        //
        //endTag("", "SegmentList");
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

        writeMediaListPrologue(String.valueOf(mId++), MediaFormatUtils.extractMimeType(firstItem), firstItem.getLanguage());

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

    private void writeMediaListPrologue(String id, String mimeType, String language) {
        startTag("", "AdaptationSet");
        attribute("", "id", id);
        attribute("", "mimeType", mimeType);
        if (language != null) {
            attribute("", "lang", language);
        }
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
        String mimeType = MediaFormatUtils.extractMimeType(mediaItem);
        if (mimeType != null) {
            switch (mimeType) {
                case MediaFormatUtils.MIME_MP4_VIDEO:
                    placeholder = mMP4Videos;
                    break;
                case MediaFormatUtils.MIME_WEBM_VIDEO:
                    placeholder = mWEBMVideos;
                    break;
                case MediaFormatUtils.MIME_MP4_AUDIO:
                    placeholder = getMP4Audios(mediaItem.getLanguage());
                    break;
                case MediaFormatUtils.MIME_WEBM_AUDIO:
                    placeholder = getWEBMAudios(mediaItem.getLanguage());
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

    private void writeMediaFormatTag(MediaFormat format) {
        startTag("", "Representation");

        attribute("", "id", format.isDrc() ? format.getITag() + "-drc" : format.getITag());
        attribute("", "codecs", MediaFormatUtils.extractCodecs(format));
        attribute("", "startWithSAP", "1");
        attribute("", "bandwidth", format.getBitrate());

        if (isVideo(format)) {
            // video attrs
            attribute("", "width", String.valueOf(format.getWidth()));
            attribute("", "height", String.valueOf(format.getHeight()));
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
            writeLiveMediaSegmentList(format);
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

    private void writeLiveMediaSegmentList(MediaFormat format) {
        //startTag("", "SegmentList");
        //
        //for (String mediaDesc : new String[]{
        //        "sq/610/lmt/1546797364563137",
        //        "sq/611/lmt/1546797365000899",
        //        "sq/612/lmt/1546797369574434"}) {
        //    startTag("", "SegmentURL");
        //
        //    attribute("", "media", mediaDesc);
        //
        //    endTag("", "SegmentURL");
        //}
        //
        //endTag("", "SegmentList");

        // Last segment index:
        // https://docs.aws.amazon.com/mediapackage/latest/ug/segtemp-format-duration.html#how-stemp-dur-works
        // ((wall clock time - availabilityStartTime ) / (duration / timescale )) + startNumber

        int unitsPerSecond = 1_000_000;

        // Present on live streams only.
        int segmentDurationUs = mInfo.getSegmentDurationUs();

        if (segmentDurationUs <= 0) {
            // Inaccurate. Present on past (!) live streams.
            segmentDurationUs = format.getTargetDurationSec() * 1_000_000;
        }

        int lengthSeconds = Integer.parseInt(mInfo.getLengthSeconds());

        if (mInfo.isLive() || lengthSeconds <= 0) {
            // For premiere streams (length > 0) or regular streams (length == 0) set window that exceeds normal limits - 48hrs
            lengthSeconds = MAX_DURATION_SEC;
        }

        // To make long streams (12hrs) seekable we should decrease size of the segment a bit
        //long segmentDurationUnits = (long) targetDurationSec * unitsPerSecond * 9999 / 10000;
        int segmentDurationUnits = (int)(segmentDurationUs * (long) unitsPerSecond / 1_000_000);
        // Increase count a bit to compensate previous tweak
        //long segmentCount = (long) lengthSeconds / targetDurationSec * 10000 / 9999;
        //int segmentCount = (int)(lengthSeconds * (long) unitsPerSecond / segmentDurationUnits);
        int segmentCount = (int) Math.ceil(lengthSeconds * (double) unitsPerSecond / segmentDurationUnits);
        // Increase offset a bit to compensate previous tweaks
        // Streams to check:
        // https://www.youtube.com/watch?v=drdemkJpgao
        long offsetUnits = (long) segmentDurationUnits * mInfo.getStartSegmentNum();

        String segmentDurationUnitsStr = String.valueOf(segmentDurationUnits);
        // Use offset to sync player timeline with MPD timeline!!!
        String offsetUnitsStr = String.valueOf(offsetUnits);

        startTag("", "SegmentTemplate");

        attribute("", "duration", segmentDurationUnitsStr); // segment duration in units (could be safely omitted)
        attribute("", "timescale", String.valueOf(unitsPerSecond)); // units per second
        attribute("", "media", format.getUrl() + "&sq=$Number$");
        attribute("", "startNumber", String.valueOf(mInfo.getStartSegmentNum()));
        // Used in SegmentBase, SegmentTemplate or BaseURL
        attribute("", "presentationTimeOffset", offsetUnitsStr); // in units
        //attribute("", "availabilityTimeOffset", "43200000");

        // lengthSeconds > 0 indicates past live stream

        // https://www.unified-streaming.com/blog/stop-numbering-underappreciated-power-dashs-segmenttimeline
        //  <SegmentTimeline>
        //    <S t="0" d="180000" r="394"/>
        //  </SegmentTimeline>

        startTag("", "SegmentTimeline");

        startTag("", "S"); // segment set

        attribute("", "t", offsetUnitsStr); // start time (units)
        attribute("", "d", segmentDurationUnitsStr); // duration (units)
        attribute("", "r", String.valueOf(segmentCount)); // repeat counts (number of segments)

        endTag("", "S");

        endTag("", "SegmentTimeline");

        endTag("", "SegmentTemplate");
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
        return item.getWidth() > 0 && item.getHeight() > 0;
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

        if (mInfo.getLengthSeconds() == null) {
            Log.e(TAG, "FormatInfo doesn't contain duration");
            return false;
        }
        
        return true;
    }

    @Override
    public InputStream build() {
        if (!mInfo.containsDashFormats()) {
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

    private boolean isLive() {
        for (MediaFormat item : mMP4Videos) {
            return MediaFormatUtils.isLiveMedia(item);
        }

        for (MediaFormat item : mWEBMVideos) {
            return MediaFormatUtils.isLiveMedia(item);
        }

        return false;
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

    private Set<MediaFormat> getMP4Audios(String language) {
        return getFormats(mMP4Audios, language);
    }

    private Set<MediaFormat> getWEBMAudios(String language) {
        return getFormats(mWEBMAudios, language);
    }

    private static Set<MediaFormat> getFormats(Map<String, Set<MediaFormat>> formatMap, String language) {
        if (language == null) {
            language = "default";
        }

        Set<MediaFormat> mediaFormats = formatMap.get(language);

        if (mediaFormats == null) {
            mediaFormats = new TreeSet<>(new MediaFormatComparator());
            formatMap.put(language, mediaFormats);
        }

        return mediaFormats;
    }
}
