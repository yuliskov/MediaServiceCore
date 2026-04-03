package com.liskovsoft.youtubeapi.next.v1.models;

import com.google.gson.annotations.SerializedName;
import com.liskovsoft.googlecommon.common.models.V2.TextItem;
import com.liskovsoft.youtubeapi.common.models.gen.CommonItems;

public class EndScreenElement {
    @SerializedName("style")
    private String mStyle; // "CHANNEL", "VIDEO", "PLAYLIST"

    @SerializedName("image")
    private CommonItems.Thumbnail mImage;

    @SerializedName("title")
    private TextItem mTitle;

    @SerializedName("metadata")
    private TextItem mMetadata;

    @SerializedName("left")
    private float mLeft;

    @SerializedName("top")
    private float mTop;

    @SerializedName("width")
    private float mWidth;

    @SerializedName("aspectRatio")
    private float mAspectRatio;

    @SerializedName("startMs")
    private String mStartMs;

    @SerializedName("endMs")
    private String mEndMs;

    @SerializedName("endpoint")
    private Endpoint mEndpoint;

    public String getStyle() {
        return mStyle;
    }

    public CommonItems.Thumbnail getImage() {
        return mImage;
    }

    public TextItem getTitle() {
        return mTitle;
    }

    public TextItem getMetadata() {
        return mMetadata;
    }

    public float getLeft() {
        return mLeft;
    }

    public float getTop() {
        return mTop;
    }

    public float getWidth() {
        return mWidth;
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    public String getStartMs() {
        return mStartMs;
    }

    public String getEndMs() {
        return mEndMs;
    }

    public Endpoint getEndpoint() {
        return mEndpoint;
    }

    public static class Endpoint {
        @SerializedName("browseEndpoint")
        private BrowseEndpoint mBrowseEndpoint;

        @SerializedName("watchEndpoint")
        private WatchEndpoint mWatchEndpoint;

        public BrowseEndpoint getBrowseEndpoint() {
            return mBrowseEndpoint;
        }

        public WatchEndpoint getWatchEndpoint() {
            return mWatchEndpoint;
        }

        public static class BrowseEndpoint {
            @SerializedName("browseId")
            private String mBrowseId; // channelId or playlistId

            public String getBrowseId() {
                return mBrowseId;
            }
        }

        public static class WatchEndpoint {
            @SerializedName("videoId")
            private String mVideoId;

            public String getVideoId() {
                return mVideoId;
            }
        }
    }
}