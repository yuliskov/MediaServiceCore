package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.next.models.WatchNextItem;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.next.models.WatchNextSection;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WatchNextManagerTestBase {
    public void checkWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);
        List<WatchNextSection> allRows = watchNextResult.getSections();
        assertNotNull("Watch next contains rows", allRows);

        WatchNextSection firstRow = allRows.get(0);

        assertFalse("Row has title", firstRow.getTitle().isEmpty());
        assertFalse("Row has continuation data", firstRow.getNextPageKey().isEmpty());

        WatchNextItem watchNextItem = firstRow.getWatchNextItems().get(0);

        checkFields(watchNextItem);
    }

    private void checkFields(WatchNextItem watchNextItem) {
        assertFalse("Watch next item has title", watchNextItem.getTitle().isEmpty());
        assertFalse("Watch next item has video id", watchNextItem.getVideoId().isEmpty());
        assertFalse("Watch next item has user name", watchNextItem.getUserName().isEmpty());
        assertFalse("Watch next item has channel id", watchNextItem.getChannelId().isEmpty());
        assertFalse("Watch next item has view count", watchNextItem.getViewCount().isEmpty());
        assertFalse("Watch next item has length", watchNextItem.getLengthText().isEmpty());
        assertTrue("Watch next item has thumbnails", watchNextItem.getThumbnails().size() > 0);
    }
}
