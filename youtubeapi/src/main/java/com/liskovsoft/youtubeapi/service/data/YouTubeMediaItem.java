package com.liskovsoft.youtubeapi.service.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.v1.models.NextVideo;
import com.liskovsoft.youtubeapi.common.models.V2.TileItem;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;

public class YouTubeMediaItem implements MediaItem {
    //private static int sId;
    private int mId;
    private String mTitle;
    private CharSequence mSecondTitle;
    private String mVideoId;
    private String mChannelId;
    private String mPlaylistId;
    private String mCardImageUrl;
    private String mBackgroundImageUrl;
    private String mContentType;
    private boolean mIsLive;
    private boolean mIsShorts;
    private boolean mIsUpcoming;
    private String mLengthText;
    private String mBadgeText;
    private String mProductionDate;
    private int mWidth;
    private int mHeight;
    private String mAudioChannelConfig;
    private String mPurchasePrice;
    private String mRentalPrice;
    private int mRatingStyle;
    private double mRatingScore;
    private int mMediaItemType;
    private int mPercentWatched = -1;
    private int mStartTimeSeconds;
    private String mAuthor;
    private String mVideoPreviewUrl;
    private int mPlaylistIndex;
    private String mReloadPageKey;
    private boolean mHasNewContent;
    private String mFeedbackToken;
    private String mParams;
    private String mClickTrackingParams;
    private boolean mIsMovie;
    private long mPublishedDate;
    private long mUpdatedDate;
    private String mDescription;
    private int mViewCount;

    public static YouTubeMediaItem from(ItemWrapper item, int position) {
        YouTubeMediaItem mediaItem = from(item);

        // In case can't find a position of item inside browse playlist query. So using position inside group instead.
        if (mediaItem != null && mediaItem.mPlaylistIndex == -1 && mediaItem.mPlaylistId != null) {
            mediaItem.mPlaylistIndex = position;
        }

        return mediaItem;
    }

    public static YouTubeMediaItem from(ItemWrapper item) {
        if (item.getVideoItem() != null) {
            return from(item.getVideoItem());
        } else if (item.getMusicItem() != null) {
            return from(item.getMusicItem());
        } else if (item.getChannelItem() != null) {
            return from(item.getChannelItem());
        } else if (item.getPlaylistItem() != null) {
            return from(item.getPlaylistItem());
        } else if (item.getRadioItem() != null) {
            return from(item.getRadioItem());
        } else if (item.getTileItem() != null) {
            return from(item.getTileItem());
        }

        return null;
    }

    public static YouTubeMediaItem from(TileItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        switch (item.getContentType() != null ? item.getContentType() : "") {
            case TileItem.CONTENT_TYPE_PLAYLIST:
                video.mMediaItemType = MediaItem.TYPE_PLAYLIST;
                break;
            case TileItem.CONTENT_TYPE_CHANNEL:
                video.mMediaItemType = MediaItem.TYPE_CHANNEL;
                break;
            case TileItem.CONTENT_TYPE_VIDEO:
                video.mMediaItemType = MediaItem.TYPE_VIDEO;
                break;
            default:
                video.mMediaItemType = MediaItem.TYPE_UNDEFINED;
                break;
        }

        video.mTitle = item.getTitle();
        video.mSecondTitle = YouTubeHelper.createInfo(
                item.getDescBadgeText(), // Mostly it's a 4K label
                item.getUserName(),
                item.getPublishedTime(),
                item.getViewCountText(),
                item.getUpcomingEventText());
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mProductionDate = item.getPublishedTime();
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mParams = item.getPlaylistParams();
        video.mPlaylistIndex = item.getPlaylistIndex();
        video.mChannelId = item.getChannelId();
        // TODO: time conversion doesn't take into account locale's specific delimiters
        video.mLengthText = item.getBadgeText();
        video.mBadgeText = item.getBadgeText();
        video.mPercentWatched = item.getPercentWatched();
        video.mStartTimeSeconds = item.getStartTimeSeconds();
        video.mAuthor = item.getUserName();
        video.mVideoPreviewUrl = item.getRichThumbnailUrl();
        video.mIsLive = item.isLive();
        video.mIsShorts = item.isShorts();
        video.mIsUpcoming = item.isUpcoming();
        video.mFeedbackToken = item.getFeedbackToken();
        video.mClickTrackingParams = item.getClickTrackingParams();
        video.mIsMovie = item.isMovie();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(VideoItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mTitle = item.getTitle();
        video.mSecondTitle = YouTubeHelper.createInfo(
                item.getDescBadgeText(), // Mostly it's a 4K label
                item.getUserName(),
                item.getPublishedDate(),
                item.getShortViewCountText() != null ? item.getShortViewCountText() : item.getViewCountText(),
                item.getUpcomingEventText());
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mProductionDate = item.getPublishedDate();
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mPlaylistIndex = item.getPlaylistIndex();
        video.mChannelId = item.getChannelId();
        // TODO: time conversion doesn't take into account locale specific delimiters
        video.mLengthText = item.getLengthText() != null ? item.getLengthText() : item.getBadgeText();
        video.mBadgeText = item.getBadgeText() != null ? item.getBadgeText() : item.getLengthText();
        video.mPercentWatched = item.getPercentWatched();
        video.mStartTimeSeconds = item.getStartTimeSeconds();
        video.mAuthor = Helpers.toString(item.getUserName());
        video.mVideoPreviewUrl = item.getRichThumbnailUrl();
        video.mIsLive = item.isLive();
        video.mIsUpcoming = item.isUpcoming();
        video.mFeedbackToken = item.getFeedbackToken();
        video.mClickTrackingParams = item.getClickTrackingParams();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(MusicItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_MUSIC;
        video.mTitle = item.getTitle();
        video.mSecondTitle = YouTubeHelper.createInfo(
                item.getUserName(),
                item.getViewsAndPublished());
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mProductionDate = item.getViewsAndPublished();
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mPlaylistIndex = item.getPlaylistIndex();
        video.mChannelId = item.getChannelId();
        // TODO: time conversion doesn't take into account locale specific delimiters
        video.mLengthText = item.getLengthText();
        video.mBadgeText = item.getLengthText();
        video.mPercentWatched = item.getPercentWatched();
        video.mAuthor = item.getUserName();
        video.mVideoPreviewUrl = item.getRichThumbnailUrl();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(ChannelItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_CHANNEL;
        video.mTitle = item.getTitle();
        video.mSecondTitle = YouTubeHelper.createInfo(item.getSubscriberCountText());
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mChannelId = item.getChannelId();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(RadioItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_PLAYLIST;
        video.mTitle = item.getTitle();
        video.mSecondTitle = YouTubeHelper.createInfo(item.getVideoCountText());
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mPlaylistId = item.getPlaylistId();
        video.mVideoId = item.getVideoId();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(PlaylistItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_PLAYLIST;
        video.mTitle = item.getTitle();
        video.mSecondTitle = YouTubeHelper.createInfo(item.getVideoCountText());
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mPlaylistId = item.getPlaylistId();
        video.mVideoId = item.getVideoId();

        addCommonProps(video);

        return video;
    }

    private static void addCommonProps(YouTubeMediaItem video) {
        //video.mId = createId(video);
        video.mContentType = "video/mp4";
        video.mWidth = 1280;
        video.mHeight = 720;
        video.mAudioChannelConfig = "2.0";
        video.mPurchasePrice = "$5.99";
        video.mRentalPrice = "$4.99";
        video.mRatingStyle = 5;
        video.mRatingScore = 4d;
    }

    public static MediaItem from(NextVideo item) {
        if (item == null) {
            return null;
        }

        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mTitle = item.getTitle();
        video.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(item.getThumbnails());
        video.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mPlaylistIndex = item.getPlaylistItemIndex();
        addCommonProps(video);

        return video;
    }

    /**
     * Special item derived from tab (e.g. user Library sections)
     */
    public static YouTubeMediaItem from(GridTab tab, int type) {
        YouTubeMediaItem item = new YouTubeMediaItem();
        
        item.mMediaItemType = type;
        item.mTitle = tab.getTitle();
        item.mCardImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(tab.getThumbnails());
        item.mBackgroundImageUrl = YouTubeHelper.findHighResThumbnailUrl(tab.getThumbnails());
        item.mReloadPageKey = tab.getReloadPageKey();
        item.mHasNewContent = tab.hasNewContent();
        addCommonProps(item);

        return item;
    }

    //private static int createId(YouTubeMediaItem item) {
    //    int id;
    //
    //    if (item.mVideoId != null) {
    //        id = item.mVideoId.hashCode();
    //    } else if (item.mChannelId != null) {
    //        id = item.mChannelId.hashCode();
    //    } else {
    //        id = sId++;
    //    }
    //
    //    return id;
    //}

    @Override
    public int getId() {
        if (mId == 0) {
            mId = hashCode();
        }

        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public CharSequence getSecondTitle() {
        return mSecondTitle;
    }

    public void setSecondTitle(CharSequence secondTitle) {
        mSecondTitle = secondTitle;
    }

    @Override
    public int getType() {
        return mMediaItemType;
    }

    @Override
    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        mCardImageUrl = cardImageUrl;
    }

    @Override
    public String getBackgroundImageUrl() {
        return mBackgroundImageUrl;
    }

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    @Override
    public String getContentType() {
        return mContentType;
    }

    @Override
    public boolean isLive() {
        return mIsLive;
    }

    public void setLive(boolean isLive) {
        mIsLive = isLive;
    }

    @Override
    public boolean isUpcoming() {
        return mIsUpcoming;
    }

    public void setUpcoming(boolean isUpcoming) {
        mIsUpcoming = isUpcoming;
    }

    @Override
    public boolean isShorts() {
        return mIsShorts;
    }

    @Override
    public boolean isMovie() {
        return mIsMovie;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public String getAudioChannelConfig() {
        return mAudioChannelConfig;
    }

    @Override
    public String getPurchasePrice() {
        return mPurchasePrice;
    }

    @Override
    public String getRentalPrice() {
        return mRentalPrice;
    }

    @Override
    public int getRatingStyle() {
        return mRatingStyle;
    }

    @Override
    public double getRatingScore() {
        return mRatingScore;
    }

    @Override
    public String getProductionDate() {
        return mProductionDate;
    }

    @Override
    public long getDurationMs() {
        return ServiceHelper.timeTextToMillis(mLengthText);
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    @Override
    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    @Override
    public String getParams() {
        return mParams;
    }

    public void setParams(String params) {
        if (params != null) {
            mParams = params;
        }
    }

    @Override
    public int getPercentWatched() {
        return mPercentWatched;
    }

    public void setPercentWatched(int percentWatched) {
        mPercentWatched = percentWatched;
    }

    @Override
    public int getStartTimeSeconds() {
        return mStartTimeSeconds;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    @Override
    public String getBadgeText() {
        return mBadgeText;
    }

    public void setBadgeText(String badgeText) {
        mBadgeText = badgeText;
    }

    @Override
    public boolean hasNewContent() {
        return mHasNewContent;
    }

    @Override
    public String getVideoPreviewUrl() {
        return mVideoPreviewUrl;
    }

    public void setVideoPreviewUrl(String videoPreviewUrl) {
        mVideoPreviewUrl = videoPreviewUrl;
    }

    @Override
    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    @Override
    public String getFeedbackToken() {
        return mFeedbackToken;
    }

    @Override
    public String getFeedbackToken2() {
        return null;
    }

    @Override
    public boolean hasUploads() {
        return mReloadPageKey != null;
    }

    @Override
    public String getClickTrackingParams() {
        return mClickTrackingParams;
    }

    @Override
    public String getSearchQuery() {
        return null;
    }

    public void sync(MediaItemMetadata metadata) {
        if (metadata == null) {
            return;
        }

        mTitle = metadata.getTitle();
        mSecondTitle = metadata.getSecondTitle();
        mChannelId = metadata.getChannelId();
        mParams = metadata.getParams();
    }

    @Override
    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    public void setReloadPageKey(String reloadPageKey) {
        mReloadPageKey = reloadPageKey;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s", mReloadPageKey, mTitle, mSecondTitle, mCardImageUrl, mVideoId, mPlaylistId, mChannelId, mMediaItemType);
    }

    public static MediaItem fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = spec.split("&mi;");

        // 'mMediaItemType' backward compatibility
        if (split.length == 7) {
            split = Helpers.appendArray(split, new String[]{"-1"});
        }

        if (split.length != 8) {
            return null;
        }

        YouTubeMediaItem mediaItem = new YouTubeMediaItem();

        mediaItem.mReloadPageKey = Helpers.parseStr(split[0]);
        mediaItem.mTitle = Helpers.parseStr(split[1]);
        mediaItem.mSecondTitle = Helpers.parseStr(split[2]);
        mediaItem.mCardImageUrl = Helpers.parseStr(split[3]);
        mediaItem.mVideoId = Helpers.parseStr(split[4]);
        mediaItem.mPlaylistId = Helpers.parseStr(split[5]);
        mediaItem.mChannelId = Helpers.parseStr(split[6]);
        mediaItem.mMediaItemType = Helpers.parseInt(split[7]);

        return mediaItem;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof YouTubeMediaItem) {
            if (mVideoId != null) {
                return mVideoId.equals(((YouTubeMediaItem) obj).mVideoId);
            }

            if (mPlaylistId != null) {
                return mPlaylistId.equals(((YouTubeMediaItem) obj).mPlaylistId);
            }

            if (mChannelId != null) {
                return mChannelId.equals(((YouTubeMediaItem) obj).mChannelId);
            }

            if (mReloadPageKey != null) {
                return mReloadPageKey.equals(((YouTubeMediaItem) obj).mReloadPageKey);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        //return Helpers.hashCode(mVideoId, mPlaylistId, mChannelId, mReloadPageKey);
        int hashCodeAny = YouTubeHelper.hashCodeAny(this);
        return hashCodeAny != -1 ? hashCodeAny : super.hashCode();
    }

    public static String serializeMediaItem(MediaItem mediaItem) {
        if (mediaItem == null) {
            return null;
        }

        return mediaItem.toString();
    }

    public static MediaItem deserializeMediaItem(String itemSpec) {
        if (itemSpec == null) {
            return null;
        }

        return fromString(itemSpec);
    }

    @Override
    public long getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(long publishedDate) {
        mPublishedDate = publishedDate;
    }

    public long getUpdatedDate() {
        return mUpdatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        mUpdatedDate = updatedDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getViewCount() {
        return mViewCount;
    }

    public void setViewCount(int views) {
        mViewCount = views;
    }
}
