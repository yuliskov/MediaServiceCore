package com.liskovsoft.youtubeapi.videoinfo.old.hlsbuilder;

import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.MediaItem;
import com.liskovsoft.youtubeapi.videoinfo.old.misc.MediaItemComparator;
import com.liskovsoft.youtubeapi.videoinfo.old.misc.MediaItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SimpleUrlListBuilder implements UrlListBuilder {
    private final Set<MediaItem> mVideos;

    public SimpleUrlListBuilder() {
        MediaItemComparator comp = new MediaItemComparator(MediaItemComparator.ORDER_ASCENDANT);
        mVideos = new TreeSet<>(comp);
    }

    @Override
    public void append(MediaItem mediaItem) {
        if (!MediaItemUtils.isDash(mediaItem)) {
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
        for (MediaItem item : mVideos) {
            list.add(0, item.getUrl());
        }

        // remain only first item as ExoPlayer doesn't support adaptive streaming for url list
        return list.subList(0, 1);
    }
}
