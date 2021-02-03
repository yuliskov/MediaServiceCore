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
        checkSuggestedSection(watchNextResult.getSuggestedSections().get(0));
    }

    public void checkSignedWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);

        checkFields(watchNextResult.getVideoMetadata());
        checkSignedFields(watchNextResult.getVideoOwner());
        checkFields(watchNextResult.getNextVideo(), false);
        checkSuggestedSection(watchNextResult.getSuggestedSections().get(0));
    }

    public void checkSignedPlaylistWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);

        checkFields(watchNextResult.getVideoMetadata());
        checkSignedFields(watchNextResult.getVideoOwner());
        checkFields(watchNextResult.getNextVideo(), true);
        // Skip first playlist row 'cause not all fields present here
        checkSuggestedSection(watchNextResult.getSuggestedSections().get(1));
        checkPlaylist(watchNextResult.getPlaylist());
    }

    private void checkSuggestedSection(SuggestedSection suggestedSection) {
        assertNotNull("Watch next contains rows", suggestedSection);

        assertNotNull("Row has title", suggestedSection.getTitle());
        assertNotNull("Row has continuation data", suggestedSection.getNextPageKey());

        VideoItem suggestedItem = suggestedSection.getItemWrappers().get(0).getVideoItem();

        checkSuggestedItem(suggestedItem);
    }

    private void checkSuggestedItem(VideoItem videoItem) {
        String videoId = videoItem.getVideoId();
        assertNotNull("Suggested item has title: " + videoId, videoItem.getTitle());
        assertNotNull("Suggested item has video id: " + videoId, videoId);
        assertNotNull("Suggested item has user name: " + videoId, videoItem.getUserName());
        assertNotNull("Suggested item has channel id: " + videoId, videoItem.getChannelId());
        assertTrue("Suggested item has view count: " + videoId, videoItem.getViewCountText() != null || videoItem.getShortViewCountText() != null || videoItem.isLive());
        assertTrue("Suggested item has length: " + videoId, videoItem.getLengthText() != null || videoItem.isLive());
        assertNotNull("Suggested item has thumbnails: " + videoId, videoItem.getThumbnails());
        assertTrue("Suggested item thumbnails not empty: " + videoId, videoItem.getThumbnails().size() > 0);
    }

    private void checkFields(CurrentVideo videoMetadata) {
        assertNotNull("Video metadata not empty", videoMetadata);
        assertNotNull("Video metadata has video id", videoMetadata.getVideoId());
        assertNotNull("Video metadata has title", videoMetadata.getTitle());
        assertNotNull("Video metadata has description", videoMetadata.getDescription());
        assertNotNull("Video metadata has published date", videoMetadata.getPublishedDate());
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

    private void checkPlaylist(Playlist playlist) {
        assertNotNull("Playlist not empty", playlist);
        assertNotNull("Playlist has playlist id", playlist.getPlaylistId());
        assertTrue("Playlist has playlist index", playlist.getPlaylistIndex() >= 0);
        assertNotNull("Playlist has title", playlist.getTitle());
        assertTrue("Playlist has author name", playlist.getTotalVideos() > 0);
    }
}
