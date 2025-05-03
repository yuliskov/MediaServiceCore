package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;

import java.util.Comparator;

public class MediaFormatComparator implements Comparator<MediaFormat> {
    public static final int ORDER_DESCENDANT = 0;
    public static final int ORDER_ASCENDANT = 1;
    private int mOrderType = ORDER_DESCENDANT;

    public MediaFormatComparator() {
        
    }

    public MediaFormatComparator(int orderType) {
        mOrderType = orderType;
    }

    /**
     * NOTE: Descendant sorting (better on top). High quality playback on external player.
     */
    @Override
    public int compare(MediaFormat leftItem, MediaFormat rightItem) {
        if (leftItem.getGlobalSegmentList() != null ||
            rightItem.getGlobalSegmentList() != null) {
            return 1;
        }

        if (mOrderType == ORDER_ASCENDANT) {
            MediaFormat tmpItem = leftItem;
            leftItem = rightItem;
            rightItem = tmpItem;
        }

        int leftItemBitrate = leftItem.getBitrate() == null ? 0 : parseInt(leftItem.getBitrate());
        int rightItemBitrate = rightItem.getBitrate() == null ? 0 : parseInt(rightItem.getBitrate());

        int leftItemHeight = leftItem.getHeight();
        int rightItemHeight = rightItem.getHeight();

        int delta = rightItemHeight - leftItemHeight;

        if (delta == 0) {
            delta = rightItemBitrate - leftItemBitrate;
        }

        return delta;
    }

    private int parseInt(String num) {
        if (!Helpers.isNumeric(num)) {
            return 0;
        }

        return Integer.parseInt(num);
    }
}
