package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.next.models.NextVideo;
import com.liskovsoft.youtubeapi.next.models.VideoMetadata;
import com.liskovsoft.youtubeapi.next.models.VideoOwner;
import com.liskovsoft.youtubeapi.next.models.WatchNextItem;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.next.models.WatchNextSection;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WatchNextManagerTestBase {
    public void checkWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);

        checkFields(watchNextResult.getVideoMetadata());
        checkFields(watchNextResult.getVideoOwner());
        checkFields(watchNextResult.getNextVideo());
        checkFields(watchNextResult.getWatchNextSections());
    }

    private void checkFields(WatchNextItem watchNextItem) {
        String videoId = watchNextItem.getVideoId();
        assertNotNull("Watch next item has title: " + videoId, watchNextItem.getTitle());
        assertNotNull("Watch next item has video id: " + videoId, videoId);
        assertNotNull("Watch next item has user name: " + videoId, watchNextItem.getUserName());
        assertNotNull("Watch next item has channel id: " + videoId, watchNextItem.getChannelId());
        assertTrue("Watch next item has view count: " + videoId, watchNextItem.getViewCountText() != null || watchNextItem.isLive());
        assertTrue("Watch next item has length: " + videoId, watchNextItem.getLengthText() != null || watchNextItem.isLive());
        assertNotNull("Watch next item has thumbnails: " + videoId, watchNextItem.getThumbnails());
        assertTrue("Watch next item thumbnails not empty: " + videoId, watchNextItem.getThumbnails().size() > 0);
    }

    private void checkFields(List<WatchNextSection> watchNextSections) {
        assertNotNull("Watch next contains rows", watchNextSections);

        WatchNextSection firstRow = watchNextSections.get(0);

        assertNotNull("Row has title", firstRow.getTitle());
        assertNotNull("Row has continuation data", firstRow.getNextPageKey());

        WatchNextItem watchNextItem = firstRow.getWatchNextItems().get(0);

        checkFields(watchNextItem);
    }

    private void checkFields(VideoMetadata videoMetadata) {
        assertNotNull("Video metadata not empty", videoMetadata);
        assertNotNull("Video metadata has video id", videoMetadata.getVideoId());
        assertNotNull("Video metadata has title", videoMetadata.getTitle());
        assertNotNull("Video metadata has description", videoMetadata.getDescription());
        assertNotNull("Video metadata has published date", videoMetadata.getPublishedDate());
        assertNotNull("Video metadata has view count", videoMetadata.getViewCount());
        assertNotNull("Video metadata has like status", videoMetadata.getLikeStatus());
    }

    private void checkFields(VideoOwner videoOwner) {
        assertNotNull("Video owner not empty", videoOwner);
        assertNotNull("Video owner has channel id", videoOwner.getChannelId());
        assertNotNull("Video owner has subscriber count", videoOwner.getSubscriberCount());
        assertNotNull("Video owner has author name", videoOwner.getVideoAuthor());
    }

    private void checkFields(NextVideo nextVideo) {
        assertNotNull("Next video not empty", nextVideo);
        assertNotNull("Next video has video id", nextVideo.getVideoId());
        assertNotNull("Next video has title", nextVideo.getTitle());
        assertNotNull("Next video has author name", nextVideo.getAuthor());
        assertNotNull("Next video has thumbnails", nextVideo.getThumbnails());
    }
}
