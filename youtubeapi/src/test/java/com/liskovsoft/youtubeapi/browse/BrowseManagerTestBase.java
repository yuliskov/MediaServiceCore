package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrowseManagerTestBase {
    protected void testThatItemsIsUnique(ItemWrapper item1, ItemWrapper item2) {
        String title1 = null;
        String title2 = null;

        if (item1.getVideoItem() != null) {
            VideoItem videoItem1 = item1.getVideoItem();
            VideoItem videoItem2 = item2.getVideoItem();

            assertNotNull("Video1 title null " + videoItem1.getVideoId(), videoItem1.getTitle());
            assertNotNull("Video2 title null " + videoItem2.getVideoId(), videoItem2.getTitle());

            title1 = videoItem1.getTitle();
            title2 = videoItem2.getTitle();
        } else if (item1.getMusicItem() != null) {
            title1 = item1.getMusicItem().getTitle();
            title2 = item2.getMusicItem().getTitle();
        } else if (item1.getRadioItem() != null) {
            title1 = item1.getRadioItem().getTitle();
            title2 = item2.getRadioItem().getTitle();
        } else if (item1.getPlaylistItem() != null) {
            title1 = item1.getPlaylistItem().getTitle();
            title2 = item2.getPlaylistItem().getTitle();
        } else if (item1.getChannelItem() != null) {
            title1 = item1.getChannelItem().getTitle();
            title2 = item2.getChannelItem().getTitle();
        }

        assertNotNull("Video1 not null", title1);
        assertNotNull("Video2 not null", title2);

        assertNotEquals("Music tab continuation is unique", title1, title2);
    }

    protected void testFields(ItemWrapper itemWrapper) {
        if (itemWrapper.getPlaylistItem() != null) {
            testFields(itemWrapper.getPlaylistItem());
        } else if (itemWrapper.getVideoItem() != null) {
            testFields(itemWrapper.getVideoItem());
        } else if (itemWrapper.getMusicItem() != null) {
            testFields(itemWrapper.getMusicItem());
        }
    }

    protected void testFields(PlaylistItem playlistItem) {
        assertNotNull("Title not null", playlistItem.getTitle());
        assertNotNull("Description not null", playlistItem.getDescription());
        String playlistId = playlistItem.getPlaylistId();
        assertNotNull("Playlist Id not null", playlistId);
        assertNotNull("Video count not null: " + playlistId, playlistItem.getVideoCountText());
        assertNotNull("Channel not null: " + playlistId, playlistItem.getChannelId());
        assertNotNull("Thumbs not null: " + playlistId, playlistItem.getThumbnails());
    }

    protected void testFields(VideoItem videoItem) {
        assertNotNull("Title not null", videoItem.getTitle());
        String videoId = videoItem.getVideoId();
        assertNotNull("Id not null", videoId);
        assertTrue("Time not null or live: " + videoId, videoItem.getPublishedTime() != null || videoItem.isLive());
        assertTrue("Length not null or live: " + videoId, videoItem.getLengthText() != null || videoItem.isLive());
        //assertNotNull("Channel not null: " + videoId, videoItem.getChannelId());
        assertNotNull("User not null: " + videoId, videoItem.getUserName());
        assertNotNull("Thumbs not null: " + videoId, videoItem.getThumbnails());

        // Absent in Subscribe section
        //assertNotNull("Views not null: " + videoId, videoItem.getViewCountText());
    }

    protected void testFields(MusicItem musicItem) {
        assertNotNull("Title not null", musicItem.getTitle());
        String videoId = musicItem.getVideoId();
        assertNotNull("Id not null", videoId);
        assertNotNull("Time not null: " + videoId, musicItem.getPublishedText());
        assertNotNull("Views not null: " + videoId, musicItem.getViewCountText());
        assertNotNull("Length not null: " + videoId, musicItem.getLengthText());
        assertNotNull("User not null: " + videoId, musicItem.getUserName());
        assertNotNull("Thumbs not null: " + videoId, musicItem.getThumbnails());

        // Absent in Recommended section
        //assertNotNull("Channel not null: " + videoId, musicItem.getChannelId());
    }
}
