package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.EndScreenItem;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.next.v1.models.EndScreenElement;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;

public class YouTubeEndScreenItem implements EndScreenItem {
    private int mType;
    private String mTitle;
    private String mMetadata;
    private String mImageUrl;
    private String mId;
    private long mStartTimeMs;
    private long mEndTimeMs;
    private float mLeft;
    private float mTop;
    private float mWidth;
    private float mAspectRatio;

    public static YouTubeEndScreenItem from(EndScreenElement element) {
        if (element == null) {
            return null;
        }

        YouTubeEndScreenItem item = new YouTubeEndScreenItem();

        // Determine type
        String style = element.getStyle();
        if ("VIDEO".equals(style)) {
            item.mType = TYPE_VIDEO;
        } else if ("CHANNEL".equals(style)) {
            item.mType = TYPE_CHANNEL;
        } else if ("PLAYLIST".equals(style)) {
            item.mType = TYPE_PLAYLIST;
        } else {
            item.mType = TYPE_UNKNOWN;
        }

        // Extract text content
        item.mTitle = element.getTitle() != null ? element.getTitle().toString() : null;
        item.mMetadata = element.getMetadata() != null ? element.getMetadata().toString() : null;

        // Extract image URL using gen helper
        if (element.getImage() != null) {
            item.mImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(element.getImage());
        }

        // Extract timing
        item.mStartTimeMs = Helpers.parseLong(element.getStartMs());
        item.mEndTimeMs = Helpers.parseLong(element.getEndMs());

        // Extract positioning
        item.mLeft = element.getLeft();
        item.mTop = element.getTop();
        item.mWidth = element.getWidth();
        item.mAspectRatio = element.getAspectRatio();

        // Extract ID based on endpoint type
        if (element.getEndpoint() != null) {
            EndScreenElement.Endpoint endpoint = element.getEndpoint();

            if (endpoint.getWatchEndpoint() != null) {
                item.mId = endpoint.getWatchEndpoint().getVideoId();
            } else if (endpoint.getBrowseEndpoint() != null) {
                item.mId = endpoint.getBrowseEndpoint().getBrowseId();
            }
        }

        return item;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getMetadata() {
        return mMetadata;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public long getStartTimeMs() {
        return mStartTimeMs;
    }

    @Override
    public long getEndTimeMs() {
        return mEndTimeMs;
    }

    @Override
    public float getLeft() {
        return mLeft;
    }

    @Override
    public float getTop() {
        return mTop;
    }

    @Override
    public float getWidth() {
        return mWidth;
    }

    @Override
    public float getAspectRatio() {
        return mAspectRatio;
    }
}