package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.models.NextVideo;
import com.liskovsoft.youtubeapi.next.models.Playlist;
import com.liskovsoft.youtubeapi.next.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.models.CurrentVideo;
import com.liskovsoft.youtubeapi.next.models.VideoOwner;
import com.liskovsoft.youtubeapi.next.result.WatchNextResult;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WatchNextManagerTestBase {
    public void checkWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);

        checkFields(watchNextResult.getVideoMetadata());
        checkFields(watchNextResult.getVideoOwner());
        checkFields(watchNextResult.getNextVideo(), watchNextResult.getPlaylist() != null);
        checkFields(watchNextResult.getSuggestedSections());
    }

    public void checkSignedWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);

        checkFields(watchNextResult.getVideoMetadata());
        checkSignedFields(watchNextResult.getVideoOwner());
        checkFields(watchNextResult.getNextVideo(), false);
        checkFields(watchNextResult.getSuggestedSections());
    }

    public void checkSignedPlaylistWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);

        checkFields(watchNextResult.getVideoMetadata());
        checkSignedFields(watchNextResult.getVideoOwner());
        checkFields(watchNextResult.getNextVideo(), true);
        checkFields(watchNextResult.getSuggestedSections());
        checkFields(watchNextResult.getPlaylist());
    }

    private void checkFields(List<SuggestedSection> watchNextSections) {
        assertNotNull("Watch next contains rows", watchNextSections);

        SuggestedSection firstRow = watchNextSections.get(0);

        assertNotNull("Row has title", firstRow.getTitle());
        assertNotNull("Row has continuation data", firstRow.getNextPageKey());

        VideoItem watchNextItem = firstRow.getVideoSuggestions().get(0);

        checkFields(watchNextItem);
    }

    private void checkFields(VideoItem watchNextItem) {
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

    private void checkFields(CurrentVideo videoMetadata) {
        assertNotNull("Video metadata not empty", videoMetadata);
        assertNotNull("Video metadata has video id", videoMetadata.getVideoId());
        assertNotNull("Video metadata has title", videoMetadata.getTitle());
        assertNotNull("Video metadata has description", videoMetadata.getDescription());
        assertNotNull("Video metadata has published date", videoMetadata.getPublishedTime());
        assertNotNull("Video metadata has view count", videoMetadata.getViewCount());
        assertNotNull("Video metadata has like status", videoMetadata.getLikeStatus());
    }

    private void checkSignedFields(VideoOwner videoOwner) {
        assertNotNull("Video contains subscribe status", videoOwner.isSubscribed());
        checkFields(videoOwner);
    }

    private void checkFields(VideoOwner videoOwner) {
        assertNotNull("Video owner not empty", videoOwner);
        assertNotNull("Video owner has channel id", videoOwner.getChannelId());
        assertNotNull("Video owner has subscriber count", videoOwner.getSubscriberCount());
        assertNotNull("Video owner has author name", videoOwner.getVideoAuthor());
    }

    private void checkFields(NextVideo nextVideo, boolean isPlaylist) {
        assertNotNull("Next video not empty", nextVideo);
        assertNotNull("Next video has video id", nextVideo.getVideoId());
        assertNotNull("Next video has title", nextVideo.getTitle());
        assertNotNull("Next video has author name", nextVideo.getAuthor());
        assertNotNull("Next video has thumbnails", nextVideo.getThumbnails());

        if (isPlaylist) {
            assertNotNull("Next video has playlist id", nextVideo.getPlaylistId());
            assertTrue("Next video has playlist index", nextVideo.getPlaylistItemIndex() >= 0);
        }
    }

    private void checkFields(Playlist playlist) {
        assertNotNull("Playlist not empty", playlist);
        assertNotNull("Playlist has playlist id", playlist.getPlaylistId());
        assertTrue("Playlist has playlist index", playlist.getPlaylistIndex() >= 0);
        assertNotNull("Playlist has title", playlist.getTitle());
        assertTrue("Playlist has author name", playlist.getTotalVideos() > 0);
    }
}
