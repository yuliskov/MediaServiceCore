package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.MediaFormatComparator;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class YouTubeUrlListBuilder implements UrlListBuilder {
    private final Set<MediaFormat> mVideos;
    private final MediaItemFormatInfo mInfo;

    public YouTubeUrlListBuilder(MediaItemFormatInfo formatInfo) {
        mInfo = formatInfo;
        MediaFormatComparator comp = new MediaFormatComparator(MediaFormatComparator.ORDER_ASCENDANT);
        mVideos = new TreeSet<>(comp);
    }

    public static UrlListBuilder from(MediaItemFormatInfo formatInfo) {
        UrlListBuilder builder = new YouTubeUrlListBuilder(formatInfo);

        if (formatInfo.containsUrlFormats()) {
            for (MediaFormat format : formatInfo.getUrlFormats()) {
                builder.append(format);
            }
        }

        return builder;
    }

    @Override
    public void append(MediaFormat mediaItem) {
        if (!MediaFormatUtils.isDash(mediaItem)) {
            mVideos.add(mediaItem);
        }
    }

    @Override
    public boolean isEmpty() {
        return mVideos.size() == 0;
    }

    @Override
    public List<String> buildUriList() {
        if (!mInfo.containsUrlFormats()) {
            return null;
        }

        List<String> list = new ArrayList<>();

        // Put hq items on top.
        // Note, hq items sometimes cannot be played.
        for (MediaFormat item : mVideos) {
            list.add(0, item.getUrl());
        }

        return list;
    }
}
