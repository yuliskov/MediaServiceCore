package com.liskovsoft.youtubeapi.formatbuilders.utils;

import com.liskovsoft.mediaserviceinterfaces.MediaFormat;
import com.liskovsoft.sharedutils.helpers.Helpers;

public class MediaFormatUtils {
    public static String getHeight(MediaFormat item) {
        String size = item.getSize();

        if (size == null) {
            return "";
        }

        String[] widthHeight = size.split("x");

        if (widthHeight.length != 2) {
            return "";
        }

        return widthHeight[1];
    }

    public static String getWidth(MediaFormat item) {
        String size = item.getSize();

        if (size == null) {
            return "";
        }

        String[] widthHeight = size.split("x");

        if (widthHeight.length != 2) {
            return "";
        }

        return widthHeight[0];
    }

    public static boolean isDash(MediaFormat mediaItem) {
        if (mediaItem.getITag() == null) {
            return false;
        }

        if (mediaItem.getGlobalSegmentList() != null) {
            return true;
        }

        String id = mediaItem.getITag();

        return Helpers.isDash(id);
    }

    public static boolean checkMediaUrl(MediaFormat mediaItem) {
        return mediaItem != null && mediaItem.getUrl() != null;
    }
}
