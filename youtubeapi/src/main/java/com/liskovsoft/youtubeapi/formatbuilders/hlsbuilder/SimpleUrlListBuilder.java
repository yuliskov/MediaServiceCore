package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.MediaFormat;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.MediaFormatComparator;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SimpleUrlListBuilder implements UrlListBuilder {
    private final Set<MediaFormat> mVideos;

    public SimpleUrlListBuilder() {
        MediaFormatComparator comp = new MediaFormatComparator(MediaFormatComparator.ORDER_ASCENDANT);
        mVideos = new TreeSet<>(comp);
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
        List<String> list = new ArrayList<>();

        // put hq items on top
        for (MediaFormat item : mVideos) {
            list.add(0, item.getUrl());
        }

        // remain only first item as ExoPlayer doesn't support adaptive streaming for url list
        return list.subList(0, 1);
    }
}
