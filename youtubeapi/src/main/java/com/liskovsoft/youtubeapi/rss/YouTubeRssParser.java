package com.liskovsoft.youtubeapi.rss;

import android.util.Xml;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.helpers.DateHelper;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class YouTubeRssParser {
    // We don't use namespaces
    private static final String ns = null;
    private static final String TAG_MEDIA_ITEM = "entry";
    private static final String TAG_VIDEO_ID = "yt:videoId";
    private static final String TAG_CHANNEL_ID = "yt:channelId";
    private static final String TAG_TITLE = "title";
    private static final String TAG_PUBLISHED = "published";
    private static final String TAG_UPDATED = "updated";
    private static final String TAG_MEDIA_GROUP = "media:group";
    private static final String TAG_THUMBNAIL = "media:thumbnail";
    private static final String TAG_DESCRIPTION = "media:description";
    private static final String TAG_COMMUNITY = "media:community";
    private static final String TAG_STATISTICS = "media:statistics";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_AUTHOR_NAME = "name";
    private InputStream mContent;
    private XmlPullParser mParser;

    public YouTubeRssParser(InputStream content) {
        try {
            initParser(content);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void initParser(InputStream content) throws XmlPullParserException, IOException {
        if (content == null) {
            return;
        }

        mContent = content;
        mParser = Xml.newPullParser();
        mParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        mParser.setInput(content, null);
        mParser.nextTag();
    }

    public List<MediaItem> parse() {
        if (mContent == null) {
            return new ArrayList<>();
        }
        List<MediaItem> result;
        try {
            result = readMediaGroup();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    //private List<MediaItem> readRssFeed() throws IOException, XmlPullParserException {
    //    List<MediaItem> mediaItems = new ArrayList<>();
    //
    //    while (mParser.next() != XmlPullParser.END_TAG) {
    //        if (mParser.getEventType() != XmlPullParser.START_TAG) {
    //            continue;
    //        }
    //        String name = mParser.getName();
    //        // Starts by looking for the entry tag
    //        if (name.equals(TAG_MEDIA_GROUP)) {
    //            mediaItems.addAll(readMediaGroup());
    //        }
    //    }
    //    return mediaItems;
    //}

    private void skip() throws XmlPullParserException, IOException {
        if (mParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (mParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    
    private List<MediaItem> readMediaGroup() throws IOException, XmlPullParserException {
        List<MediaItem> mediaItems = new ArrayList<>();
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            if (name.equals(TAG_MEDIA_ITEM)) {
                mediaItems.add(readMediaItem());
            } else {
                skip();
            }
        }
        return mediaItems;
    }

    private MediaItem readMediaItem() throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, ns, TAG_MEDIA_ITEM);
        YouTubeMediaItem item = new YouTubeMediaItem();

        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            switch (name) {
                case TAG_VIDEO_ID:
                    item.setVideoId(readTagContent(TAG_VIDEO_ID));
                    break;
                case TAG_CHANNEL_ID:
                    item.setChannelId(readTagContent(TAG_CHANNEL_ID));
                    break;
                case TAG_TITLE:
                    item.setTitle(readTagContent(TAG_TITLE));
                    break;
                case TAG_PUBLISHED:
                    item.setPublishedDate(DateHelper.toUnixTimeMs(readTagContent(TAG_PUBLISHED)));
                    break;
                case TAG_UPDATED:
                    item.setUpdatedDate(DateHelper.toUnixTimeMs(readTagContent(TAG_UPDATED)));
                    break;
                case TAG_MEDIA_GROUP:
                    readMediaGroup(item);
                    break;
                case TAG_AUTHOR:
                    readAuthor(item);
                    break;
                default:
                    skip();
                    break;
            }
        }

        if (item.getSecondTitle() == null) {
            item.setSecondTitle(YouTubeHelper.createInfo(
                    item.getAuthor(), ServiceHelper.prettyCount(item.getViewCount()),
                        DateHelper.toShortDate(item.getPublishedDate(), true, true, false)));
        }

        return item;
    }

    private void readMediaGroup(YouTubeMediaItem item) throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, ns, TAG_MEDIA_GROUP);

        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            switch (name) {
                case TAG_THUMBNAIL:
                    item.setCardImageUrl(readTagAttribute(TAG_THUMBNAIL, "url"));
                    mParser.next();
                    break;
                case TAG_DESCRIPTION:
                    item.setDescription(readTagContent(TAG_DESCRIPTION));
                    break;
                case TAG_COMMUNITY:
                    readCommunity(item);
                    break;
                default:
                    skip();
                    break;
            }
        }
    }

    private void readCommunity(YouTubeMediaItem item) throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, ns, TAG_COMMUNITY);

        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            switch (name) {
                case TAG_STATISTICS:
                    item.setViewCount(Helpers.parseInt(readTagAttribute(TAG_STATISTICS, "views")));
                    mParser.next();
                    break;
                default:
                    skip();
                    break;
            }
        }
    }

    private void readAuthor(YouTubeMediaItem item) throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, ns, TAG_AUTHOR);

        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            switch (name) {
                case TAG_AUTHOR_NAME:
                    item.setAuthor(readTagContent(TAG_AUTHOR_NAME));
                    mParser.next();
                    break;
                default:
                    skip();
                    break;
            }
        }
    }

    private String readTagAttribute(String tagName, String attributeName) throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, ns, tagName);
        String value = mParser.getAttributeValue(ns, attributeName);
        mParser.next();
        return value;
    }

    private String readTagContent(String tagName) throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, ns, tagName);
        return readText();
    }

    private String readText() throws IOException, XmlPullParserException {
        String result = "";
        if (mParser.next() == XmlPullParser.TEXT) {
            result = mParser.getText();
            mParser.nextTag();
        }
        return result;
    }
}
