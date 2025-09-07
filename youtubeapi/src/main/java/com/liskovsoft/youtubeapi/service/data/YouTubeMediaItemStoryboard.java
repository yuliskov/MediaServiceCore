package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser;
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser.Storyboard;

public class YouTubeMediaItemStoryboard implements MediaItemStoryboard {
    private final Storyboard mStoryboard;
    private Size mSize;

    public YouTubeMediaItemStoryboard(Storyboard storyboard) {
        mStoryboard = storyboard;
    }

    public static MediaItemStoryboard from(Storyboard storyboard) {
        if (storyboard == null) {
            return null;
        }

        return new YouTubeMediaItemStoryboard(storyboard);
    }

    @Override
    public int getGroupDurationMS() {
        return mStoryboard.getGroupDurationMS();
    }

    @Override
    public Size getGroupSize() {
        if (mSize == null) {
            mSize = new Size(mStoryboard.getGroupSize());
        }
        return mSize;
    }

    @Override
    public String getGroupUrl(int imgNum) {
        return mStoryboard.getGroupUrl(imgNum + mSize.getStartNum());
    }

    public static class Size implements MediaItemStoryboard.Size {
        private final YouTubeStoryParser.Size mSize;

        public Size(YouTubeStoryParser.Size size) {
            mSize = size;
        }

        @Override
        public int getDurationEachMS() {
            return mSize.getDurationEachMS();
        }

        @Override
        public int getStartNum() {
            return mSize.getStartNum();
        }

        @Override
        public int getWidth() {
            return mSize.getWidth();
        }

        @Override
        public int getHeight() {
            return mSize.getHeight();
        }

        @Override
        public int getRowCount() {
            return mSize.getRowCount();
        }

        @Override
        public int getColCount() {
            return mSize.getColCount();
        }
    }
}
